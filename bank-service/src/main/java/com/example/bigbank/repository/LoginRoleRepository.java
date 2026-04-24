package com.example.bigbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bigbank.entity.LoginRole;
import com.example.bigbank.entity.LoginRoleId;

public interface LoginRoleRepository extends JpaRepository<LoginRole, LoginRoleId> {
}