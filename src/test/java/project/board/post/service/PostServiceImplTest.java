package project.board.post.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import project.board.auth.dto.SignUpRequestDto;
import project.board.auth.dto.SignUpResponseDto;
import project.board.member.domain.Member;
import project.board.member.service.MemberService;
import project.board.post.dto.PostCreateRequestDto;
import project.board.post.dto.PostResponseDto;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class PostServiceImplTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PostService postService;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("게시글 작성후 게시글 확인")
    public void 게시글_작성후_게시글_내용_일치_확인(){
        SignUpRequestDto memberDto =
                new SignUpRequestDto(
                        "username",
                        "1234",
                        "email",
                        "nickname");
        SignUpResponseDto memberResDto =
                memberService.addMember(memberDto);
        Long memberId = memberResDto.getId();

        PostCreateRequestDto postReqDto = new PostCreateRequestDto("title", "content");
        long postId = postService.createPost(postReqDto, memberId);

        em.flush();
        em.clear();

        PostResponseDto postById = postService.findPostById(postId);

        Assertions.assertThat(postById.getTitle()).isEqualTo(postReqDto.getTitle());
        Assertions.assertThat(postById.getContent()).isEqualTo(postReqDto.getContent());

    }

    @Test
    @DisplayName("게시글 작성후 유저 확인")
    public void 게시글_작성후_유저_일치_확인(){
        SignUpRequestDto memberDto =
                new SignUpRequestDto(
                        "username",
                        "1234",
                        "email",
                        "nickname");
        SignUpResponseDto memberResDto =
                memberService.addMember(memberDto);
        Long memberId = memberResDto.getId();

        PostCreateRequestDto postReqDto = new PostCreateRequestDto("title", "content");
        long postId = postService.createPost(postReqDto, memberId);

        em.flush();
        em.clear();

        PostResponseDto postById = postService.findPostById(postId);

        Assertions.assertThat(postById.getMember().getId()).isEqualTo(memberId);
        Assertions.assertThat(postById.getMember().getNickname()).isEqualTo(memberResDto.getNickname());
    }


}