package project.board.repository;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import project.board.member.domain.Member;
import project.board.member.repository.MemberJpaRepository;
import project.board.post.domain.Post;
import project.board.post.repository.PostRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class PostJpaRepositoryTest {

    @Autowired
    private PostRepository postJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Member와 Post를 생성하고 저장한 뒤 일치하는 지 확인한다.")
    public void createMemberAndPostTest(){
        Member member = Member.builder()
                .username("username")
                .password("1234")
                .email("email")
                .nickname("nickname")
                .build();

        Post post = Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();


        Member savedMember = memberJpaRepository.save(member);
        Post savedPost = postJpaRepository.save(post);

        em.flush();
        em.clear();

        assertThat("title").isEqualTo(savedPost.getTitle());
        assertThat("content").isEqualTo(savedPost.getContent());
        assertThat(savedMember).isEqualTo(savedPost.getMember());
        assertThat(savedMember.getUsername()).isEqualTo(savedPost.getMember().getUsername());
        assertThat(savedMember.getPosts()).contains(savedPost);
    }

    @Test
    @DisplayName("Member와 Post를 생성한 뒤 저장하고, ID를 통해 조회한다.")
    public void findPostAndMemberById(){
        Member member = Member.builder()
                .username("username")
                .password("1234")
                .email("email")
                .nickname("nickname")
                .build();

        Post post = Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();

        Member savedMember = memberJpaRepository.save(member);
        Post savedPost = postJpaRepository.save(post);

        em.flush();
        em.clear();

        Member findMember = memberJpaRepository.findById(savedMember.getId()).orElseThrow();
        Post findPost = postJpaRepository.findById(savedPost.getId()).orElseThrow();

        assertThat(savedPost.getId()).isEqualTo(findPost.getId());
        assertThat(savedPost.getMember().getId()).isEqualTo(findPost.getMember().getId());
        assertThat(findMember.getId()).isEqualTo(findPost.getMember().getId());
        assertThat(savedMember.getId()).isEqualTo(findPost.getMember().getId());
        assertThat(findMember.getPosts()).contains(findPost);
        assertThat(findMember.getEmail()).isEqualTo(findPost.getMember().getEmail());
    }

    @Test
    @DisplayName("Member와 Post를 생성하고 전체 게시글 목록을 가져온다.")
    public void findPostAll(){
        Member member = Member.builder()
                .username("username")
                .password("1234")
                .email("email")
                .nickname("nickname")
                .build();

        Post post1 = Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();

        Post post2 = Post.builder()
                .title("title2")
                .content("content2")
                .member(member)
                .build();

        memberJpaRepository.save(member);

        postJpaRepository.save(post1);
        postJpaRepository.save(post2);

        em.flush();
        em.clear();
        List<Post> posts = postJpaRepository.findAll();

        assertThat(posts.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Member와 Post를 생성하고 등록한 뒤에 Post 삭제")
    public void deletePost(){
        Member member = Member.builder()
                .username("username")
                .password("1234")
                .email("email")
                .nickname("nickname")
                .build();

        Post post1 = Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();

        Post post2 = Post.builder()
                .title("title2")
                .content("content2")
                .member(member)
                .build();

        Member savedMember = memberJpaRepository.save(member);

        Post savedPost1 = postJpaRepository.save(post1);
        Post savedPost2 = postJpaRepository.save(post2);

        postJpaRepository.delete(savedPost1);
        savedMember.getPosts().remove(savedPost1);

        em.flush();
        em.clear();

        List<Post> posts = postJpaRepository.findAll();
        Optional<Post> findPost = postJpaRepository.findById(savedPost1.getId());

        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.size()).isEqualTo(savedMember.getPosts().size());
        assertThat(findPost).isNotPresent();
    }

    @Test
    @DisplayName("게시글 제목의 일부만으로 DB에서 조회한 뒤 맞는 데이터 확인")
    public void searchPostByTitle_Keyword(){
        Member member = Member.builder()
                .username("username")
                .password("1234")
                .email("email")
                .nickname("nickname")
                .build();

        Post post1 = Post.builder()
                .title("test")
                .content("content")
                .member(member)
                .build();

        Post post2 = Post.builder()
                .title("title")
                .content("content2")
                .member(member)
                .build();

        memberJpaRepository.save(member);
        Post savedPost = postJpaRepository.save(post1);
        postJpaRepository.save(post2);

        em.flush();
        em.clear();

        List<Post> posts = postJpaRepository.findByTitleContaining("t");
        List<Post> posts2 = postJpaRepository.findByTitleContaining("tes");

        assertThat(posts.size()).isEqualTo(2);
        assertThat(posts2.size()).isEqualTo(1);
        assertThat(posts2.stream().map(Post::getId)).contains(savedPost.getId());
    }

    @Test
    @DisplayName("게시글 작성자의 아이디로 게시글 검색")
    public void searchPostByMemberUsername_keyword(){

        Member member1 = Member.builder()
                .username("abcg")
                .password("1234")
                .email("email")
                .nickname("nickname")
                .build();
        Member member2 = Member.builder()
                .username("defg")
                .password("1234")
                .email("email")
                .nickname("nickname")
                .build();

        Post post1 = Post.builder()
                .title("test")
                .content("content")
                .member(member1)
                .build();

        Post post2 = Post.builder()
                .title("title")
                .content("content2")
                .member(member1)
                .build();

        Post post3 = Post.builder()
                .title("title")
                .content("content2")
                .member(member2)
                .build();

        Member savedMember = memberJpaRepository.save(member1);
        Member savedMember2 = memberJpaRepository.save(member2);

        postJpaRepository.save(post1);
        postJpaRepository.save(post2);
        postJpaRepository.save(post3);

        em.flush();
        em.clear();

        List<Post> posts1 = postJpaRepository.findByMember_UsernameContaining("abc");
        List<Post> posts2 = postJpaRepository.findByMember_UsernameContaining("def");

        assertThat(posts1.size()).isEqualTo(2);
        assertThat(posts2.size()).isEqualTo(1);
        assertThat(posts1).allMatch(p -> p.getMember().getId().equals(savedMember.getId()));
        assertThat(posts2).allMatch(p -> p.getMember().getId().equals(savedMember2.getId()));
    }
}