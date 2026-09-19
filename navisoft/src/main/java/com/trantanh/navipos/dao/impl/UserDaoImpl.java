package com.trantanh.navipos.dao.impl;

import com.trantanh.navipos.config.SpringContext;
import com.trantanh.navipos.dao.UserDao;
import com.trantanh.navipos.model.User;
import com.trantanh.navipos.repository.UserRepository;

import java.util.List;

/**
 * Legacy DAO facade backed by Spring Data JPA.
 *
 * UI callers can keep the existing UserDao contract while persistence is
 * managed by Spring. The facade can be removed once callers inject the
 * repository or a dedicated user service directly.
 */
public class UserDaoImpl implements UserDao {

    private static final String DEFAULT_ROLE = "uživatel";

    private final UserRepository userRepository;

    public UserDaoImpl() {
        this(SpringContext.getBean(UserRepository.class));
    }

    UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getRole(String login) {
        return userRepository.findFirstByLogin(login)
                .map(User::getRole)
                .orElse(DEFAULT_ROLE);
    }

    @Override
    public boolean findUser(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(String login) {
        userRepository.deleteAllByLogin(login);
    }
}
