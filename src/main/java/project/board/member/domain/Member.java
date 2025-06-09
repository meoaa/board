package project.board.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.board.common.domain.BaseEntity;
import project.board.comment.domain.Comment;
import project.board.post.domain.Post;
import project.board.member.dto.MemberUpdateProfileRequestDto;
import project.board.member.dto.PasswordChangeRequestDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "username"})
})
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private ROLE role;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Member(String email, String password, String nickname, String username) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.username = username;
        this.role = ROLE.ROLE_USER;
    }

    /*변경 메서드*/
    public void updateProfile(MemberUpdateProfileRequestDto dto){
        this.nickname =  dto.getNickname();
        this.email = dto.getEmail();
    }
    public void changePassword(PasswordChangeRequestDto dto){
        this.password = dto.getNewPassword();
    }
}
