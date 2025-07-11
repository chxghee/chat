package com.example.chat_server.chat.presentation.request;

public record ChatMessage(
        String message,
        String senderEmail
) {
}
