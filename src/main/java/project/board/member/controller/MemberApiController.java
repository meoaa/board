package project.board.member.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.board.auth.CustomUserDetails;
import project.board.common.ApiResponse;
import project.board.member.dto.MemberResponseDto;
import project.board.member.dto.MemberUpdateProfileRequestDto;
import project.board.member.dto.PasswordChangeRequestDto;
import project.board.member.service.MemberService;

import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<?> findMember(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        long userId = userDetails.getId();

        MemberResponseDto memberResponseDto = memberService.searchMemberById(userId);

        return ResponseEntity.ok(ApiResponse.of(200,"요청완료", memberResponseDto));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<MemberResponseDto>> updateProfile(
            @RequestBody MemberUpdateProfileRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        log.info("{}", requestDto);
        long userId = userDetails.getId();

        MemberResponseDto responseDto = memberService.updateMember(userId, requestDto);
        return ResponseEntity.ok(ApiResponse.of(200,"요청 성공", responseDto));
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<MemberResponseDto>> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PasswordChangeRequestDto dto){

        dto.validatePasswordMatch();
        long userId = userDetails.getId();

        MemberResponseDto responseDto = memberService.changePassword(userId, dto);

        return ResponseEntity.ok(ApiResponse.of(200, "요청 성공", responseDto));
    }

}
