package com.trantanh.navipos.config;

import com.trantanh.navipos.dao.ConfigDao;
import com.trantanh.navipos.dao.impl.ConfigDaoImpl;
import com.trantanh.navipos.model.Config;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class ConfigManager {

    private ConfigDao configDao;

    public ConfigManager() {
        configDao = new ConfigDaoImpl();
    }

    public void saveLogin(String login) {
        Config config = configDao.getConfig();
        config.setLogin(login);
        configDao.saveOrUpdate(config);
    }

    public void setPrinter(String value) {
        Config config = configDao.getConfig();
        config.setPrinter(value);
        configDao.saveOrUpdate(config);
    }

    public String getPrinter() {
        return configDao.getConfig().getPrinter();
    }

    public void setEet(String eet) {
        Config config = configDao.getConfig();
        config.setEet(eet);
        configDao.saveOrUpdate(config);
    }

    public String getEet() {
        return configDao.getConfig().getEet();
    }

    public void setSpaceEet(String number) {
        Config config = configDao.getConfig();
        config.setSpaceeet(number);
        configDao.saveOrUpdate(config);
    }

    public String getSpaceEet() {
        return configDao.getConfig().getSpaceeet();
    }

    public String getTax() {
        return configDao.getConfig().getTax();
    }

    public void setTax(String tax) {
        Config config = configDao.getConfig();
        config.setTax(tax);
        configDao.saveOrUpdate(config);
    }

    public void setLanguage(String value) {
        Config config = configDao.getConfig();
        config.setLanguage(value);
        configDao.saveOrUpdate(config);
    }

    public String getLanguage() {
        return configDao.getConfig().getLanguage();
    }

    public void savePassword(String login, String password, String checkbox) {
        Config config = configDao.getConfig();
        config.setLogin(login);
        config.setPassword(password);
        config.setCheckbox(checkbox);
        configDao.saveOrUpdate(config);
    }

    public String getCheckBox(){
        return configDao.getConfig().getCheckbox();
    }

    public String getPassword(){
        return configDao.getConfig().getPassword();
    }

    public String getLogin(){
        return configDao.getConfig().getLogin();
    }

    public String readProperty() {
        return configDao.getConfig().getLogin();
    }
}
