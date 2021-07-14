package com.duc.smallproject.modaldialog.security;

import com.duc.smallproject.modaldialog.model.User;
import com.duc.smallproject.modaldialog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class WebUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository repo;

    public WebUserDetailService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByEmail = repo.getUserByEmail(username);
        if (userByEmail != null) {
            return new WebUserDetail(userByEmail);
        }
        throw new UsernameNotFoundException("Could not find user with email");

    }
}
