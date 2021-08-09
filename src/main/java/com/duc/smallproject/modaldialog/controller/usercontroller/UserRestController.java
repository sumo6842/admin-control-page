package com.duc.smallproject.modaldialog.controller.usercontroller;

import com.duc.smallproject.modaldialog.model.User;
import com.duc.smallproject.modaldialog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api")
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

    @GetMapping("/users/getall")
    public List<User> getAll() {
        return this.userService.listAll();
    }

    /**
     * todo Test Swagger:
     * @param id
     * @return
     */

//    private ConcurrentMap<Integer, User> users = new ConcurrentHashMap<>();
//    @GetMapping("/user/{id}")
//    public User getUser(@PathVariable Integer id) {
//        return null;
//    }


}
