package project.board.service;

import project.board.dto.member.*;

import java.util.List;

public interface MemberService {
    SignUpResponseDto addMember(MemberSignUpRequestDto dto);

    MemberResponseDto updateMember(int id, MemberUpdateProfileRequestDto dto);

    MemberResponseDto searchMemberById(long id);

    MemberResponseDto changePassword(int id, PasswordChangeRequestDto dto);

    List<MemberResponseDto> searchAllMember();

    void deleteMember(long id);
}
