package project.board.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.board.auth.domain.RefreshToken;
import project.board.auth.repository.RefreshTokenRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveOrUpdate(String username, String token) {
        refreshTokenRepository.findByUsername(username)
                .ifPresentOrElse(
                        rt -> rt.updateToken(token),
                        () -> refreshTokenRepository.save(new RefreshToken(username, token))
                );
    }

    public Optional<RefreshToken> findByUsername(String username) {
        return refreshTokenRepository.findByUsername(username);
    }

    @Transactional
    public void deleteByUsername(String username) {
        refreshTokenRepository.deleteById(username);
    }


    public boolean exists(String username, String token){
        return refreshTokenRepository.findByUsername(username)
                .map(saved -> saved.getRefreshToken().equals(token))
                .orElse(false);
    }
}
