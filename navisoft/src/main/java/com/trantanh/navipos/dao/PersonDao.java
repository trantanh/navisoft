package com.trantanh.navipos.dao;

import com.trantanh.navipos.model.Person;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 * 20.02.2021
 */
public interface PersonDao {
    Person getPerson();
    void saveOrUpdate(Person person);
}
