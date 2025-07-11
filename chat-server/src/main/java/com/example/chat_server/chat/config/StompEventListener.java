package com.example.chat_server.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// stomp는 세션 관리를 알아서 해줌
// 연결 연결해제 시 이벤트 기록하고 연결된 세션 수를 실시간으로 확인할 목적으로 이벤트 리스너를 생성 -> 로그 디버깅 목적
@Slf4j
@Component
public class StompEventListener {

    private final Set<String> sessions = ConcurrentHashMap.newKeySet();

    // 세션 연결 이벤트 시 실행되는 메서드
    @EventListener
    public void connectHandle(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.add(accessor.getSessionId());
        log.info("connect session ID " + accessor.getSessionId());
        log.info("총 세션 개수: " + sessions.size());
    }

    // 세션 종료 이벤트 시 실행되는 메서드
    @EventListener
    public void disconnectHandle(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.remove(accessor.getSessionId());
        log.info("disconnect session ID " + accessor.getSessionId());
        log.info("총 세션 개수: " + sessions.size());
    }

}
