package com.trantanh.navipos.dao;

import com.trantanh.navipos.dao.impl.UserDaoImpl;
import com.trantanh.navipos.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 * 20.02.2021
 */
public class UserDaoImplTest {

    private com.trantanh.navipos.dao.UserDao userDao;

    @BeforeEach
    public void setUp()  {
        userDao = new UserDaoImpl();
    }

    @Test
    public void addAndDeleteUser(){
        User user = new User();
        user.setLogin("test");
        user.setPassword("test");
        user.setRole("admin");
        userDao.add(user);
        for(User user1: userDao.findAll()){
            if(user1.getLogin().equals(user.getLogin())){
                assertEquals(user.getLogin(), user1.getLogin());
                userDao.delete(user.getLogin());
            }
        }
    }

    @Test
    public void findUser(){
       boolean isUser = userDao.findUser("admin");
       assertEquals(true, isUser);
    }

    @Test
    public void getRole(){
        String admin = userDao.getRole("admin");
        assertEquals("admin", admin);
    }


}
