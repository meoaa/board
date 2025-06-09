package project.board.post.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.board.common.domain.BaseEntity;
import project.board.comment.domain.Comment;
import project.board.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        if(member != null){
            addMember(member);
        }
    }

    /* 의존관계 메서드 */
    private void addMember(Member member){
        this.member = member;
        member.getPosts().add(this);
    }
}
