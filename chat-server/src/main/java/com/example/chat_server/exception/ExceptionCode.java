package com.example.chat_server.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {
    HttpStatus getHttpStatus();
    String getTitle();
    String getDetail();
}
