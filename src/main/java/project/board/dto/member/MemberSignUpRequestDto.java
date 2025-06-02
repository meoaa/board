package project.board.dto.member;

import lombok.Data;

@Data
public class MemberSignUpRequestDto {

    private String username;
    private String password;
    private String email;
    private String nickname;

    public MemberSignUpRequestDto(String username, String password, String email, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }
}
