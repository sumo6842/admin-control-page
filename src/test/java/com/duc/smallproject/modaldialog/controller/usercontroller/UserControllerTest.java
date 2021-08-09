package com.duc.smallproject.modaldialog.controller.usercontroller;

import com.duc.smallproject.modaldialog.model.Role;
import com.duc.smallproject.modaldialog.model.User;
import com.duc.smallproject.modaldialog.repo.RoleRepository;
import com.duc.smallproject.modaldialog.repo.UserRepository;
import com.duc.smallproject.modaldialog.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService service;

    @MockBean
    private RoleRepository repo;
    @MockBean
    private UserRepository userRepo;

    @Test
    public void testExportExcel() throws Exception {
        var roles = new ArrayList<Role>();
        roles.add(new Role("Admin", "admin"));
        var value = Arrays.asList(
                new User(1L, "Duc@gmail.com", true,"Duc", "Tran", (List)roles),
                new User(2L, "Test@gmail.com", true,"Test", "John",(List) roles)
        );
        Mockito.when(service.listAll()).thenReturn(value);
        String url = "/users/exporter/excel";
        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
        var contentAsByteArray = mvcResult.getResponse().getContentAsByteArray();
        Path path = Paths.get("test.xlsx");
        Files.write(path, contentAsByteArray);

    }

}