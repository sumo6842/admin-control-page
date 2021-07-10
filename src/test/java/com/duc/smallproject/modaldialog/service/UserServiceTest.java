package com.duc.smallproject.modaldialog.service;

import com.duc.smallproject.modaldialog.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
    private final UserService service;
    @Autowired
    public UserServiceTest(UserService service) {
        this.service = service;
    }

    @Test
    void listALlPage() {
        Page<User> users =
                this.service.listByPage(1, "id", "asc");
        users.getContent().forEach(System.out::println);
    }

}