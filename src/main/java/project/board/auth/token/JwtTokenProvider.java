package project.board.auth.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.access-secret}")
    private String accessSecret;

    @Value("${jwt.refresh-secret}")
    private String refreshSecret;

    private Key accessKey;
    private Key refreshKey;

    private final long EXPIRATION = 1000L * 60 * 30;
    private final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7;

    @PostConstruct
    public void init(){
        this.accessKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(accessSecret.getBytes()));
        this.refreshKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(refreshSecret.getBytes()));
    }

    // JWT 토큰 생성
    public String generateAccessToken(Authentication authentication){
        String username = authentication.getName();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(accessKey, SignatureAlgorithm.HS512)
                .compact();
    }
    public String generateRefreshToken(Authentication authentication){
        String username = authentication.getName();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(refreshKey, SignatureAlgorithm.HS512)
                .compact();
    }

    //JWT 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token, boolean isRefresh){
        String subject = Jwts.parserBuilder()
                .setSigningKey(isRefresh ? refreshKey : accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        log.info("get username from token{}", subject);
        return subject;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token, boolean isRefresh) {
        try{
            Jwts.parserBuilder().setSigningKey(isRefresh ? refreshKey : accessKey)
                    .build().parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
