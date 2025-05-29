package project.board.service;

import project.board.dto.member.MemberResponseDto;
import project.board.dto.member.MemberSignUpRequestDto;

import java.util.List;

public interface MemberService {
    void addMember(MemberSignUpRequestDto dto);

    MemberResponseDto searchMemberById(long id);

    List<MemberResponseDto> searchAllMember();

    void deleteMember(long id);
}
