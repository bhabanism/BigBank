package com.example.bigbank.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.bigbank.dto.request.AdminRegisterUserRequest;
import com.example.bigbank.dto.response.AdminRegisterUserResponse;
import com.example.bigbank.entity.Customer;
import com.example.bigbank.entity.LoginInfo;
import com.example.bigbank.entity.LoginRole;
import com.example.bigbank.entity.Role;
import com.example.bigbank.repository.CustomerRepository;
import com.example.bigbank.repository.LoginInfoRepository;
import com.example.bigbank.repository.LoginRoleRepository;
import com.example.bigbank.repository.RoleRepository;

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
