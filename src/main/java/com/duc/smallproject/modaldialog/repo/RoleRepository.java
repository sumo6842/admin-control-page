package com.duc.smallproject.modaldialog.repo;

import com.duc.smallproject.modaldialog.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
