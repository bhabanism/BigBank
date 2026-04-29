package com.example.identityservice.security;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.identityservice.entity.LoginInfo;
import com.example.identityservice.repository.LoginInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginInfoUserDetailsService implements UserDetailsService {

    private final LoginInfoRepository loginInfoRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        LoginInfo login = loginInfoRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        var authorities = login.getLoginRoles().stream()
                .map(lr -> lr.getRole().getRoleName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return User.builder()
                .username(login.getUsername())
                .password(login.getPasswordHash())
                .authorities(authorities)
                .accountLocked(Boolean.TRUE.equals(login.getIsLocked()))
                .build();
    }
}
