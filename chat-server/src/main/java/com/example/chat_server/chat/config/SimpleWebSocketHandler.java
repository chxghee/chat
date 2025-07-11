//package com.example.chat_server.chat.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//// /connect 로 웹소켓 연결 요청이 들어왔을때 처리할 클래스
////@Slf4j
////@Component
//public class SimpleWebSocketHandler extends TextWebSocketHandler {
//
//    // 연결된 사용자 정보를 저장할 자료구조
//    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
//
//
//    // 연결 시 메모리에 연결된 사용자 정보를 저장
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.add(session);
//        log.info("연결 성공!: " + session.getId());
//    }
//
//
//    // 사용자에게 메세지를 보내줌
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        log.info("받은 메세지: " + payload);
//
//        for (WebSocketSession s : sessions) {       // 현재 연결된 모든 사용자에게 메세지 전달
//            if(s.isOpen()) {
//                s.sendMessage(new TextMessage(payload));
//            }
//        }
//    }
//
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
//        log.info("연결 종료!: " + session.getId());
//    }
//}
