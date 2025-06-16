package project.board.post.controller.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.board.post.dto.PostListResponseDto;
import project.board.post.service.PostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostViewController {

    private final PostService postService;

    @GetMapping
    public String showAllPostList(Model model){
        List<PostListResponseDto> posts =
                postService.findAllPost();
        model.addAttribute("posts", posts);

        return "/posts/list";
    }

    @GetMapping("/create")
    public String createPostPage(){

        return "/posts/write";
    }
}
