package com.decagon.queuepay.repositories;

import com.decagon.queuepay.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  Boolean existsByUsername(String username);
  User findByEmailVerificationToken(String token);
}
