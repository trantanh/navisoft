package com.trantanh.navipos.dao.impl;

import com.trantanh.navipos.dao.ConfigDao;
import com.trantanh.navipos.manager.DatabaseManager;
import com.trantanh.navipos.model.Config;

public class ConfigDaoImpl implements ConfigDao {

    private DatabaseManager<Config> databaseManager;

    public ConfigDaoImpl() {
        this.databaseManager = new DatabaseManager();
    }

    @Override
    public Config getConfig() {
        databaseManager.setup();
        Config config = databaseManager.findAll(Config.class).get(0);
        databaseManager.exit();
        return config;
    }

    @Override
    public void saveOrUpdate(Config config) {
        databaseManager.setup();
        databaseManager.saveOrUpdate(config);
        databaseManager.exit();
    }
}
