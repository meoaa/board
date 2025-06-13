package project.board.auth.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/createMember")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> createMember(@RequestBody SignUpRequestDto dto){
        log.info("{}", dto);
        SignUpResponseDto responseDto = memberService.addMember(dto);
        return ResponseEntity.ok(ApiResponse.of(200,"회원가입 완료", responseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@RequestBody LoginRequestDto dto){
        log.info("login 컨트롤러 도달");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authToken);
        String token = jwtTokenProvider.generateToken(authenticate);

        return ResponseEntity.ok(ApiResponse.of(500,"로그인성공",new AuthResponseDto(token)));
    }
}
