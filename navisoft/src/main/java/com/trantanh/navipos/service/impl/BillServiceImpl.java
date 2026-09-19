package com.trantanh.navipos.service.impl;

import com.trantanh.eet.impl.EETClient;
import com.trantanh.eet.table.Eet;
import com.trantanh.navipos.config.ConfigManager;
import com.trantanh.navipos.dao.BillDao;
import com.trantanh.navipos.dao.impl.BillDaoImpl;
import com.trantanh.navipos.dto.BillDTO;
import com.trantanh.navipos.model.Bill;
import com.trantanh.navipos.model.BillModel;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.BillService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;
import java.util.List;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class BillServiceImpl implements BillService {
    private static BillServiceImpl instance = null;
    private ConfigManager configManager = new ConfigManager();
    private BillDao billDao = new BillDaoImpl();
    ;
    private EETClient eetClient;

    public static BillServiceImpl getInstance() {
        if (instance == null) {
            BillServiceImpl.instance = new BillServiceImpl();
        }
        return BillServiceImpl.instance;
    }

    @Override
    public void addBill(int billId, int productId, String quantity, String price) {
        billDao.addBill(billId, productId, quantity, price);
    }

    @Override
    public int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP, String PKP, int porad_cis, String dan1, String zakl_dan1, String dan2, String zakl_dan2) {
        return billDao.createBill(number_bill, date, returnMoney, totalPrice, person, acceptmoney, FIK, BKP, PKP, porad_cis, dan1, zakl_dan1, dan2, zakl_dan2);
    }

    @Override
    public int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP, String PKP, int porad_cis, String dan1, String zakl_dan1, String dan2, String zakl_dan2, boolean payByCard) {
        return billDao.createBill(number_bill, date, returnMoney, totalPrice, person, acceptmoney, FIK, BKP, PKP, porad_cis, dan1, zakl_dan1, dan2, zakl_dan2, payByCard);
    }

    @Override
    public int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP) {
        return billDao.createBill(number_bill, date, returnMoney, totalPrice, person, acceptmoney, FIK, BKP);
    }

    @Override
    public int getId(String number_bill) {
        return billDao.getId(number_bill);
    }

    @Override
    public String getReturnPrice(int id) {
        return billDao.getReturnPrice(id);
    }

    @Override
    public String getTotalPrice(int id) {
        return billDao.getTotalPrice(id);
    }

    @Override
    public void deleteProductsBills(String number) {
        billDao.deleteProductsBills(number);
    }

    @Override
    public void deleteBill(int id) {
        billDao.deleteBill(id);
    }

    @Override
    public ObservableList<Bill> getBillList() {
        ObservableList<Bill> bills = FXCollections.observableArrayList();
        bills.addAll(billDao.getBillList());
        return bills;
    }

    @Override
    public ObservableList<Bill> getCurrentBills() {
        ObservableList<Bill> bills = FXCollections.observableArrayList();
        bills.addAll(billDao.getCurrentBillList());
        return bills;
    }

    @Override
    public ObservableList<Eet> getCurrentBillOfflineList() {
        ObservableList<Eet> eets = FXCollections.observableArrayList();
        eets.addAll(getCurrentBillOfflineList());
        return eets;
    }

    @Override
    public ObservableList<Product> getBillProductList(int id) {
        ObservableList<Product> bills = FXCollections.observableArrayList();
        bills.addAll(billDao.getBillProductList(id));
        return bills;
    }

    @Override
    public Bill getBill(String id) {
        return billDao.getBill(id);
    }

    @Override
    public String getDate(int id) {
        return billDao.getDate(id);
    }

    @Override
    public List<Integer> listId() {
        return billDao.listId();
    }

    @Override
    public void deleteBills(int id) {
        billDao.deleteBills(id);
    }

    @Override
    public void updateFik(int id, String fik) {

    }


    @Override
    public void sendOfflineData() {
        List<BillModel> billModels = billDao.getOfflineBill();
        for (BillModel billModel : billModels) {
            Date date = new Date(billModel.getCreated().getTime());
            if (configManager.getTax().equals("1")) {
                eetClient = new EETClient(Double.valueOf(billModel.getTotalPrice()), date, billModel.getPorad_cis(), billModel.getZakl_dan1(), billModel.getDan1(), billModel.getZakl_dan2(), billModel.getDan2());
            } else {
                eetClient = new EETClient(Double.valueOf(billModel.getTotalPrice()), billModel.getPorad_cis(), date);
            }
            eetClient.data();
            String fik = eetClient.getFik();
            updateFik(fik, billModel.getNumberBill());
        }
    }

    public void updateFik(String fik, String billNumber) {
        if (!fik.isEmpty() && fik != null) {
            billDao.updateFik(billNumber, fik);
        }
    }

    @Override
    public List<BillModel> getOfflineBillToday() {
        return null;
    }

    @Override
    public int getPoradCisel() {
        return billDao.getPoradCisel();
    }


    @Override
    public void returnBill(ObservableList<Product> data) {
        //TODO
    }

    @Override
    public List<BillModel> getOfflineBill() {
        return billDao.getOfflineBill();
    }

    @Override
    public void saveBill(BillDTO billDTO) {

    }

}
