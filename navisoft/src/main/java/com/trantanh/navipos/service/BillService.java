package com.trantanh.navipos.service;

import com.trantanh.eet.table.Eet;
import com.trantanh.navipos.dto.BillDTO;
import com.trantanh.navipos.model.Bill;
import com.trantanh.navipos.model.BillModel;
import com.trantanh.navipos.model.Product;
import javafx.collections.ObservableList;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public interface BillService {

    void addBill(int bill_id, int product_id, String quantity, String price);

    int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP, String PKP, int porad_cis, String dan1, String zakl_dan1, String dan2, String zakl_dan2);
    int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP, String PKP, int porad_cis, String dan1, String zakl_dan1, String dan2, String zakl_dan2, boolean payByCard);

    int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP);

    int getId(String number_bill);

    String getReturnPrice(int id);
    String getTotalPrice(int id);

    void deleteProductsBills(String number);

    void deleteBill(int id);

    ObservableList<Bill> getBillList();

    ObservableList<Bill> getCurrentBills();

    ObservableList<Eet> getCurrentBillOfflineList();

    ObservableList<Product> getBillProductList(int id);

    Bill getBill(String id);

    String getDate(int id);

    List<Integer> listId();

    void deleteBills(int id);

    void updateFik(int id, String fik);

    void updateFik(String numberOfBill, String fik);

    List<BillModel> getOfflineBillToday();

    int getPoradCisel();

    
    void sendOfflineData();
    
    void returnBill(ObservableList<Product> data);

    List<BillModel> getOfflineBill();

    void saveBill(BillDTO billDTO);
}