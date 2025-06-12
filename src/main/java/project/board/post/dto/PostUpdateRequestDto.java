package project.board.post.dto;

import lombok.Data;

@Data
public class PostUpdateRequestDto {
    private String title;
    private String content;
}
