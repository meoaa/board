package project.board.post.repository;

import project.board.post.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findAllPosts();
}
