package com.example.chat_server.exception;

public record ErrorResponse(
        String title,
        int status,
        String detail,
        String instance
) {
    public static ErrorResponse of(ExceptionCode code, String instance) {
        return new ErrorResponse(code.getTitle(), code.getHttpStatus().value(), code.getDetail(), instance);
    }
}
