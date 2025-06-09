package project.board.auth.dto;

import lombok.Data;
import project.board.member.domain.Member;

import java.time.LocalDateTime;

@Data
public class SignUpResponseDto {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private LocalDateTime createdAt;

    private SignUpResponseDto(Long id, String username, String email, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public static SignUpResponseDto from(Member member){
        return new SignUpResponseDto(member.getId(), member.getUsername(), member.getEmail(), member.getNickname(), member.getCreatedAt());
    }
}
