package project.board.service;

import project.board.dto.member.MemberResponseDto;
import project.board.dto.member.MemberSignUpRequestDto;
import project.board.dto.member.MemberUpdateProfileRequestDto;
import project.board.dto.member.PasswordChangeRequestDto;

import java.util.List;

public interface MemberService {
    void addMember(MemberSignUpRequestDto dto);

    MemberResponseDto updateMember(int id, MemberUpdateProfileRequestDto dto);

    MemberResponseDto searchMemberById(long id);

    MemberResponseDto changePassword(int id, PasswordChangeRequestDto dto);

    List<MemberResponseDto> searchAllMember();

    void deleteMember(long id);
}
