package com.trantanh.navipos.dao.impl;

import com.trantanh.navipos.dao.PersonDao;
import com.trantanh.navipos.manager.DatabaseManager;
import com.trantanh.navipos.model.Person;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class PersonDaoImpl implements PersonDao {

    private DatabaseManager<Person> databaseManager;

    public PersonDaoImpl() {
        databaseManager = new DatabaseManager<>();
    }

    @Override
    public Person getPerson() {
        databaseManager.setup();
        Person person = databaseManager.findAll(Person.class).get(0);
        databaseManager.exit();
        return person;
    }

    @Override
    public void saveOrUpdate(Person person) {
        databaseManager.setup();
        databaseManager.saveOrUpdate(person);
        databaseManager.exit();
    }
}
