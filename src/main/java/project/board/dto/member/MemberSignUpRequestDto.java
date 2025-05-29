package project.board.dto.member;

import lombok.Data;

@Data
public class MemberSignUpRequestDto {

    private String username;
    private String password;
    private String email;
    private String nickname;
}
