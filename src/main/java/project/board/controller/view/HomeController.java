package project.board.controller.view;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.board.post.dto.PostListResponseDto;
import project.board.post.service.PostServiceImpl;


@Controller
@Slf4j
@AllArgsConstructor
public class HomeController {

    private final PostServiceImpl postService;

    @GetMapping("/")
    public String home(){

        return "index";
    }
}
