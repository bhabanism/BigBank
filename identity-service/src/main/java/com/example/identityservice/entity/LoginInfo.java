package com.example.identityservice.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "logininfo")
public class LoginInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loginid")
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "customerid", unique = true, nullable = false)
    private Customer customer;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "passwordhash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "lastlogin")
    private OffsetDateTime lastLogin;

    @Column(name = "failedattempts")
    private Integer failedAttempts = 0;

    @Column(name = "islocked")
    private Boolean isLocked = false;

    @JsonIgnore
    @OneToMany(mappedBy = "loginInfo", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    private List<LoginRole> loginRoles = new ArrayList<>();

    public static LoginInfo newForCustomer(Customer customer, String username, String passwordHash) {
        LoginInfo login = new LoginInfo();
        login.setCustomer(customer);
        login.setUsername(username);
        login.setPasswordHash(passwordHash);
        return login;
    }
}
