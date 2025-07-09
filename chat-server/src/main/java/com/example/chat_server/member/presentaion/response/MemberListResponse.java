package com.example.chat_server.member.presentaion.response;

import com.example.chat_server.member.domain.Member;

public record MemberListResponse(
        Long id,
        String name,
        String email
) {
    public static MemberListResponse of(Member member) {
        return new MemberListResponse(member.getId(), member.getName(), member.getEmail());
    }
}
