package com.example.chat_server.chat.presentation;

import com.example.chat_server.chat.presentation.request.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompController {

    private final SimpMessageSendingOperations messagingTemplate;
//    // 방법1 메세지 수신과 송신을 한번에 처리
//    @MessageMapping("/{roomId}")
//    @SendTo("/topic/{roomId}")      // 해당 룸 아이디로 메세지를 발행햐 구독중인 클라이언트에게 메세지 전송
//    public String sendMessage(@DestinationVariable("roomId") Long roomId, String message) {
//        log.info("메세지: " + message);
//        return message;
//    }

    // 방법1 메세지 수신과 송신을 한번에 처리
    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable("roomId") Long roomId, ChatMessage chatMessage) {
        log.info("메세지: " + chatMessage.message());
        messagingTemplate.convertAndSend("/topic/" + roomId, chatMessage);      // @SendTo 와 같은 기능을 함
    }
}
