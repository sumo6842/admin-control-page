package com.duc.smallproject.modaldialog.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(name = "description", length = 150, nullable = false)
    private String description;

    @Column(name = "name", length = 100)
    private String name;

    public Role() {
    }

    public Role(String description, String name) {
        this.description = description;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
