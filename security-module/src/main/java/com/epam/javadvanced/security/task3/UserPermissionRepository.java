package com.epam.javadvanced.security.task3;

import com.epam.javadvanced.security.task2.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    List<UserPermission> findByUser(User user);
    List<UserPermission> findByUserEmail(String email);
}