package project.board.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Post> searchWithPaging(String keyword, Pageable pageable) {
        QPost post = QPost.post;

        List<Post> content = queryFactory
                .selectFrom(post)
                .where(
                        keyword != null ? post.title.containsIgnoreCase(keyword) : null
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        keyword != null ? post.title.containsIgnoreCase(keyword) : null
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
