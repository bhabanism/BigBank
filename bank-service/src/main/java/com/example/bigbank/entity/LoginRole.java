package com.example.bigbank.entity;

import java.time.OffsetDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
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

    @Column(name = "assigned_at", insertable = false, updatable = false)
    private OffsetDateTime assignedAt;

    public LoginRole(LoginInfo loginInfo, Role role) {
        this.loginInfo = Objects.requireNonNull(loginInfo, "loginInfo");
        this.role = Objects.requireNonNull(role, "role");
        Long loginId = loginInfo.getId();
        Long roleId = role.getId();
        if (loginId != null && roleId != null) {
            this.id = new LoginRoleId(loginId, roleId);
        }
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
        if (id != null && loginInfo != null) {
            id.setLoginId(loginInfo.getId());
        } else if (loginInfo != null && role != null) {
            this.id = new LoginRoleId(loginInfo.getId(), role.getId());
        }
    }

    public void setRole(Role role) {
        this.role = role;
        if (id != null && role != null) {
            id.setRoleId(role.getId());
        } else if (loginInfo != null && role != null) {
            this.id = new LoginRoleId(loginInfo.getId(), role.getId());
        }
    }
}