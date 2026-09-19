package com.trantanh.navipos.repository;

import com.trantanh.navipos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByLogin(String login);

    boolean existsByLogin(String login);

    @Transactional
    void deleteAllByLogin(String login);
}
