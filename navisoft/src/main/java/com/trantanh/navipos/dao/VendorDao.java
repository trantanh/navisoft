package com.trantanh.navipos.dao;

import com.trantanh.navipos.model.Vendor;
import javafx.collections.ObservableList;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public interface VendorDao {
    ObservableList<Vendor> getVendorsList();
    void add(Vendor vendor);
    void delete(String name);
    void updateName(String name, String newName);
    void updateTelephone(String telephone, String newTelephone);
}
