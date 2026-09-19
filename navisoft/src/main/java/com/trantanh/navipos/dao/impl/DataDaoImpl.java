package com.trantanh.navipos.dao.impl;

import com.trantanh.navipos.dao.DataDao;
import com.trantanh.navipos.manager.DatabaseManager;
import com.trantanh.navipos.model.Data;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class DataDaoImpl implements DataDao {

    private DatabaseManager<Data> dataDatabaseManager;
    public DataDaoImpl() {
        dataDatabaseManager = new DatabaseManager<>();
    }

    @Override
    public Data getData() {
        dataDatabaseManager.setup();
        Data data = dataDatabaseManager.findAll(Data.class).get(0);
        dataDatabaseManager.exit();
        return data;
    }

    @Override
    public void addData(String path_file, String path_mysql) {
        dataDatabaseManager.setup();
        Data data = dataDatabaseManager.findAll(Data.class).get(0);
        data.setPathFile(path_file);
        data.setPathMySQl(path_mysql);
        dataDatabaseManager.saveOrUpdate(data);
        dataDatabaseManager.exit();
    }

}
