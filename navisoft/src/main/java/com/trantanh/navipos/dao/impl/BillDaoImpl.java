package com.trantanh.navipos.dao.impl;

import com.trantanh.eet.table.Eet;
import com.trantanh.navipos.dao.BillDao;
import com.trantanh.navipos.dao.ProductDao;
import com.trantanh.navipos.dao.SalesDao;
import com.trantanh.navipos.model.DatabaseConnector;
import com.trantanh.navipos.model.Bill;
import com.trantanh.navipos.model.BillModel;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.utils.DateUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class BillDaoImpl extends DatabaseConnector implements BillDao {

    private SalesDao salesDao = new SalesDaoImpl();

    private ProductDao productDao = new ProductDaoImpl();

    private final static DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public BillDaoImpl() {
        super();
    }

    @Override
    public void addBill(int billId, int product_id, String quantity, String price) {
        try {
            String query = "INSERT INTO products_bills (bill_id, product_id, quantity, price) VALUES (?, ?, ?,?) ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, billId);
            preparedStatement.setInt(2, product_id);
            preparedStatement.setString(3, quantity);
            preparedStatement.setString(4, price);
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP, String PKP, int porad_cis, String dan1, String zakl_dan1, String dan2, String zakl_dan2) {
        try {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String query = "INSERT INTO bills(numberbill,returnmoney,totalprice,person,acceptmoney, date, FIK, BKP,PKP,porad_cis, dan1, zakl_dan1, dan2, zakl_dan2) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) connection.prepareStatement(query);
            preparedStatement.setString(1, number_bill);
            preparedStatement.setString(2, returnMoney);
            preparedStatement.setString(3, totalPrice);
            preparedStatement.setString(4, person);
            preparedStatement.setString(5, acceptmoney);
            preparedStatement.setDate(6, sqlDate);
            preparedStatement.setString(7, FIK);
            preparedStatement.setString(8, BKP);
            preparedStatement.setString(9, PKP);
            preparedStatement.setInt(10, porad_cis);
            preparedStatement.setString(11, dan1);
            preparedStatement.setString(12, zakl_dan1);
            preparedStatement.setString(13, dan2);
            preparedStatement.setString(14, zakl_dan2);
            preparedStatement.execute();
            return getId(String.valueOf(number_bill));
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP, String PKP, int porad_cis, String dan1, String zakl_dan1, String dan2, String zakl_dan2, boolean payByCard) {
        try {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String query = "INSERT INTO bills(numberbill,returnmoney,totalprice,person,acceptmoney, date, FIK, BKP,PKP,porad_cis, dan1, zakl_dan1, dan2, zakl_dan2, pay_by_card) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) connection.prepareStatement(query);
            preparedStatement.setString(1, number_bill);
            preparedStatement.setString(2, returnMoney);
            preparedStatement.setString(3, totalPrice);
            preparedStatement.setString(4, person);
            preparedStatement.setString(5, acceptmoney);
            preparedStatement.setDate(6, sqlDate);
            preparedStatement.setString(7, FIK);
            preparedStatement.setString(8, BKP);
            preparedStatement.setString(9, PKP);
            preparedStatement.setInt(10, porad_cis);
            preparedStatement.setString(11, dan1);
            preparedStatement.setString(12, zakl_dan1);
            preparedStatement.setString(13, dan2);
            preparedStatement.setString(14, zakl_dan2);
            preparedStatement.setBoolean(15, payByCard);
            preparedStatement.execute();
            return getId(String.valueOf(number_bill));
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public int createBill(String number_bill, Date date, String returnMoney, String totalPrice, String person, String acceptmoney, String FIK, String BKP) {
        try {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String query = "INSERT INTO bills(numberbill,returnmoney,totalprice,person,acceptmoney,date,FIK, BKP) VALUES(?,?,?,?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) connection.prepareStatement(query);
            preparedStatement.setString(1, number_bill);
            preparedStatement.setString(2, returnMoney);
            preparedStatement.setString(3, totalPrice);
            preparedStatement.setString(4, person);
            preparedStatement.setString(5, acceptmoney);
            preparedStatement.setDate(6, sqlDate);
            preparedStatement.setString(7, FIK);
            preparedStatement.setString(8, BKP);
            preparedStatement.execute();
            return getId(String.valueOf(number_bill));
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public int getId(String number_bill) {
        try {
            int getId = 0;
            String query = "SELECT id FROM bills WHERE numberbill=?";
            preparedStatement = (PreparedStatement) connection.prepareStatement(query);
            preparedStatement.setString(1, number_bill);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                getId = rs.getInt("id");
            }
            return getId;
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public String getTotalPrice(int id) {
        try {
            String query = "SELECT totalprice FROM bills WHERE id=?";
            preparedStatement = (PreparedStatement) connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            String totalPrice = "";
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                totalPrice = rs.getString("totalprice");
            }
            return totalPrice;
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getReturnPrice(int id) {
        try {
            String query = "SELECT returnmoney FROM bills WHERE id=?";
            preparedStatement = (PreparedStatement) connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            String returnPrice = "";
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                returnPrice = rs.getString("returnmoney");
            }
            return returnPrice;
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void deleteProductsBills(String number) {
        try {
            int id = getId(number);
            String query = "DELETE FROM products_bills WHERE bill_id =? ";
            preparedStatement = (PreparedStatement) connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            salesDao.minusTotalPrices(getDate(id), getTotalPrice(id));
            deleteBill(id);
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteBill(int id) {
        try {
            String query = "DELETE FROM bills WHERE id =? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public List<Bill> getBillList() {
        try {
            String query = "SELECT * FROM bills ORDER BY created DESC";
            ResultSet rs = getResult(query);
            List<Bill> data = new ArrayList<>();
            while (rs.next()) {
                int billId = rs.getInt("id");
                String numberBill = rs.getString("numberbill");
                String returnMoney = rs.getString("returnmoney");
                String acceptMoney = rs.getString("acceptmoney");
                String person = rs.getString("person");
                String totalPrice = rs.getString("totalprice");
                String fik = rs.getString("FIK");
                String bkp = rs.getString("BKP");
                String pkp = rs.getString("PKP");
                String porad_cis = rs.getString("porad_cis");
                String dan1 = rs.getString("dan1");
                String dan2 = rs.getString("dan2");
                String zakl_dan1 = rs.getString("zakl_dan1");
                String zakl_dan2 = rs.getString("zakl_dan2");
                boolean payByCard = rs.getBoolean("pay_by_card");
                Timestamp created = rs.getTimestamp("created");
                Bill bill = new Bill(billId, numberBill, DateUtils.formatDate(created), DateUtils.formatTime(created), returnMoney, totalPrice, person, acceptMoney, fik, bkp, pkp, porad_cis, dan1, dan2, zakl_dan1, zakl_dan2, payByCard);
                data.add(bill);
            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Bill> getCurrentBillList() {
        List<Bill> data = new ArrayList<>();
        try {
            Date date = new Date();
            String query = "SELECT * FROm bills WHERE `date`=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, SIMPLE_DATE_FORMAT.format(date));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int billId = rs.getInt("id");
                String numberBill = rs.getString("numberbill");
                String returnMoney = rs.getString("returnmoney");
                String acceptMoney = rs.getString("acceptmoney");
                String person = rs.getString("person");
                String totalPrice = rs.getString("totalprice");
                String fik = rs.getString("FIK");
                String bkp = rs.getString("BKP");
                String pkp = rs.getString("PKP");
                String porad_cis = rs.getString("porad_cis");
                String dan1 = rs.getString("dan1");
                String dan2 = rs.getString("dan2");
                String zakl_dan1 = rs.getString("zakl_dan1");
                String zakl_dan2 = rs.getString("zakl_dan2");
                boolean payByCard = rs.getBoolean("pay_by_card");
                Timestamp created = rs.getTimestamp("created");
                Bill bill = new Bill(billId, numberBill, DateUtils.formatDate(created), DateUtils.formatTime(created), returnMoney, totalPrice, person, acceptMoney, fik, bkp, pkp, porad_cis, dan1, dan2, zakl_dan1, zakl_dan2, payByCard);
                data.add(bill);
            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getCurrentBill() {
        try {
            Date date = new Date();
            String query = "SELECT `totalprice` FROM `products_bills` WHERE `date`=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, SIMPLE_DATE_FORMAT.format(date));
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next() == true) {
                return rs.getString("totalprice");
            }
            return "0";
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Product> getBillProductList(int id) {
        List<Product> data = new ArrayList<>();
        try {
            String query = "SELECT `product_id` , `quantity`, price  FROM `products_bills` WHERE `bill_id`=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int product_id = rs.getInt("product_id");
                String quantity = rs.getString("quantity");
                String price = rs.getString("price");
                Product product = productDao.findById(product_id, quantity, price);
                data.add(product);
            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    @Override
    public int getPoradCisel() {
        String query = "SELECT MAX(porad_cis) AS porad_cis FROM bills";
        ResultSet rs = getResult(query);
        try {
            while (rs.next()) {
                return rs.getInt("porad_cis");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public Bill getBill(String id) {
        try {
            String query = "SELECT * FROM bills WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int billId = rs.getInt("id");
                String numberBill = rs.getString("numberbill");
                String returnMoney = rs.getString("returnmoney");
                String acceptMoney = rs.getString("acceptmoney");
                String person = rs.getString("person");
                String totalPrice = rs.getString("totalprice");
                String fik = rs.getString("FIK");
                String bkp = rs.getString("BKP");
                String pkp = rs.getString("PKP");
                String porad_cis = rs.getString("porad_cis");
                String dan1 = rs.getString("dan1");
                String dan2 = rs.getString("dan2");
                String zakl_dan1 = rs.getString("zakl_dan1");
                String zakl_dan2 = rs.getString("zakl_dan2");
                boolean payByCard = rs.getBoolean("pay_by_card");
                Timestamp created = rs.getTimestamp("created");
                Bill bill = new Bill(billId, numberBill, DateUtils.formatDate(created), DateUtils.formatTime(created), returnMoney, totalPrice, person, acceptMoney, fik, bkp, pkp, porad_cis, dan1, dan2, zakl_dan1, zakl_dan2, payByCard);
                return bill;
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Eet> getCurrentBillOfflineList() {
        List<Eet> data = new ArrayList<>();
        try {
            String query = """
                    SELECT b.*, COALESCE(es.status, 'LEGACY') AS eet_status
                      FROM bills b
                      JOIN eet_submission es ON es.bill_id = b.id
                     WHERE es.status IN ('PENDING', 'SENDING', 'RETRY', 'REJECTED')
                    """;
            preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String numberbill = rs.getString("numberbill");
                Timestamp created = rs.getTimestamp("created");
                String status = switch (rs.getString("eet_status")) {
                    case "PENDING" -> "Čeká";
                    case "SENDING" -> "Odesílá se";
                    case "RETRY" -> "Opakování";
                    case "REJECTED" -> "Odmítnuto";
                    default -> "Legacy";
                };
                Eet eet = new Eet(id, numberbill, DateUtils.formatDate(created), DateUtils.formatTime(created), status);
                data.add(eet);
            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getDate(int id) {
        try {
            String query = "SELECT created FROM bills WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Timestamp created = rs.getTimestamp("created");
                return DateUtils.classicFormatDate(created);
            }
            return "";
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> listId() {
        try {
            String query = """
                    SELECT b.id FROM bills b
                    JOIN eet_submission es ON es.bill_id = b.id
                    WHERE es.status IN ('PENDING', 'SENDING', 'RETRY', 'REJECTED')
                    """;
            List<Integer> listId = new ArrayList<>();
            preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                listId.add(id);
            }
            return listId;
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void deleteBills(int id) {
        try {
            String query = "DELETE FROM products_bills WHERE bill_id =? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            salesDao.minusTotalPrices(getDate(id), getTotalPrice(id));
            deleteBill(id);
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateFik(int id, String fik) {
        try {
            String query = "UPDATE bills set FIK =? WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, fik);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateFik(String numberOfBill, String fik) {
        try {
            String query = "UPDATE bills set FIK =? WHERE numberbill=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, fik);
            preparedStatement.setString(2, numberOfBill);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<BillModel> getOfflineBill() {
        Date date = new Date();
        List<BillModel> billModels = new ArrayList<>();
        String query = "SELECT * FROM `bills` WHERE (`FIK` IS NULL OR `FIK` = '') AND `date` = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, SIMPLE_DATE_FORMAT.format(date));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                BillModel billModel = new BillModel();
                billModel.setId(rs.getInt("id"));
                billModel.setNumberBill(rs.getString("numberbill"));
                billModel.setCreated(rs.getTimestamp("created"));
                billModel.setTotalPrice(rs.getString("totalprice"));
                billModel.setReturnMoney(rs.getString("returnmoney"));
                billModel.setAcceptMoney(rs.getString("acceptMoney"));
                billModel.setPerson(rs.getString("person"));
                billModel.setDate(rs.getDate("date"));
                billModel.setPkp(rs.getString("PKP"));
                billModel.setBkp(rs.getString("BKP"));
                billModel.setPorad_cis(rs.getInt("porad_cis"));
                billModel.setZakl_dan1(rs.getString("zakl_dan1"));
                billModel.setDan1(rs.getString("dan1"));
                billModel.setZakl_dan2(rs.getString("zakl_dan2"));
                billModel.setDan2(rs.getString("dan2"));
                billModels.add(billModel);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billModels;
    }
}
