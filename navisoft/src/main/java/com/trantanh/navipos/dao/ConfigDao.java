package com.trantanh.navipos.dao;

import com.trantanh.navipos.model.Config;

public interface ConfigDao {
    Config getConfig();

    void saveOrUpdate(Config config);
}
