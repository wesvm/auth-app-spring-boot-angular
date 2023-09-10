package com.api.login.repositories;

import com.api.login.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByEnabledTrue(Pageable pageable);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndEnabledTrue(String email);
    boolean existsUserByEmail(String email);
}
