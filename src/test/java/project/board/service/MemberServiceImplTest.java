package project.board.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.transaction.annotation.Transactional;
import project.board.member.domain.Member;
import project.board.auth.dto.SignUpRequestDto;
import project.board.auth.dto.SignUpResponseDto;
import project.board.common.exception.ExistEmailException;
import project.board.common.exception.ExistUsernameException;
import project.board.common.exception.MissMatchOldPassword;
import project.board.member.dto.MemberResponseDto;
import project.board.member.dto.MemberUpdateProfileRequestDto;
import project.board.member.dto.PasswordChangeRequestDto;
import project.board.member.service.MemberService;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager em;

    @Test
    public void 회원가입_성공(){
        SignUpRequestDto requestDto =
                new SignUpRequestDto(
                        "test name",
                        "password",
                        "test mail",
                        "test nickname");

        SignUpResponseDto responseDto = memberService.addMember(requestDto);

        Assertions.assertThat(responseDto.getUsername()).isEqualTo(requestDto.getUsername());
    }

    @Test
    public void 회원가입_아이디_중복예외(){
        SignUpRequestDto dto1 =
                new SignUpRequestDto("user1", "pw1", "same@mail.com", "nick1");
        SignUpRequestDto sameUsernameDto =
                new SignUpRequestDto(
                        "user1",
                        "pw1",
                        "same@mail.com1",
                        "nick1");

        memberService.addMember(dto1);

        assertThrows(ExistUsernameException.class, ()->{
            memberService.addMember(sameUsernameDto);
        });
    }

    @Test
    public void 회원가입_이메일_중복예외(){
        SignUpRequestDto dto1 =
                new SignUpRequestDto("user1",
                        "pw1",
                        "same@mail.com",
                        "nick1");

        memberService.addMember(dto1);

        SignUpRequestDto sameEmailDto =
                new SignUpRequestDto(
                        "user2",
                        "pw1",
                        "same@mail.com",
                        "nick1");

        assertThrows(ExistEmailException.class, ()->{
            memberService.addMember(sameEmailDto);
        });
    }

    @Test
    @Transactional
    public void 회원정보_업데이트(){
        SignUpRequestDto signupDto = new SignUpRequestDto(
                "user1",
                "pw1",
                "email@email.com",
                "nick"
        );
        SignUpResponseDto res = memberService.addMember(signupDto);

        em.flush();
        em.clear();

        MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
        requestDto.setNickname("changed nickname");
        requestDto.setEmail("changed email");

        MemberResponseDto responseDto = memberService.updateMember(res.getId(), requestDto);

        Assertions.assertThat(responseDto.getNickname()).isEqualTo(requestDto.getNickname());
        Assertions.assertThat(responseDto.getEmail()).isEqualTo(responseDto.getEmail());
    }
    @Test
    @Transactional
    public void 회원정보_업데이트시_존재하는_이메일_예외(){
        SignUpRequestDto signupDto1 = new SignUpRequestDto(
                "user1",
                "pw1",
                "email@email.com",
                "nick"
        );
        SignUpRequestDto signupDto2 = new SignUpRequestDto(
                "user2",
                "pw1",
                "email@email.com2",
                "nick"
        );
        SignUpResponseDto res = memberService.addMember(signupDto1);
        memberService.addMember(signupDto2);

        em.flush();
        em.clear();

        MemberUpdateProfileRequestDto requestDto = new MemberUpdateProfileRequestDto();
        requestDto.setEmail("email@email.com2");

        assertThrows(ExistEmailException.class,
                () -> memberService.updateMember(res.getId(), requestDto));

    }

    @Test
    @Transactional
    //TODO:Security 도입시 나중에 passwordEncoder match로변경
    public void 비밀번호_변경_테스트(){
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "user",
                "pw1",
                "email@email.com",
                "nick");

        SignUpResponseDto signupResDto = memberService.addMember(requestDto);

        em.flush();
        em.clear();

        PasswordChangeRequestDto changeRequestDto = new PasswordChangeRequestDto();
        changeRequestDto.setOldPassword("pw1");
        changeRequestDto.setNewPassword("pw2");
        changeRequestDto.setNewPasswordConfirm("pw2");

        memberService.changePassword(signupResDto.getId(), changeRequestDto);
        Member member = em.find(Member.class, signupResDto.getId());

        Assertions.assertThat("pw2")
                .isEqualTo(member.getPassword());
    }

    @Test
    @Transactional
    public void 비밀번호_변경시_기존의_비밀번호_불일치_예외발생(){
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "user",
                "pw1",
                "email@email.com",
                "nick");

        SignUpResponseDto signupResDto = memberService.addMember(requestDto);

        em.flush();
        em.clear();

        PasswordChangeRequestDto changeRequestDto = new PasswordChangeRequestDto();
        changeRequestDto.setOldPassword("pw2");
        changeRequestDto.setNewPassword("pw2");
        changeRequestDto.setNewPasswordConfirm("pw2");

        assertThrows(MissMatchOldPassword.class,
                () -> memberService.changePassword(signupResDto.getId(), changeRequestDto));
    }

}