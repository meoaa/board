package project.board.member.dto;

import lombok.Data;

@Data
public class MemberUpdateProfileRequestDto {

    private String nickname;
    private String email;
}
