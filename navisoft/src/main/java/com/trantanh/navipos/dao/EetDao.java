package com.trantanh.navipos.dao;

import com.trantanh.navipos.model.EetConfigModel;

/**
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public interface EetDao {
    EetConfigModel getEet();

    void saveOrUpdate(EetConfigModel eetConfigModel);
}