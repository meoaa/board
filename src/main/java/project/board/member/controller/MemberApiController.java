package project.board.member.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.board.common.ApiResponse;
import project.board.member.dto.MemberResponseDto;
import project.board.member.dto.MemberUpdateProfileRequestDto;
import project.board.member.dto.PasswordChangeRequestDto;
import project.board.member.service.MemberService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponseDto>> findMember(
            @PathVariable int id) {
        MemberResponseDto dto = memberService.searchMemberById(id);
        return ResponseEntity.ok(ApiResponse.of(200, "요청 성공", dto));
    }

    //TODO: Security 도입 후 @AuthenticationPrincipal 변경예정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponseDto>> updateProfile(
            @PathVariable long id,
            @RequestBody MemberUpdateProfileRequestDto requestDto
            ){
        log.info("{}", requestDto);
        MemberResponseDto responseDto = memberService.updateMember(id, requestDto);
        return ResponseEntity.ok(ApiResponse.of(200,"요청 성공", responseDto));
    }

    @PatchMapping("/password/{id}")
    public ResponseEntity<ApiResponse<MemberResponseDto>> changePassword(
            @PathVariable long id,
            @RequestBody PasswordChangeRequestDto dto){

        dto.validatePasswordMatch();
        MemberResponseDto responseDto = memberService.changePassword(id, dto);

        return ResponseEntity.ok(ApiResponse.of(200, "요청 성공", responseDto));
    }

}
