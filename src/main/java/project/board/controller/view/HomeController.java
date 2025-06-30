package project.board.controller.view;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.board.post.domain.Post;
import project.board.post.service.PostService;


@Controller
@Slf4j
@AllArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String home(Model model,
                       @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                       Pageable pageable
                       ){
        Page<Post> postPage = postService.getPostList(pageable);

        model.addAttribute("postPage", postPage);

        return "index";
    }
}
