package com.trantanh.navipos.dao.impl;

import com.trantanh.navipos.dao.VendorDao;
import com.trantanh.navipos.manager.DatabaseManager;
import com.trantanh.navipos.model.Vendor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class VendorDaoImpl implements VendorDao {

    private DatabaseManager<Vendor> vendorDatabaseManager;

    public VendorDaoImpl() {
        vendorDatabaseManager = new DatabaseManager<>();
    }

    @Override
    public ObservableList<Vendor> getVendorsList() {
        vendorDatabaseManager.setup();
        List<Vendor> vendorList = vendorDatabaseManager.findAll(Vendor.class);
        vendorDatabaseManager.exit();
        ObservableList<Vendor> vendorObservableList = FXCollections.observableArrayList();
        vendorObservableList.addAll(vendorList);
        return vendorObservableList;
    }

    @Override
    public void add(Vendor vendor) {
        vendorDatabaseManager.setup();
        vendorDatabaseManager.saveOrUpdate(vendor);
        vendorDatabaseManager.exit();
    }

    @Override
    public void delete(String name) {
        vendorDatabaseManager.setup();
        List<Vendor> vendorList = vendorDatabaseManager.findAll(Vendor.class);
        for (Vendor vendor : vendorList) {
            if (vendor.getName().equals(name)) {
                vendorDatabaseManager.delete(vendor);
            }
        }
        vendorDatabaseManager.exit();
    }

    @Override
    public void updateName(String name, String newName) {
        vendorDatabaseManager.setup();
        List<Vendor> vendorList = vendorDatabaseManager.findAll(Vendor.class);
        for (Vendor vendor : vendorList) {
            if (vendor.getName().equals(name)) {
                vendor.setName(newName);
                vendorDatabaseManager.saveOrUpdate(vendor);
            }
        }
        vendorDatabaseManager.exit();
    }

    @Override
    public void updateTelephone(String telephone, String newTelephone) {
        vendorDatabaseManager.setup();
        List<Vendor> vendorList = vendorDatabaseManager.findAll(Vendor.class);
        for (Vendor vendor : vendorList) {
            if (vendor.getTelephone().equals(telephone)) {
                vendor.setTelephone(newTelephone);
                vendorDatabaseManager.saveOrUpdate(vendor);
            }
        }
        vendorDatabaseManager.exit();
    }

}
