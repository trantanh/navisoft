package com.trantanh.navipos.dao.impl;

import com.trantanh.navipos.dao.UserDao;
import com.trantanh.navipos.manager.DatabaseManager;
import com.trantanh.navipos.model.User;

import java.util.List;

/**
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class UserDaoImpl implements com.trantanh.navipos.dao.UserDao {

    private DatabaseManager<User> databaseManager;
    private final static String ROLE = "uživatel";

    public UserDaoImpl() {
        databaseManager = new DatabaseManager<>();
    }

    @Override
    public String getRole(String user) {
        databaseManager.setup();
        List<User> userList = databaseManager.findAll(User.class);
        databaseManager.exit();
        for (User userModel : userList) {
            if (userModel.getLogin().equals(user)) {
                return userModel.getRole();
            }
        }
        return ROLE;
    }

    @Override
    public boolean findUser(String login) {
        databaseManager.setup();
        List<User> userList = databaseManager.findAll(User.class);
        databaseManager.exit();
        for (User user : userList) {
            if (user.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<User> findAll() {
        databaseManager.setup();
        List<User> userList = databaseManager.findAll(User.class);
        databaseManager.exit();
        return userList;
    }

    @Override
    public void add(User user) {
        databaseManager.setup();
        databaseManager.saveOrUpdate(user);
        databaseManager.exit();
    }

    @Override
    public void delete(String login) {
        databaseManager.setup();
        List<User> userList = databaseManager.findAll(User.class);
        for(User user:userList){
            if(login.equals(user.getLogin())){
                databaseManager.delete(user);
            }
        }
        databaseManager.exit();
    }
}
