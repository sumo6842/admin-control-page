package com.duc.smallproject.modaldialog.controller;

import com.duc.smallproject.modaldialog.repo.UserRepository;
import com.duc.smallproject.modaldialog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/checkEmail")
    public String checkDupEmail(@Param("email")String email,
                                @Param("id") Long id) {
        return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }

}
