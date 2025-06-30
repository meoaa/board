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
import project.board.common.exception.PostAccessDeniedException;
import project.board.common.exception.PostNotFoundException;
import project.board.member.service.MemberService;
import project.board.post.dto.PostCreateRequestDto;
import project.board.post.dto.PostListResponseDto;
import project.board.post.dto.PostResponseDto;
import project.board.post.dto.PostUpdateRequestDto;

import java.util.List;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class PostServiceTest {

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

    @Test
    @DisplayName("게시글 작성 후 없는 게시글")
    public void 게시글_작성후_없는_게시글_검색(){
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

        org.junit.jupiter.api.Assertions.assertThrows(PostNotFoundException.class, () -> postService.findPostById(99L));
    }

    @Test
    @DisplayName("전체 게시글 조회")
    public void 전체_게시글_조회(){
        SignUpRequestDto memberDto =
                new SignUpRequestDto(
                        "username",
                        "1234",
                        "email",
                        "nickname");
        SignUpResponseDto memberResDto =
                memberService.addMember(memberDto);
        Long memberId = memberResDto.getId();

        PostCreateRequestDto postReqDto1 = new PostCreateRequestDto("title", "content");
        PostCreateRequestDto postReqDto2 = new PostCreateRequestDto("title", "content");

        postService.createPost(postReqDto1, memberId);
        postService.createPost(postReqDto2, memberId);

        em.flush();
        em.clear();

        List<PostListResponseDto> allPost = postService.findAllPost();

        Assertions.assertThat(allPost.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 수정")
    public void 게시글_수정_테스트(){
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

        long savedPostId = postService.createPost(postReqDto, memberId);

        PostUpdateRequestDto postUpdateReqDto = new PostUpdateRequestDto("updated title", "updated content");
        long updatedPostId = postService.updatePost(savedPostId, memberId, postUpdateReqDto);
        em.flush();
        em.clear();

        PostResponseDto updatedPost = postService.findPostById(updatedPostId);

        Assertions.assertThat(updatedPost.getTitle()).isEqualTo(postUpdateReqDto.getTitle());
        Assertions.assertThat(updatedPost.getContent()).isEqualTo(postUpdateReqDto.getContent());
    }

    @Test
    @DisplayName("게시글 수정 작성자 불일치")
    public void 게시글_수정시_작성자_불일치_테스트(){
        SignUpRequestDto memberDto =
                new SignUpRequestDto(
                        "username",
                        "1234",
                        "email",
                        "nickname");
        SignUpResponseDto memberResDto =
                memberService.addMember(memberDto);


        Long memberId = memberResDto.getId();

        SignUpRequestDto memberDto2 =
                new SignUpRequestDto(
                        "username2",
                        "1234",
                        "email22",
                        "nickname");
        SignUpResponseDto memberResDto2 =
                memberService.addMember(memberDto2);

        Long wrongMemberId = memberResDto2.getId();


        PostCreateRequestDto postReqDto = new PostCreateRequestDto("title", "content");
        long postId = postService.createPost(postReqDto, memberId);

        PostUpdateRequestDto postUpdateReqDto = new PostUpdateRequestDto("updated title", "updated content");

        em.flush();
        em.clear();

        org.junit.jupiter.api.Assertions.assertThrows(PostAccessDeniedException.class, ()-> postService.updatePost(postId,wrongMemberId,postUpdateReqDto));
    }

    @Test
    @DisplayName("게시글 삭제")
    public void 게시글_삭제_테스트(){
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
        postService.deletePost(postId, memberId);

        em.flush();
        em.clear();

        List<PostListResponseDto> allPost = postService.findAllPost();

        Assertions.assertThat(allPost.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 삭제시 유저 불일치")
    public void 게시글_삭제시_유저_불일치_테스트(){
        SignUpRequestDto memberDto =
                new SignUpRequestDto(
                        "username",
                        "1234",
                        "email",
                        "nickname");
        SignUpResponseDto memberResDto =
                memberService.addMember(memberDto);


        Long memberId = memberResDto.getId();

        SignUpRequestDto memberDto2 =
                new SignUpRequestDto(
                        "username2",
                        "1234",
                        "email22",
                        "nickname");
        SignUpResponseDto memberResDto2 =
                memberService.addMember(memberDto2);

        Long wrongMemberId = memberResDto2.getId();

        PostCreateRequestDto postReqDto = new PostCreateRequestDto("title", "content");
        long postId = postService.createPost(postReqDto, memberId);

        em.flush();
        em.clear();

        org.junit.jupiter.api.Assertions.assertThrows(PostAccessDeniedException.class,
                ()-> postService.deletePost(postId, wrongMemberId));

    }
}