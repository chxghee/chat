package com.example.chat_server.common.auth;

import com.example.chat_server.exception.ApplicationException;
import com.example.chat_server.exception.ErrorResponse;
import com.example.chat_server.exception.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilter {

    public final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getRequestURI().startsWith("/member/sign-up") || request.getRequestURI().startsWith("/member/login") || request.getRequestURI().startsWith("/connect")) {
            filterChain.doFilter(request, response); // 그냥 통과
            return;
        }

        try {

            String bearerToken = request.getHeader("Authorization");
            String accessToken = jwtTokenProvider.getAccessToken(bearerToken);
            Authentication authentication = getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            sendError(response, request, AuthExceptionCode.ACCESS_TOKEN_EXPIRED);
        } catch (MalformedJwtException | SecurityException e) {
            sendError(response, request, AuthExceptionCode.TOKEN_MALFORMED);
        } catch (UnsupportedJwtException e) {
            sendError(response, request, AuthExceptionCode.TOKEN_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            sendError(response, request, AuthExceptionCode.ACCESS_TOKEN_ILLEGAL);
        } catch (JwtException e) {
            sendError(response, request, AuthExceptionCode.ACCESS_TOKEN_INVALID);
        }
    }

    private Authentication getAuthentication(String accessToken) {
        Claims claims = jwtTokenProvider.getClaims(accessToken);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private void sendError(HttpServletResponse response, HttpServletRequest request, ExceptionCode exceptionCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(exceptionCode, request.getRequestURI());
        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
