package project.board.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.board.post.dto.PostListResponseDto;
import project.board.post.dto.QPostListResponseDto;
import java.util.List;
import static project.board.member.domain.QMember.*;
import static project.board.post.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostListResponseDto> findAllWithMember(Pageable pageable, String query) {

        List<PostListResponseDto> content = queryFactory
                .select(new QPostListResponseDto(
                        post.id,
                        post.title,
                        member.nickname,
                        post.createdAt
                ))
                .from(post)
                .leftJoin(post.member, member)
                .where(
                        titleEq(query)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(titleEq(query))
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    private BooleanExpression titleEq(String query){
        return query != null ? post.title.contains(query) : null;
    }

//    @Override
//    public Page<Post> findAllWithMember(Pageable pageable) {
//        List<Post> content = queryFactory
//                .selectFrom(post)
//                .leftJoin(post.member, member).fetchJoin()
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(post.id.desc())
//                .fetch();
//
//        Long total = queryFactory
//                .select(post.count())
//                .from(post)
//                .fetchOne();
//
//        return new PageImpl<>(content, pageable, total != null ? total : 0);
//    }


}
