package com.example.chat_server.member.presentaion.request;

public record LoginRequest (
        String email,
        String password
){
}
