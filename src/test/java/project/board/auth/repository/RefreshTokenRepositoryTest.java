package project.board.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.board.auth.domain.RefreshToken;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("토큰 생성 및 조회 테스트")
    public void createTokenAndFindTest(){
        RefreshToken refreshToken =
                new RefreshToken(
                        "username",
                        "access_token");
        refreshTokenRepository.save(refreshToken);

        em.flush();
        em.clear();

        RefreshToken findToken = refreshTokenRepository.findByUsername(refreshToken.getUsername()).get();

        Assertions.assertThat(findToken.getUsername()).isEqualTo(refreshToken.getUsername());
        Assertions.assertThat(findToken.getRefreshToken()).isEqualTo(refreshToken.getRefreshToken());
    }

    @Test
    @DisplayName("토큰 업데이트 테스트")
    public void updatedTokenAndFindTest(){
        RefreshToken refreshToken = new RefreshToken("username", "access_token");

        refreshTokenRepository.save(refreshToken);

        em.flush();
        em.clear();
        String newTokenName = "new_token";
        RefreshToken findToken = refreshTokenRepository.findByUsername(refreshToken.getUsername()).get();
        findToken.updateToken(newTokenName);

        Assertions.assertThat(findToken.getRefreshToken()).isEqualTo(newTokenName);

    }


}