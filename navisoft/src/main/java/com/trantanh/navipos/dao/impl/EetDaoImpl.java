package com.trantanh.navipos.dao.impl;

import com.trantanh.navipos.dao.EetDao;
import com.trantanh.navipos.manager.DatabaseManager;
import com.trantanh.navipos.model.EetConfigModel;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class EetDaoImpl implements EetDao {

    private DatabaseManager<EetConfigModel> databaseManager;

    public EetDaoImpl() {
        databaseManager = new DatabaseManager<>();
    }

    @Override
    public EetConfigModel getEet() {
        databaseManager.setup();
        EetConfigModel eetConfigModel = databaseManager.findAll(EetConfigModel.class).get(0);
        databaseManager.exit();
        return eetConfigModel;
    }

    @Override
    public void saveOrUpdate(EetConfigModel eetConfigModel) {
        databaseManager.setup();
        databaseManager.saveOrUpdate(eetConfigModel);
        databaseManager.exit();
    }

}
