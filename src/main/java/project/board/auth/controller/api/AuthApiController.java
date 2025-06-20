package project.board.auth.controller.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import project.board.auth.CustomUserDetails;

import project.board.auth.dto.LoginRequestDto;
import project.board.auth.service.RefreshTokenService;
import project.board.auth.token.JwtTokenProvider;
import project.board.common.ApiResponse;
import project.board.auth.dto.SignUpRequestDto;
import project.board.auth.dto.SignUpResponseDto;
import project.board.common.ErrorResponse;
import project.board.member.service.MemberService;


import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/createMember")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> createMember(
            @RequestBody SignUpRequestDto dto){
        log.info("{}", dto);
        SignUpResponseDto responseDto = memberService.addMember(dto);
        return ResponseEntity.ok(ApiResponse.of(200,"회원가입 완료", responseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody LoginRequestDto dto,
            HttpServletResponse response){
        log.info("login 컨트롤러 도달");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        Authentication authenticate =
                authenticationManager.authenticate(authToken);

        String accessToken = jwtTokenProvider.generateAccessToken(authenticate);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authenticate);

        refreshTokenService.saveOrUpdate(authenticate.getName(), refreshToken);
        log.info("name : {}" , authenticate.getName());

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(1800)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(604800)
                .sameSite("Strict")
                .build();


        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok(ApiResponse.of(200, "로그인 성공", null));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        log.info("refresh Token : {}", refreshToken);

        if (refreshToken != null) {
            String username = extractUsername(refreshToken);
            log.info("refresh token username : {}" , username);
            refreshTokenService.deleteByUsername(username);

        }

        ResponseCookie deleteAccessToken =
                ResponseCookie.from("accessToken", "")
                        .maxAge(0)
                        .path("/")
                        .build();

        ResponseCookie deleteRefreshToken =
                ResponseCookie.from("refreshToken", "")
                        .maxAge(0)
                        .path("/api/auth")
                        .build();

        response.addHeader("Set-Cookie", deleteAccessToken.toString());
        response.addHeader("Set-Cookie", deleteRefreshToken.toString());

        return ResponseEntity.ok("로그아웃 완료");
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails user){
        if(user == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "로그인 인증 성공",
                        Map.of(
                                "username", user.getUsername(),
                                "nickname", user.getNickname())));
    }

    private String extractUsername(String token){
        return jwtTokenProvider.getUsernameFromToken(token, true);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response){

        if(refreshToken == null) {
            return ResponseEntity.status(401).body(ErrorResponse.of(401,"Refresh Token 없음"));
        }
        try{
            //1 유효성 검사(토큰 자체 검사 + DB에 저장된 값과 일치 여부 확인)
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken, true);
            boolean valid = jwtTokenProvider.validateToken(refreshToken, true);

            if(!valid || !refreshTokenService.exists(username ,refreshToken)){
                return ResponseEntity.status(401).body(ErrorResponse.of(401, "Refresh Token 무효"));
            }

            //2 유저 정보 조회
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //3 새로운 Access Token 발급
            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

            ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(1800)
                    .build();

            response.addHeader("Set-Cookie", accessCookie.toString());

            return ResponseEntity.ok(ApiResponse.of(200, "accessToken 재발급 성공", null));


        } catch (Exception ex) {
            return ResponseEntity.status(401).body(ErrorResponse.of(401 ,"Refresh Token 처리 중 오류"));
        }
    }
}
