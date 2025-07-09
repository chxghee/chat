package com.example.chat_server.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ExceptionCode code;

    public ApplicationException(ExceptionCode code) {
        super(code.getTitle());
        this.code = code;
    }
}
