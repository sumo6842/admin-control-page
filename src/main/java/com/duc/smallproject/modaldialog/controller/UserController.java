package com.duc.smallproject.modaldialog.controller;

import com.duc.smallproject.modaldialog.model.Role;
import com.duc.smallproject.modaldialog.model.User;
import com.duc.smallproject.modaldialog.service.UserService;
import com.duc.smallproject.modaldialog.user.UserNotFoundException;
import com.duc.smallproject.modaldialog.util.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public String listAllUserAction(Model model) {
        List<User> listUsers = service.listAll();
        model.addAttribute("users", listUsers);
        return  getFirstPage(1, model, "id", "asc");
    }

    @GetMapping("/users/page/{pageNumber}")
    public String getFirstPage(@PathVariable(name = "pageNumber") int pageNumber,
                               Model model,
                               @Param("sortField") String sortField,
                               @Param("sortDir") String sortDir) {
        Page<User> page = service.listByPage(pageNumber, sortField, sortDir);
        List<User> listUsers = page.getContent();

        String reverse = sortDir.equals("asc") ? "des" : "asc";

        long startCount = (long) (pageNumber - 1) * UserService.User_Per_Page  + 1;
        long endCount = startCount + UserService.User_Per_Page - 1;
        if (endCount > page.getTotalPages()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("users", listUsers);

        model.addAttribute("reverse",reverse);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "manager";
    }

    @GetMapping("/users/new")
    public String newUserPage(Model model) {
        List<Role> roles = service.listRole();

        User user = new User();
        //th:Object
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }


    @PostMapping("users/save")
    public String saveUserAction(User user,
                                 RedirectAttributes attributes,
                                 @RequestParam("image") MultipartFile image) {
        if (!image.isEmpty()) {
            String photo = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            user.setPhoto(photo);
            User save = service.save(user);
            String uploadDir = "photo-user/" + save.getId();

            try {
                FileUploadUtils.clearDir(uploadDir);
                FileUploadUtils.saveFile(uploadDir, photo, image);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        } else {
            if (user.getPhoto().isEmpty()) user.setPhoto(null);
            service.save(user);
        }
        System.out.println(user);
        attributes.addFlashAttribute("message",
                "The user has been saved successfully😀");
        return getRedirectURLtoAffectedUser(user);
    }

    private String getRedirectURLtoAffectedUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=as&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUserAction(@PathVariable(name = "id") Long id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = service.get(id);
            List<Role> listRoles = service.listRole();
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (Id: " + id + ")");
            model.addAttribute("roles", listRoles);
            return "user_form";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUserAction(@PathVariable("id") Long id,
                                   RedirectAttributes attributes) {
        try {
            service.delete(id);
            attributes.addFlashAttribute("message",
                    "The user Id: " + id + " has been delete successfully");
        } catch (UserNotFoundException ex) {
            attributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatusAction(@PathVariable("id") Long id,
                                                @PathVariable("status") boolean status,
                                                RedirectAttributes attributes) {
        service.updateUserEnabledStatus(id, status);
        String enable = status ? "enabled" : "disabled";
        String message = "The user Id " + id + " has been " + enable;
        attributes.addFlashAttribute("message", message);
        return "redirect:/users";
    }

}
