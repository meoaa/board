package project.board.member.dto;

import lombok.Data;
import project.board.member.domain.Member;

import java.time.LocalDateTime;

@Data
public class MemberResponseDto {

    private String username;
    private String email;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MemberResponseDto(Member member) {
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
    }
}
