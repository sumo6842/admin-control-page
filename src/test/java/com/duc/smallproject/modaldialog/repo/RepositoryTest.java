package com.duc.smallproject.modaldialog.repo;

import com.duc.smallproject.modaldialog.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class RepositoryTest {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Autowired
    RepositoryTest(RoleRepository roleRepository,
                   UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Test
    public void getUserByRepo() {

        User userByEmail = this.userRepository.getUserByEmail("sumo6842@gmail.com");
        System.out.println(userByEmail);
        assertThat(userByEmail != null).isTrue();

    }


    @Test
    public void testFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<User> usersInPage = userRepository.findAll(page);
        List<User> list = usersInPage.getContent();
        list.forEach(System.out::println);
        assertThat(list.size()).isEqualTo(pageSize);
    }



}