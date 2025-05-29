package project.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "username"})
})
public class Member extends BaseEntity{

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
}
