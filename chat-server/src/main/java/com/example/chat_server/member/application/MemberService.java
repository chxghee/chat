package com.example.chat_server.member.application;

import com.example.chat_server.exception.ApplicationException;
import com.example.chat_server.member.domain.Member;
import com.example.chat_server.member.domain.MemberExceptionCode;
import com.example.chat_server.member.domain.MemberRepository;
import com.example.chat_server.member.presentaion.request.LoginRequest;
import com.example.chat_server.member.presentaion.request.SignUpRequest;
import com.example.chat_server.member.presentaion.response.MemberListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Member signUp(SignUpRequest signUpRequest) {
        Member signUpMember = Member.builder()
                .name(signUpRequest.name())
                .email(signUpRequest.email())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .build();
        try {
            System.out.println("ASDADS");
            memberRepository.save(signUpMember);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(MemberExceptionCode.DUPLICATE_EMAIL);
        }
        return signUpMember;
    }

    public Member login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ApplicationException(MemberExceptionCode.NOT_FOUND_MEMBER_EMAIL));
        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new ApplicationException(MemberExceptionCode.MISS_MATCH_PASSWORD);
        }
        return member;
    }

    public List<MemberListResponse> findAllMembers() {
        List<Member> all = memberRepository.findAll();
        return all.stream()
                .map(MemberListResponse::of)
                .toList();
    }

}
