package project.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCreateRequestDto {
    private String title;
    private String content;
}
