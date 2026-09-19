package com.trantanh.navipos.dao;

import com.trantanh.navipos.model.Data;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 * 09.07.2020
 */
public interface DataDao {
    Data getData();
    void addData(String path_file, String path_mysql);
}
