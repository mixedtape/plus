package com.example.plus.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByNickname(String nickname);
    Optional<User> findByNickname(String nickname);
}
