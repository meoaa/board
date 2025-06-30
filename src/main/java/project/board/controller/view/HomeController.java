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
import org.springframework.web.bind.annotation.RequestParam;
import project.board.post.domain.Post;
import project.board.post.dto.PostListResponseDto;
import project.board.post.service.PostService;


@Controller
@Slf4j
@AllArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String home(@RequestParam(name = "q", required = false) String query,
                       @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                       Pageable pageable,
                       Model model
                       ){

        Page<PostListResponseDto> postPage = postService.getPostList(pageable, query);

        model.addAttribute("postPage", postPage);

        log.info("query = {}", query);
        log.info("size = {}", postPage.getSize());
        log.info("totalPages = {}", postPage.getTotalPages());
        log.info("total elements = {}", postPage.getTotalElements());
        log.info("number of elements ={}", postPage.getNumberOfElements());

        return "index";
    }
}
