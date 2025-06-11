package project.board.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse res,
                         AuthenticationException authException)
            throws IOException, ServletException {
        log.warn("Authentication failed: {}", authException.getMessage());
        log.warn("Authentication failed: {}", authException.getClass().getSimpleName());
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json; charset=UTF-8");

        String json = """
            {
                "code": 401,
                "message": "아이디 또는 비밀번호가 올바르지 않습니다.",
                "success" : "false"
            }
            """;

        res.getWriter().write(json);
    }
}
