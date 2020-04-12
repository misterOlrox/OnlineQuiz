package com.olrox.quiz.config;

import com.olrox.quiz.entity.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebsocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated()
                .simpDestMatchers("/topic/tracker").hasAuthority(Role.ADMIN.name())
                // matches any destination that starts with /topic/
                // (i.e. cannot send messages directly to /topic/)
                // (i.e. cannot subscribe to /topic/messages/* to get messages sent to
                // /topic/messages-user<id>)
                .simpDestMatchers("/topic/**").authenticated()
                // message types other than MESSAGE and SUBSCRIBE
                //.simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
                // catch all
                .anyMessage().authenticated();
    }

    /**
     * Disables CSRF for Websockets.
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
