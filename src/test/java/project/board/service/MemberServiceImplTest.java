package project.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import project.board.dto.member.MemberSignUpRequestDto;
import project.board.dto.member.SignUpResponseDto;
import project.board.exception.ExistEmailException;
import project.board.exception.ExistUsernameException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void addMemberTest(){
        MemberSignUpRequestDto requestDto =
                new MemberSignUpRequestDto(
                        "test name",
                        "password",
                        "test mail",
                        "test nickname");

        SignUpResponseDto responseDto = memberService.addMember(requestDto);

        Assertions.assertThat(responseDto.getUsername()).isEqualTo(requestDto.getUsername());
    }

    @Test
    public void 회원가입_아이디_이메일_중복예외(){
        MemberSignUpRequestDto dto1 =
                new MemberSignUpRequestDto("user1", "pw1", "same@mail.com", "nick1");
        MemberSignUpRequestDto sameUsernameDto =
                new MemberSignUpRequestDto(
                        "user1",
                        "pw1",
                        "same@mail.com1",
                        "nick1");

        MemberSignUpRequestDto sameEmailDto =
                new MemberSignUpRequestDto(
                        "user2",
                        "pw1",
                        "same@mail.com",
                        "nick1");

        memberService.addMember(dto1);


        assertThrows(ExistUsernameException.class, ()->{
            memberService.addMember(sameUsernameDto);
        });
        assertThrows(ExistEmailException.class, ()->{
            memberService.addMember(sameEmailDto);
        });
    }
}