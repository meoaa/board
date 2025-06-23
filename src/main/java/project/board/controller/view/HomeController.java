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
import project.board.post.service.PostService;

import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String home(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            Model model){

        log.info("page : {}" , page);
        log.info("size : {}", size);
        Pageable pageable = PageRequest.of(page, size);
        Page<PostListResponseDto> postPage = postService.searchPosts(keyword, pageable);


        model.addAttribute("posts", postPage.getContent()); // ✅ 게시글 목록
        model.addAttribute("page", postPage);               // ✅ 페이징 정보
        model.addAttribute("keyword", keyword);             // ✅ 검색어 유지용

        return "index";
    }
}
