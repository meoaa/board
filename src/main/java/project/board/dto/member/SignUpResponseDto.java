package project.board.dto.member;

import lombok.Data;
import project.board.domain.Member;

@Data
public class SignUpResponseDto {
    private Long id;
    private String username;
    private String email;
    private String nickname;

    public SignUpResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
