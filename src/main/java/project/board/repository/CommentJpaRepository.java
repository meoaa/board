package project.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.domain.Comment;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}
