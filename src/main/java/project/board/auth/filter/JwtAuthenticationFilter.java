package project.board.auth.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import project.board.auth.CustomUserDetailsService;
import project.board.auth.token.JwtTokenProvider;

import java.io.IOException;

@Getter
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);

        try{
            if(token != null && jwtTokenProvider.validateToken(token)){
            String username = jwtTokenProvider.getUsernameFromToken(token);
            var userDetails = userDetailsService.loadUserByUsername(username);

            var authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            }
            filterChain.doFilter(request,response);
        }catch(JwtException e){
            log.warn("{}", e.getMessage());
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            return;
        }
    }

    private String resolveToken(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");

        String json = String.format("{\"code\": %d, \"message\": \"%s\",\"success\":\"false\"}", status.value(), message);
        response.getWriter().write(json);
    }
}
