package project.board.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import project.board.post.domain.Post;

import java.time.LocalDateTime;

@Data
public class PostListResponseDto {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdAt;

    @QueryProjection
    public PostListResponseDto(Long id, String title, String writer, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.createdAt = createdAt;
    }
}
