package project.board.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.board.post.domain.Post;
import project.board.post.dto.PostListResponseDto;

public interface PostRepositoryCustom {
   // Page<Post> findAllWithMember(Pageable pageable);
    Page<PostListResponseDto> findAllWithMember(Pageable pageable, String query);
}
