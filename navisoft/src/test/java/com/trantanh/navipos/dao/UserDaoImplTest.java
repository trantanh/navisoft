package com.trantanh.navipos.dao;

import com.trantanh.navipos.dao.impl.UserDaoImpl;
import com.trantanh.navipos.CashDeskApp;
import com.trantanh.navipos.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 * 20.02.2021
 */
@SpringBootTest(
        classes = CashDeskApp.class,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:navisoft-test;MODE=MySQL;DB_CLOSE_DELAY=-1",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
public class UserDaoImplTest {

    private com.trantanh.navipos.dao.UserDao userDao;

    @BeforeEach
    public void setUp()  {
        userDao = new UserDaoImpl();
        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword("test");
        admin.setRole("admin");
        userDao.add(admin);
    }

    @AfterEach
    public void tearDown() {
        userDao.delete("test");
        userDao.delete("admin");
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
