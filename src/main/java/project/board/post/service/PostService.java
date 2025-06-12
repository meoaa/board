package project.board.post.service;

import project.board.post.dto.PostCreateRequestDto;
import project.board.post.dto.PostListResponseDto;
import project.board.post.dto.PostResponseDto;
import project.board.post.dto.PostUpdateRequestDto;

import java.util.List;

public interface PostService {
    long createPost(PostCreateRequestDto dto,Long memberId);
    PostResponseDto findPostById(Long postId);
    List<PostListResponseDto> findAllPost();
    long updatePost(Long postId, Long memberId, PostUpdateRequestDto dto);
    void deletePost(Long postId, Long memberId);

}
