package com.example.identityservice.entity;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "login_roles")
public class LoginRole {

    @EmbeddedId
    private LoginRoleId id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("loginId")
    @JoinColumn(name = "loginid", nullable = false)
    @Setter(AccessLevel.NONE)
    private LoginInfo loginInfo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roleId")
    @JoinColumn(name = "roleid", nullable = false)
    @Setter(AccessLevel.NONE)
    private Role role;

    public LoginRole(LoginInfo loginInfo, Role role) {
        this.loginInfo = Objects.requireNonNull(loginInfo, "loginInfo");
        this.role = Objects.requireNonNull(role, "role");
        this.id = new LoginRoleId(loginInfo.getId(), role.getId());
    }
}
