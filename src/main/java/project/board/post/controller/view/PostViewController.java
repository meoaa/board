package project.board.post.controller.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.board.post.service.PostService;



@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostViewController {

    private final PostService postService;


    @GetMapping("/create")
    public String createPostPage(){

        return "/posts/write";
    }
}
