package project.board.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.auth.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUsername(String username);
}
