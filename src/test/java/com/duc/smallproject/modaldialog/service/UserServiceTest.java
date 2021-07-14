package com.duc.smallproject.modaldialog.service;

import com.duc.smallproject.modaldialog.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;

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
                this.service.listByPage(1, "id", "asc", "maria");
        users.getContent().forEach(System.out::println);
    }
    @Test
    void listALl() {
        this.service.listAll().forEach(System.out::println);
    }

    @Test
    public void filterTest() {
        Page<User> users =
                service.listByPage(1, "id", "asc", "maria");
        assertThat(users).isNotNull();
        users.forEach(System.out::println);
    }


}