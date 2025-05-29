package project.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.domain.Post;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContaining(String keyword);
    List<Post> findByMember_UsernameContaining(String keyword);

}
