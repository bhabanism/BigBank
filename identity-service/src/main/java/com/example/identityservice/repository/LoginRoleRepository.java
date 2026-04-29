package com.example.identityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.identityservice.entity.LoginRole;
import com.example.identityservice.entity.LoginRoleId;

public interface LoginRoleRepository extends JpaRepository<LoginRole, LoginRoleId> {
}
