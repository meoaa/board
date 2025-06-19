package project.board.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RefreshToken {

    @Id
    private String username;

    @Column(nullable = false)
    private String refreshToken;

    public void updateToken(String newToken) {
        this.refreshToken = newToken;
    }
}
