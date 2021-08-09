package com.duc.smallproject.modaldialog.repo;

import com.duc.smallproject.modaldialog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query("SELECT u FROM User AS u WHERE u.email = :email")
    public User getUserByEmail(String email);

    public Long countById(Long id);

    @Query("UPDATE User AS u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    public void updateEnabledUser(Long id, boolean status);

    @Query("SELECT u FROM User AS u WHERE CONCAT(u.id, ' ', u.email, ' '," +
            " u.firstName, ' ', u.lastName)" +
            "LIKE %?1%")
    public Page<User> findAll(Pageable pageable, String keyword);

}
