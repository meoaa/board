package project.board.controller.view;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.board.post.dto.PostListResponseDto;
import project.board.post.service.PostService;

import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String home(Model model){
        List<PostListResponseDto> posts = postService.findAllPost();
        model.addAttribute("posts",posts);

        return "index";
    }
}
