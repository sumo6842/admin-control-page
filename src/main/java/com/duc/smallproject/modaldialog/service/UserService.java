package com.duc.smallproject.modaldialog.service;

import com.duc.smallproject.modaldialog.model.Role;
import com.duc.smallproject.modaldialog.model.User;
import com.duc.smallproject.modaldialog.repo.RoleRepository;
import com.duc.smallproject.modaldialog.repo.UserRepository;
import com.duc.smallproject.modaldialog.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    public static int User_Per_Page = 4;
    private final UserRepository repo;
    private final RoleRepository roleRepository;

    private final PasswordEncoder crypto;

    @Autowired
    public UserService(UserRepository repo,
                       RoleRepository service,
                       PasswordEncoder crypto) {
        this.repo = repo;
        this.roleRepository = service;
        this.crypto = crypto;
    }

    public Page<User> listByPage(int  numberPage) {
        Pageable pageable = PageRequest.of(numberPage - 1, User_Per_Page);
        return repo.findAll(pageable);
    }


    public List<User> listAll() {
        return (List<User>) (this.repo.findAll());
    }

    public List<Role> listRole() {
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {
        boolean isUpdating = user.getId() != null;
        if (isUpdating) {
            User updating = repo.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(updating.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }
        return this.repo.save(user);
    }

    private void encodePassword(User user) {
        user.setPassword(this.crypto.encode(user.getPassword()));
    }

    public boolean isEmailUnique(Long id, String email) {
        User userByEmail = repo.getUserByEmail(email);
        if (userByEmail == null) return true;
        boolean isCreating = (id == null);
        return !isCreating && id.equals(userByEmail.getId());
    }

    public User get(Long id) throws UserNotFoundException {
        try {
            return repo.findById(id).get();

        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException("Could not find any user with Id: " + id);
        }
    }

    public void delete(Long id) throws UserNotFoundException {
        Long countById = repo.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Could not find any user with Id: " + id);
        }
        repo.deleteById(id);
    }

    public void updateUserEnabledStatus(Long id, boolean status) {
        repo.updateEnabledUser(id, status);
    }
}
