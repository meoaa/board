package project.board.member.service;

import project.board.auth.dto.SignUpRequestDto;
import project.board.auth.dto.SignUpResponseDto;
import project.board.member.dto.MemberResponseDto;
import project.board.member.dto.MemberUpdateProfileRequestDto;
import project.board.member.dto.PasswordChangeRequestDto;

import java.util.List;

public interface MemberService {
    SignUpResponseDto addMember(SignUpRequestDto dto);

    MemberResponseDto updateMember(Long id, MemberUpdateProfileRequestDto dto);

    MemberResponseDto searchMemberById(long id);

    MemberResponseDto changePassword(Long id, PasswordChangeRequestDto dto);

    List<MemberResponseDto> searchAllMember();

    void deleteMember(long id);
}
