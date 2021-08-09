package com.duc.smallproject.modaldialog.controller.usercontroller;

import com.duc.smallproject.modaldialog.repo.UserRepository;
import com.duc.smallproject.modaldialog.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserRestController.class)
class UserRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @MockBean
    private UserRepository repo;

    @Test
    public void testCheckEmailUnique() throws Exception {
        Mockito.when(service.isEmailUnique(1L, "sumo6842@gmail.com"))
                .thenReturn(false);
        String url = "/users/checkEmail";
        mockMvc.perform(post(url)
                .param("id", "1")
                .param("email", "sumo6842@gmail.com")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Duplicated"));
    }

}