package project.board.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.board.common.exception.PostAccessDeniedException;
import project.board.common.exception.MemberNotFoundException;
import project.board.common.exception.PostNotFoundException;
import project.board.member.domain.Member;
import project.board.member.repository.MemberJpaRepository;
import project.board.post.domain.Post;
import project.board.post.dto.PostCreateRequestDto;
import project.board.post.dto.PostResponseDto;
import project.board.post.dto.PostUpdateRequestDto;

import project.board.post.repository.PostRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberJpaRepository memberRepository;

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


    public PostResponseDto findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return PostResponseDto.from(post);
    }

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

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if(!post.getMember().getId().equals(memberId)){
            throw new PostAccessDeniedException();
        }
        postRepository.delete(post);
    }

    public Page<Post> getPostList(Pageable pageable){
        return postRepository.findAll(pageable);
    }

}
