package com.example.identityservice.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = { "loginId", "roleId" })
@Embeddable
public class LoginRoleId implements Serializable {

    @Column(name = "loginid", nullable = false)
    private Long loginId;

    @Column(name = "roleid", nullable = false)
    private Long roleId;

    public LoginRoleId(Long loginId, Long roleId) {
        this.loginId = loginId;
        this.roleId = roleId;
    }
}
