package com.example.chat_server.member.presentaion;

import com.example.chat_server.common.auth.JwtTokenProvider;
import com.example.chat_server.member.application.MemberService;
import com.example.chat_server.member.domain.Member;
import com.example.chat_server.member.presentaion.request.LoginRequest;
import com.example.chat_server.member.presentaion.request.SignUpRequest;
import com.example.chat_server.member.presentaion.response.LoginResponse;
import com.example.chat_server.member.presentaion.response.MemberListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest) {

        System.out.println("ASSAD");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.valueOf(memberService.signUp(signUpRequest).getId()));
    }

    // 현재 토큰 바디에 넣어 보내는데 개선 필요
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Member loginMember = memberService.login(loginRequest);
        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getRole().toString());
        return ResponseEntity.ok(new LoginResponse(loginMember.getId(), token));
    }

    // 테스트용 api
    @GetMapping("/list")
    public ResponseEntity<List<MemberListResponse>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAllMembers());
    }
}
