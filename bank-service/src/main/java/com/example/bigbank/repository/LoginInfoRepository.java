package com.example.bigbank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bigbank.entity.LoginInfo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {

    Optional<LoginInfo> findByUsername(String username);

    @Query("""
    SELECT DISTINCT l FROM LoginInfo l
    LEFT JOIN FETCH l.loginRoles lr
    LEFT JOIN FETCH lr.role
    WHERE l.username = :username
    """)
    Optional<LoginInfo> findByUsernameWithRoles(@Param("username") String username);

    boolean existsByUsernameIgnoreCase(String username);
}