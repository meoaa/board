package project.board.post.dto;

import lombok.Data;
import project.board.post.domain.Post;

import java.time.LocalDateTime;

@Data
public class PostListResponseDto {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdAt;

    private PostListResponseDto(Long id, String title, String writer, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.createdAt = createdAt;
    }

    public static PostListResponseDto from(Post post){
        return new PostListResponseDto(
                post.getId(),
                post.getTitle(),
                post.getMember().getNickname(),
                post.getCreatedAt()
        );
    }
}
