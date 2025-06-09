package project.board.auth.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {

    private String username;
    private String password;
    private String email;
    private String nickname;

    public SignUpRequestDto(String username, String password, String email, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }
}
