package project.board.auth.controller.view;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@AllArgsConstructor
public class AuthViewController {

    @GetMapping("/login")
    public String loginPage(){
        return "/auth/login";
    }
}
