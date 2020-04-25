package com.olrox.quiz.service;

import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.sender.TopicOnlineInfoSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.time.Clock;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUserRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(OnlineUserRegistry.class);

    public static final long ONLINE_TIMEOUT = 60000;

    private Map<String, User> currentConnectedUsers = new ConcurrentHashMap<>();
    private Map<User, Long> keptOnlineUsers = new ConcurrentHashMap<>();

    private final Clock clock;
    private final TopicOnlineInfoSender topicOnlineInfoSender;

    @Autowired
    public OnlineUserRegistry(Clock clock, TopicOnlineInfoSender topicOnlineInfoSender) {
        this.clock = clock;
        this.topicOnlineInfoSender = topicOnlineInfoSender;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        User user = getUser(event);
        if (user != null && currentConnectedUsers.containsKey(user.getUsername())) {
            removeDisconnected(user);
        }
    }

    private User getUser(AbstractSubProtocolEvent event) {
        Principal principal = event.getUser();
        if (principal == null) {
            LOG.warn("Principal was null in event [{}]", event.toString());
            return null;
        } else {
            try {
                return (User) ((Authentication) event.getUser()).getPrincipal();
            } catch (ClassCastException exception) {
                LOG.error("ClassCastException: ", exception);
                return null;
            }
        }
    }

    @Scheduled(fixedDelay = ONLINE_TIMEOUT)
    private void updateOnlineUsers() {
        removeOfflineUsers();
        long startTime = clock.millis();
        Set<User> onlineUserSet = new HashSet<>(currentConnectedUsers.values());
        onlineUserSet.addAll(keptOnlineUsers.keySet());
        LOG.info("Time spent for gettingOnlineUsers: {}", clock.millis() - startTime);
        LOG.info("Connected users [{}], kept online users [{}]", currentConnectedUsers.size(), keptOnlineUsers.size());

        topicOnlineInfoSender.sendOnlineUsersInfo(onlineUserSet);
    }

    public void addConnected(User user) {
        currentConnectedUsers.put(user.getUsername(), user);
        topicOnlineInfoSender.addToSet(user);
    }

    public void removeDisconnected(User user) {
        currentConnectedUsers.remove(user.getUsername());
        keptOnlineUsers.put(user, 5000L);
    }

    private void removeOfflineUsers() {
        long startTime = clock.millis();
        keptOnlineUsers.entrySet().removeIf(entry -> isOnlineTimeoutForUser(entry.getValue()));
        LOG.info("Time spent for removing: {}", clock.millis() - startTime);
    }

    private boolean isOnlineTimeoutForUser(long lastSeenTime) {
        return clock.millis() - lastSeenTime >= ONLINE_TIMEOUT;
    }
}
