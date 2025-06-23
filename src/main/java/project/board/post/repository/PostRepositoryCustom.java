package project.board.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.board.post.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findAllPosts();
    Page<Post> searchWithPaging(String keyword, Pageable pageable);
}
