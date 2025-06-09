package project.board.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.post.domain.Post;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContaining(String keyword);
    List<Post> findByMember_UsernameContaining(String keyword);

}
