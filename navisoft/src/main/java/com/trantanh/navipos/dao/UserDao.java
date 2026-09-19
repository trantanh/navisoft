package com.trantanh.navipos.dao;

import com.trantanh.navipos.model.User;

import java.util.List;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 * 20.02.2021
 */
public interface UserDao {
    List<User> findAll();
    void add(User user);
    void delete(String login);
    String getRole(String user);
    boolean findUser(String login);
}
