package com.finefinee.nachricht.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class UserEntity {

    @Id
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    private String password;

    @Column(length = 50)
    private String introduce;

    private String profileImageUrl;

    private UserRole role;

}
