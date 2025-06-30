package project.board.post.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.board.auth.CustomUserDetails;
import project.board.common.ApiResponse;
import project.board.post.dto.PostCreateRequestDto;
import project.board.post.service.PostServiceImpl;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostApiController {

    private final PostServiceImpl postService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(
            @Valid @RequestBody PostCreateRequestDto requestDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails user){

        log.info("createPost controller");

        if (bindingResult.hasErrors()) {
            // 에러 처리
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        long memberId = user.getId();

        long postId = postService.createPost(requestDto, memberId);
        Map<String, Long> data = Map.of("postId", postId);
        return ResponseEntity
                .ok(ApiResponse.of(
                        200,
                        "게시글 작성이 완료되었습니다.",
                        data));
    }
}
