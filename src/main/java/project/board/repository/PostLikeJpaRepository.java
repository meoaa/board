package project.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.domain.PostLike;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, Long> {
}
