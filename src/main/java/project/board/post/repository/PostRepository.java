package project.board.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.board.post.domain.Post;

import java.util.List;

//PostRepositoryCustom 상속(QueryDSL)
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    List<Post> findByTitleContaining(String keyword);
    List<Post> findByMember_UsernameContaining(String keyword);


//    @EntityGraph(attributePaths = {"member"})
//    Page<Post> findAll(Pageable pageable);
//    N + 1 문제 해결
}
