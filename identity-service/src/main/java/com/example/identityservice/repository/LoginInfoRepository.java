package com.example.identityservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.identityservice.entity.LoginInfo;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {

    @Query("""
            SELECT DISTINCT l FROM LoginInfo l
            LEFT JOIN FETCH l.loginRoles lr
            LEFT JOIN FETCH lr.role
            WHERE lower(l.username) = lower(:username)
            """)
    Optional<LoginInfo> findByUsernameWithRoles(@Param("username") String username);

    boolean existsByUsernameIgnoreCase(String username);
}
