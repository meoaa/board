package project.board.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.board.common.ApiResponse;
import project.board.dto.member.MemberResponseDto;
import project.board.dto.member.MemberSignUpRequestDto;
import project.board.dto.member.SignUpResponseDto;
import project.board.service.MemberService;

@RestController
@AllArgsConstructor
@Slf4j
public class TestController {

    private final MemberService memberService;


    @PostMapping("/createMember")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> createMember(@RequestBody MemberSignUpRequestDto dto){
      log.info("{}", dto);
        SignUpResponseDto responseDto = memberService.addMember(dto);
        return ResponseEntity.ok(ApiResponse.of(200,"요청 완료", responseDto));
    }

}
