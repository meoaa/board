package project.board.auth.filter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import project.board.auth.token.JwtTokenProvider;

import java.io.IOException;

@Getter
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);
        log.info("token : {}" , token);

        try{
            if (token != null && jwtTokenProvider.validateToken(token, false)) {
                var userDetails = createUserDetails(token);

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
        } catch (SignatureException ex){
            request.setAttribute("jwtException", ex.getMessage());
        }

        filterChain.doFilter(request,response);
    }

    private UserDetails createUserDetails(String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token, false);
        var userDetails = userDetailsService.loadUserByUsername(username);
        return userDetails;
    }

    private String resolveToken(HttpServletRequest request){
        if (request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if("accessToken".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

//    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
//        response.setStatus(status.value());
//        response.setContentType("application/json; charset=UTF-8");
//
//        String json = String.format("{\"code\": %d, \"message\": \"%s\",\"success\":\"false\"}", status.value(), message);
//        response.getWriter().write(json);
//    }
}