package project.board.auth.controller.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.board.auth.dto.AuthResponseDto;
import project.board.auth.dto.LoginRequestDto;
import project.board.auth.token.JwtTokenProvider;
import project.board.common.ApiResponse;
import project.board.auth.dto.SignUpRequestDto;
import project.board.auth.dto.SignUpResponseDto;
import project.board.member.service.MemberService;

import java.time.Duration;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/createMember")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> createMember(
            @RequestBody SignUpRequestDto dto){
        log.info("{}", dto);
        SignUpResponseDto responseDto = memberService.addMember(dto);
        return ResponseEntity.ok(ApiResponse.of(200,"회원가입 완료", responseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto dto,
            HttpServletResponse response){
        log.info("login 컨트롤러 도달");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        Authentication authenticate =
                authenticationManager.authenticate(authToken);

        String accessToken = jwtTokenProvider.generateAccessToken(authenticate);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authenticate);

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
                .path("/auth/refresh")
                .maxAge(604800)
                .sameSite("Strict")
                .build();


        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok("로그인 성공");
    }
}
