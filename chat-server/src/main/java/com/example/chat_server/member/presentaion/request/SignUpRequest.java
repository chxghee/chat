package com.example.chat_server.member.presentaion.request;

public record SignUpRequest(
        String name,
        String email,
        String password
) {
}
