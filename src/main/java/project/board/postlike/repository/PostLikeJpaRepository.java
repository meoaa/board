package project.board.postlike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.postlike.domain.PostLike;

public interface PostLikeJpaRepository extends JpaRepository<PostLike, Long> {
}
