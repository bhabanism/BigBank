package com.example.identityservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.identityservice.dto.request.AdminRegisterUserRequest;
import com.example.identityservice.dto.response.AdminRegisterUserResponse;
import com.example.identityservice.entity.Customer;
import com.example.identityservice.entity.LoginInfo;
import com.example.identityservice.entity.LoginRole;
import com.example.identityservice.entity.Role;
import com.example.identityservice.repository.CustomerRepository;
import com.example.identityservice.repository.LoginInfoRepository;
import com.example.identityservice.repository.LoginRoleRepository;
import com.example.identityservice.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserRegistrationService {

    private final CustomerRepository customerRepository;
    private final LoginInfoRepository loginInfoRepository;
    private final LoginRoleRepository loginRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdminRegisterUserResponse register(AdminRegisterUserRequest request) {
        if (customerRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        if (loginInfoRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
        }
        Role customerRole = roleRepository.findByRoleName("ROLE_CUSTOMER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ROLE_CUSTOMER missing"));

        Customer customer = Customer.newCustomer(
                request.getFirstName().trim(),
                request.getLastName().trim(),
                request.getEmail().trim().toLowerCase());
        customerRepository.saveAndFlush(customer);

        LoginInfo login = LoginInfo.newForCustomer(
                customer,
                request.getUsername().trim(),
                passwordEncoder.encode(request.getPassword()));
        loginInfoRepository.saveAndFlush(login);

        LoginRole assignment = new LoginRole(login, customerRole);
        loginRoleRepository.save(assignment);

        return new AdminRegisterUserResponse(customer.getId(), login.getUsername());
    }
}
