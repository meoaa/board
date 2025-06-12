package project.board.member.dto;

import lombok.Data;

@Data
public class MemberInfoDto {

    private long id;
    private String username;
    private String nickname;

    public MemberInfoDto(long id, String username, String nickname) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
    }
}
