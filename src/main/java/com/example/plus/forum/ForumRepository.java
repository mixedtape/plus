package com.example.plus.forum;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ForumRepository extends JpaRepository<Forum, Long> {

    List<Forum> findByOrderByCreateDateDesc();
    Optional<Forum> findById(Long id);
}
