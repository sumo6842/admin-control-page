package com.duc.smallproject.modaldialog.service;

import com.duc.smallproject.modaldialog.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository repository;
    @Autowired
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }


}
