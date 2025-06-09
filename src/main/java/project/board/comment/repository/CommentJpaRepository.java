package project.board.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.comment.domain.Comment;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_Id(Long postId);
}
