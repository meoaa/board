package project.board.post.dto;

import lombok.Data;
import project.board.member.dto.MemberInfoDto;
import project.board.post.domain.Post;

import java.time.LocalDateTime;

@Data
public class PostResponseDto {
    private long id;
    private String title;
    private String content;
    private int hit;

    private MemberInfoDto member;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PostResponseDto(long id, String title, String content, int hit, MemberInfoDto member, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.member = member;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public static PostResponseDto from(Post post){
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getHit(),
                new MemberInfoDto(
                        post.getMember().getId(),
                        post.getMember().getUsername(),
                        post.getMember().getNickname()
                ),
                post.getCreatedAt(),
                post.getCreatedAt()
                );
    }
}
