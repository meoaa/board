package project.board.auth.filter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
              if (token != null && jwtTokenProvider.validateToken(token)) {
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
          }catch(JwtException | IllegalArgumentException ex){
              log.warn("JWT validation failed: {}", ex.getMessage());
          }

        filterChain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
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
