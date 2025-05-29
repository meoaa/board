package project.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.board.domain.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
