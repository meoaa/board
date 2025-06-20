package project.board.auth.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.board.auth.domain.RefreshToken;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class RefreshTokenServiceTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("새로운 유저,토큰 저장 테스트")
    public void newTokenSaveTest(){
        String username = "username";
        String token = "token";
        refreshTokenService.saveOrUpdate(username,token);

        em.flush();
        em.clear();

        RefreshToken findToken = refreshTokenService.findByUsername(username).get();

        Assertions.assertThat(findToken.getUsername()).isEqualTo(username);
        Assertions.assertThat(findToken.getRefreshToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("기존의 유저, 토큰 업데이트 테스트")
    public void tokenUpdateTest(){
        String username = "username";
        String oldToken = "old_token";
        String newToken = "new_token";

        refreshTokenService.saveOrUpdate(username, oldToken);


        em.flush();
        em.clear();

        RefreshToken findToken = refreshTokenService.findByUsername(username).get();
        Assertions.assertThat(findToken.getUsername()).isEqualTo(username);
        Assertions.assertThat(findToken.getRefreshToken()).isEqualTo(oldToken);

        refreshTokenService.saveOrUpdate(username, newToken);

        em.flush();
        em.clear();

        RefreshToken findUpdatedToken = refreshTokenService.findByUsername(username).get();

        Assertions.assertThat(findUpdatedToken.getUsername()).isEqualTo(username);
        Assertions.assertThat(findUpdatedToken.getRefreshToken()).isEqualTo(newToken);
    }

    @Test
    @DisplayName("토큰 제거 테스트")
    public void tokenDeleteTest(){
        String username = "username";
        String token = "token";

        refreshTokenService.saveOrUpdate(username, token);

        em.flush();
        em.clear();

        RefreshToken refreshToken = refreshTokenService.findByUsername(username).get();
        Assertions.assertThat(refreshToken.getUsername()).isEqualTo(username);

        refreshTokenService.deleteByUsername(username);

        em.flush();
        em.clear();

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, ()->refreshTokenService.findByUsername(username).get());
    }

    @Test
    @DisplayName("토큰 존재 여부 확인 테스트")
    public void tokenExistTest(){
        String username = "username";
        String token = "token";

        refreshTokenService.saveOrUpdate(username, token);

        em.flush();
        em.clear();

        boolean valid = refreshTokenService.exists(username, token);

        Assertions.assertThat(valid).isTrue();
    }

    @Test
    @DisplayName("토큰명 불일치시 여부 확인 테스트")
    public void noDuplicateTokenNameExistTest(){
        String username = "username";
        String token = "token";
        String wrongToken = "wrong_token";

        refreshTokenService.saveOrUpdate(username, token);

        em.flush();
        em.clear();

        boolean valid = refreshTokenService.exists(username, wrongToken);

        Assertions.assertThat(valid).isFalse();
    }
}