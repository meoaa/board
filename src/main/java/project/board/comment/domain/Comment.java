package project.board.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.board.common.domain.BaseEntity;
import project.board.member.domain.Member;
import project.board.post.domain.Post;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Builder
    public Comment(String content, Member member, Post post) {
        this.content = content;
        if(member != null){
            addMember(member);
        }
        if(post != null){
            addPost(post);
        }
    }

    public void addMember(Member member){
        this.member = member;
        member.getComments().add(this);
    }

    public void addPost(Post post){
        this.post = post;
        post.getComments().add(this);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
