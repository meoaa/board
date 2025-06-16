package project.board.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.board.post.domain.Post;
import project.board.post.domain.QPost;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findAllPosts() {
        QPost post = QPost.post;

        return queryFactory
                .selectFrom(post)
                .fetch();
    }
}
