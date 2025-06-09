package project.board.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import project.board.member.domain.Member;
import project.board.member.domain.ROLE;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("회원 저장후 조회")
    public void createMemberAndAdd(){
        Member member = Member.builder()
                .username("username1")
                .password("1234")
                .email("test@test.com")
                .nickname("nickname")
                .build();

        Member savedMember = memberJpaRepository.save(member);

        // when
        Optional<Member> found = memberJpaRepository.findById(savedMember.getId());

        // then
        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getUsername()).isEqualTo("username1");
        Assertions.assertThat(found.get().getRole()).isEqualTo(ROLE.ROLE_USER);}

    @Test
    @DisplayName("회원의 ID로 DB에서 조회")
    public void findMemberById(){
        Member member = Member.builder()
                .username("username1")
                .password("1234")
                .email("test@test.com")
                .nickname("nickname")
                .build();


        Member savedMember = memberJpaRepository.save(member);
        Long id = savedMember.getId();
        Optional<Member> findMember = memberJpaRepository.findById(id);

        Assertions.assertThat(findMember).isPresent();
        Assertions.assertThat(findMember.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("회원을 저장한 후 확인")
    public void findAllMember(){
        Member member1 = Member.builder()
                .username("username1")
                .password("1234")
                .email("test@test.com")
                .nickname("nickname")
                .build();

        Member member2 = Member.builder()
                .username("username2")
                .password("1234")
                .email("test2@test.com")
                .nickname("nickname2")
                .build();

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberJpaRepository.findAll();


        Assertions.assertThat(2).isEqualTo(members.size());
    }

    @Test
    @DisplayName("회원을 저장한 뒤 삭제하면 DB에서 제거되어야한다.")
    public void removeMember(){
        Member member1 = Member.builder()
                .username("username1")
                .password("1234")
                .email("test@test.com")
                .nickname("nickname")
                .build();

        memberJpaRepository.save(member1);

        memberJpaRepository.delete(member1);
        em.flush();
        em.clear();

        Optional<Member> findMember = memberJpaRepository.findById(1L);
        Assertions.assertThat(findMember).isEmpty();
    }

    @Test
    @DisplayName("회원의 사용자명으로 저장후 DB에서 조회한다.")
    public void findMemberByUsername(){
        Member member1 = Member.builder()
                .username("username1")
                .password("1234")
                .email("test@test.com")
                .nickname("nickname")
                .build();

        memberJpaRepository.save(member1);

        em.flush();
        em.clear();

        Optional<Member> findUser = memberJpaRepository.findByUsername("username1");

        Assertions.assertThat(findUser).isPresent();
        Assertions.assertThat("username1").isEqualTo(findUser.get().getUsername());
    }
}