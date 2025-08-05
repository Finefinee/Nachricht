package com.finefinee.nachricht.domain.user.repository;

import com.finefinee.nachricht.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsByUsername(String username);
}
