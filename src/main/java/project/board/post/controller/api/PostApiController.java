package project.board.post.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.board.post.service.PostService;

@RestController
@Slf4j
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostApiController {

    private final PostService postService;


}
