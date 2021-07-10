package com.duc.smallproject.modaldialog.model;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "first_name", length = 150, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 150, nullable = false)
    private String lastName;
    @Column(name = "password", length = 150, nullable = false)
    private String password;
    @Column(name = "photo", length = 150)
    private String photo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Role> roles;

    @Transient
    public String getPhotosImagePath() {
        if (id == null || photo == null) return "/images/default-user.png";
        return "/photo-user/" + this.id + "/" + this.photo;
    }

}
