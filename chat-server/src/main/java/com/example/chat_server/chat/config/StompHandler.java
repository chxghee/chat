package com.example.chat_server.chat.config;

import com.example.chat_server.common.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

// 사용자 인증 작업
@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);


        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("Stomp connect 시 토큰 검증");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String accessToken = jwtTokenProvider.getAccessToken(bearerToken);
            jwtTokenProvider.getClaims(accessToken);
            log.info("Stomp connect 시 토큰 완료");
        }

        return message;
    }
}
