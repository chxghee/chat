package com.example.chat_server.member.domain;

import com.example.chat_server.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberExceptionCode implements ExceptionCode {

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "가입된 이메일", "가입된 이메일이 이미 존재합니다."),
    INVALID_CREDENTIALS(HttpStatus.CONFLICT, "로그인 실패", "아이디와 패스워드가 일치하지 않습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 유저", "해당 유저는 존재하지 않습니다."),
    NOT_FOUND_MEMBER_EMAIL(HttpStatus.UNAUTHORIZED, "존재하지 않는 이메일", "해당 이메일의 가입 정보가 없습니다."),
    MISS_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호 불일치", "비밀번호가 일치하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String title;
    private final String detail;
}
