package project.board.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.board.common.exception.PostAccessDeniedException;
import project.board.common.exception.MemberNotFoundException;
import project.board.common.exception.PostNotFoundException;
import project.board.member.domain.Member;
import project.board.member.repository.MemberJpaRepository;
import project.board.post.domain.Post;
import project.board.post.dto.PostCreateRequestDto;
import project.board.post.dto.PostListResponseDto;
import project.board.post.dto.PostResponseDto;
import project.board.post.dto.PostUpdateRequestDto;
import project.board.post.repository.PostJpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostJpaRepository postRepository;
    private final MemberJpaRepository memberRepository;

    @Override
    @Transactional
    public long createPost(PostCreateRequestDto dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member)
                .build();

        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }

    @Override
    public PostResponseDto findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return PostResponseDto.from(post);
    }

    @Override
    public List<PostListResponseDto> findAllPost() {
        return postRepository.findAll().stream().
                map(PostListResponseDto::from)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    @Transactional
    public long updatePost(
            Long postId,
            Long memberId,
            PostUpdateRequestDto dto) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        if(!post.getMember().getId().equals(memberId)){
            throw new PostAccessDeniedException();
        }

        post.updatePost(dto);

        return post.getId();
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if(!post.getMember().getId().equals(memberId)){
            throw new PostAccessDeniedException();
        }
        postRepository.delete(post);
    }
}
