package project.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.domain.Comment;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_Id(Long postId);
}
