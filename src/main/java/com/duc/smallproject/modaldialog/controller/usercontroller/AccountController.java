package com.duc.smallproject.modaldialog.controller.usercontroller;

import com.duc.smallproject.modaldialog.model.User;
import com.duc.smallproject.modaldialog.security.WebUserDetail;
import com.duc.smallproject.modaldialog.service.UserService;
import com.duc.smallproject.modaldialog.util.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
public class AccountController {

    @Autowired
    private UserService service;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal WebUserDetail loggedUser,
                              Model model) {
        var email = loggedUser.getUsername();
        var userByEmail = service.getByEmail(email);
        model.addAttribute("user", userByEmail);
        return "/users/account_form";
    }
    @PostMapping("account/update")
    public String saveDetail(User user,
                                 RedirectAttributes attributes,
                                 @AuthenticationPrincipal WebUserDetail loggedUser,
                                 @RequestParam("image") MultipartFile image) {
        if (!image.isEmpty()) {
            String photo = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            user.setPhoto(photo);
            User save = service.updateAccount(user);
            String uploadDir = "photo-user/" + save.getId();

            try {
                FileUploadUtils.clearDir(uploadDir);
                FileUploadUtils.saveFile(uploadDir, photo, image);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        } else {
            if (user.getPhoto().isEmpty()) user.setPhoto(null);
            service.updateAccount(user);
        }

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());

        System.out.println(user);
        attributes.addFlashAttribute("message",
                "Your account detail updated");
        return "/users/account_form";
    }


}


