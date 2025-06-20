package project.board.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import java.util.Objects;

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
        String requestURI = req.getRequestURI();


        if(requestURI.equals("/posts/create")){
            res.sendRedirect("/login?error=unauthorized");
            return;
        }

        log.info("CustomAuthenticationEntryPoint");
        log.warn("Authentication failed: {}", authException.getMessage());
        log.warn("Authentication failed: {}", authException.getClass().getSimpleName());
        log.warn("Authentication failed: {}", req.getAttribute("jwtException"));

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");

        String jwtExceptionMessage = (String) req.getAttribute("jwtException");
        String message;

        message = Objects.requireNonNullElse(jwtExceptionMessage, "로그인이 필요하거나, 인증에 실패하였습니다.");

        ErrorResponse errorResponse = ErrorResponse.of(403, message);

        res.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
