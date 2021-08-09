package com.duc.smallproject.modaldialog.controller;

import com.duc.smallproject.modaldialog.repo.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {
    @GetMapping("/")
    public String indexPage() {
        return "/index";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }
}
