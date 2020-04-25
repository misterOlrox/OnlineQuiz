package com.olrox.quiz.service.sender;

import com.olrox.quiz.dto.NewOnlineUserInfo;
import com.olrox.quiz.dto.OnlineUsersTopicInfo;
import com.olrox.quiz.dto.UserDto;
import com.olrox.quiz.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TopicOnlineInfoSender {

    private final Set<UserDto> onlineUsersSet = ConcurrentHashMap.newKeySet();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendOnlineUsersInfo(Set<User> onlineUsers) {
        onlineUsersSet.clear();
        onlineUsers.forEach(user -> onlineUsersSet.add(UserDto.from(user)));
        sendCurrentSet();
    }

    private void sendCurrentSet() {
        OnlineUsersTopicInfo message = new OnlineUsersTopicInfo(Set.copyOf(onlineUsersSet));
        simpMessagingTemplate.convertAndSend("/topic/online", message);
    }

    public void addToSet(User user) {
        UserDto newUserDto = UserDto.from(user);
        onlineUsersSet.add(newUserDto);
        simpMessagingTemplate.convertAndSend(
                "/topic/online", new NewOnlineUserInfo(newUserDto));
        simpMessagingTemplate.convertAndSend(
                "/topic/online", new OnlineUsersTopicInfo(onlineUsersSet)
        );
    }

}
