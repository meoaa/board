package project.board.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import project.board.common.ErrorResponse;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse res,
                         AuthenticationException authException)
            throws IOException, ServletException {

        log.info("CustomAuthenticationEntryPoint");

        log.warn("Authentication failed: {}", authException.getMessage());
        log.warn("Authentication failed: {}", authException.getClass().getSimpleName());

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");

        String message;

        if(authException.getCause() instanceof SignatureException){
            message = "유효하지 않은 토큰입니다. 서명이 일치하지 않습니다.";
        }else{
            message = "로그인이 필요하거나, 인증에 실패하였습니다.";
        }
        ErrorResponse errorResponse = ErrorResponse.of(403, message);

        res.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
