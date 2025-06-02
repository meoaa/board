package project.board.dto.member;

import lombok.Data;

@Data
public class MemberUpdateProfileRequestDto {

    private String nickname;
    private String email;
}
