package project.board.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import project.board.comment.repository.CommentJpaRepository;
import project.board.comment.domain.Comment;
import project.board.member.domain.Member;
import project.board.member.repository.MemberJpaRepository;
import project.board.post.domain.Post;
import project.board.post.repository.PostRepository;

import java.util.List;

@ActiveProfiles("test")
@DataJpaTest
class CommentJpaRepositoryTest {

    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private PostRepository postJpaRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void saveComment(){
        Member member = Member.builder()
                .username("username1")
                .password("1234")
                .email("test@test.com")
                .nickname("nickname")
                .build();

        Member savedMember = memberJpaRepository.save(member);

        Post post = Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();

        Post savedPost = postJpaRepository.save(post);

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content("comment content")
                .build();

        Comment savedComment = commentJpaRepository.save(comment);

        em.flush();
        em.clear();

        Assertions.assertThat(savedComment.getContent()).isEqualTo("comment content");
        Assertions.assertThat(savedComment.getPost().getId()).isEqualTo(savedPost.getId());
        Assertions.assertThat(savedComment.getMember().getId()).isEqualTo(savedMember.getId());
    }

    @Test
    public void findCommentById(){

        Member member = Member.builder()
                .username("username1")
                .password("1234")
                .email("test@test.com")
                .nickname("nickname")
                .build();

        Member savedMember = memberJpaRepository.save(member);

        Post post = Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();

        Post savedPost = postJpaRepository.save(post);

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content("comment content")
                .build();

        Comment savedComment = commentJpaRepository.save(comment);

        em.flush();
        em.clear();

        Comment findComment = commentJpaRepository.findById(savedComment.getId()).orElseThrow();

        Assertions.assertThat(findComment.getId()).isEqualTo(savedComment.getId());
        Assertions.assertThat(findComment.getPost().getId()).isEqualTo(savedPost.getId());
        Assertions.assertThat(findComment.getMember().getId()).isEqualTo(savedMember.getId());
    }

    @Test
    public void findAllComment(){
        Member member = Member.builder()
                .username("username1")
                .password("1234")
                .email("test@test.com")
                .nickname("nickname")
                .build();

        memberJpaRepository.save(member);

        Post post = Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();

        postJpaRepository.save(post);

        Comment comment1 = Comment.builder()
                .member(member)
                .post(post)
                .content("comment content")
                .build();

        Comment comment2 = Comment.builder()
                .member(member)
                .post(post)
                .content("comment content2")
                .build();

        commentJpaRepository.save(comment1);
        commentJpaRepository.save(comment2);

        em.flush();
        em.clear();

        List<Comment> allComments = commentJpaRepository.findAll();

        Assertions.assertThat(allComments.size()).isEqualTo(2);
    }

    @Test
    public void removeComment(){
        Member member = Member.builder()
                .username("username1")
                .password("1234")
                .email("test@test.com")
                .nickname("nickname")
                .build();

        memberJpaRepository.save(member);

        Post post = Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .build();

        postJpaRepository.save(post);

        Comment comment1 = Comment.builder()
                .member(member)
                .post(post)
                .content("comment content")
                .build();

        Comment comment2 = Comment.builder()
                .member(member)
                .post(post)
                .content("comment content2")
                .build();

        Comment saveComment1 = commentJpaRepository.save(comment1);
        Comment saveComment2 = commentJpaRepository.save(comment2);

        commentJpaRepository.delete(saveComment1);
        saveComment1.getPost().getComments().remove(saveComment1);
        saveComment1.getMember().getComments().remove(saveComment1);


        em.flush();
        em.clear();

        List<Comment> allComments = commentJpaRepository.findAll();

        Assertions.assertThat(allComments.size()).isEqualTo(1);
        Assertions.assertThat(allComments.stream().map(Comment::getId)).contains(saveComment2.getId());
    }

    @Test
    @DisplayName("게시글 ID로 댓글 전체 조회")
    public void findCommentByPostId() {
        Member member = Member.builder()
                .username("username1")
                .password("1234")
                .email("test@test.com")
                .nickname("nickname")
                .build();

        memberJpaRepository.save(member);

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

        Post savePost1 = postJpaRepository.save(post1);
        Post savePost2 = postJpaRepository.save(post2);

        Comment comment1 = Comment.builder()
                .member(member)
                .post(post1)
                .content("comment content")
                .build();

        Comment comment2 = Comment.builder()
                .member(member)
                .post(post1)
                .content("comment content2")
                .build();

        Comment comment3 = Comment.builder()
                .member(member)
                .post(post2)
                .content("comment content3")
                .build();

        commentJpaRepository.save(comment1);
        commentJpaRepository.save(comment2);
        commentJpaRepository.save(comment3);

        em.flush();
        em.clear();

        List<Comment> post1Comments = commentJpaRepository.findByPost_Id(savePost1.getId());
        List<Comment> post2Comments = commentJpaRepository.findByPost_Id(savePost2.getId());

        Assertions.assertThat(post1Comments.size()).isEqualTo(2);
        Assertions.assertThat(post1Comments).allMatch(post -> post.getPost().getId().equals(savePost1.getId()));

        Assertions.assertThat(post2Comments.size()).isEqualTo(1);
        Assertions.assertThat(post2Comments).allMatch(post -> post.getPost().getId().equals(savePost2.getId()));
    }
}