package com.example.chat_server.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/connect")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();      //http 엔드포인트 요청을 허용하는
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry
                // '/publish/토픽번호' uri로 메세지를 발행
                // /publish 로 시작하는 uri로 메세지가 발행되면 컨트롤러에 @MessageMapping 메서드로 라우팅 됨
                .setApplicationDestinationPrefixes("/publish")

                .enableSimpleBroker("/topic");    // '/topic/토픽번호' uri로 메세지를 구독
    }

    // 웹소켓 요청(connect, subscribe, disconnect)드의 요청 시에는 http 헤더 등 http 메세지를 넣어올 수 있거, 이를 인터셉터를 통해 가로채 토큰을 검증 가능
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration
                .interceptors(stompHandler);

    }
}
