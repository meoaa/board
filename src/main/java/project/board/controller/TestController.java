package project.board.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.board.dto.member.MemberSignUpRequestDto;
import project.board.service.MemberService;

@RestController
@AllArgsConstructor
@Slf4j
public class TestController {

    private final MemberService memberService;


    @PostMapping("/createMember")
    public String createMember(@RequestBody MemberSignUpRequestDto dto){
      log.info("{}", dto);
      memberService.addMember(dto);
      return "ok";
    }

}
