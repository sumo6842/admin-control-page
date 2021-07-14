package com.duc.smallproject.modaldialog.security;

import com.duc.smallproject.modaldialog.model.Role;
import com.duc.smallproject.modaldialog.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebUserDetail implements UserDetails {
    private User user;

    public WebUserDetail(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = user.getRoles();
        var authories = new ArrayList<SimpleGrantedAuthority>();
        roles.forEach(role -> authories
                        .add(new SimpleGrantedAuthority(
                                role.getDescription())));
        return authories;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public void setFirstName(String firstName) {
        this.user.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.user.setLastName(lastName);
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }
}
