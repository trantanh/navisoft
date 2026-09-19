package com.trantanh.navipos.dao;

import com.trantanh.eet.table.Eet;
import com.trantanh.navipos.model.Bill;
import com.trantanh.navipos.model.BillModel;
import com.trantanh.navipos.model.Product;

import java.util.Date;
import java.util.List;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 * DAO layer for {@link Bill}
 */
public interface BillDao {

    void addBill(int bill_id, int product_id, String quantity, String price);

    int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP, String PKP, int porad_cis, String dan1, String zakl_dan1, String dan2, String zakl_dan2);
    int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP, String PKP, int porad_cis, String dan1, String zakl_dan1, String dan2, String zakl_dan2, boolean payCard);

    int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP);
    int getId(String number_bill);

    String getTotalPrice(int id);

    String getReturnPrice(int id);

    void deleteProductsBills(String number);

    void deleteBill(int id);

    List<Bill> getBillList();

    List<Bill> getCurrentBillList();

    String getCurrentBill();

    List<Product> getBillProductList(int id);

    int getPoradCisel();

    Bill getBill(String id);

    String getDate(int id);

    List<Eet> getCurrentBillOfflineList();

    List<Integer> listId();

    void deleteBills(int id);

    void updateFik(int id, String fik);

    void updateFik(String numberOfBill, String fik);

    List<BillModel> getOfflineBill();
}
