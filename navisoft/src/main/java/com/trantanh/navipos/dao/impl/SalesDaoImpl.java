package com.trantanh.navipos.dao.impl;

import com.trantanh.navipos.dao.SalesDao;
import com.trantanh.navipos.manager.DatabaseManager;
import com.trantanh.navipos.model.DatabaseConnector;
import com.trantanh.navipos.model.Statistics;
import com.trantanh.navipos.model.StatisticsTotalPrices;
import com.trantanh.navipos.utils.DateUtils;
import com.trantanh.navipos.utils.PriceUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class SalesDaoImpl extends DatabaseConnector implements SalesDao {

    private DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private DatabaseManager<StatisticsTotalPrices> statisticsTotalPricesDatabaseManager = new DatabaseManager<>();;

    public SalesDaoImpl() {
        super();
    }

    @Override
    public void updateValue(String date, String value) {
        String query = "UPDATE `statistics_totalprices` set `totalprice` =?  WHERE `date`=?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(2, date);
            preparedStatement.setString(1, value);
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getCurrentTotalPrice() {
        statisticsTotalPricesDatabaseManager.setup();
        List<StatisticsTotalPrices> statisticsTotalPricesList = statisticsTotalPricesDatabaseManager.findAll(StatisticsTotalPrices.class);
        String totalPrice = "0";
        for (StatisticsTotalPrices statisticsTotalPrices : statisticsTotalPricesList) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String currentDate = dateFormat.format(date);
            String StringDate = dateFormat.format(statisticsTotalPrices.getDate());
            if (currentDate.equals(StringDate)) {
                totalPrice = statisticsTotalPrices.getTotalPrice();
            }
        }
        statisticsTotalPricesDatabaseManager.exit();
        return totalPrice;
    }

    @Override
    public boolean minusTotalPrices(String date, String value) {
        try {
            String query = "SELECT `totalprice` FROM  `statistics_totalprices`  WHERE `date`=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                double totalPrice = Double.valueOf(rs.getString("totalprice")) - Double.valueOf(value);
                updateValue(date, String.valueOf(totalPrice));
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public ObservableList<Statistics> getSales(int category) {
        try {
            String query = "SELECT * FROM `statistics` WHERE  category_id =? ";
            preparedStatement = (PreparedStatement) connection.prepareStatement(query);
            preparedStatement.setInt(1, category);
            ResultSet rs = preparedStatement.executeQuery();
            ObservableList<Statistics> data = FXCollections.observableArrayList();
            while (rs.next()) {
                int id = rs.getInt("id");
                String price = rs.getString("totalprice");
                Date created = rs.getDate("date");
                SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy");
                Statistics statistics = new Statistics(id, price, dt1.format(created));
                data.add(statistics);
            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void addSale(String totalPrice) {
        try {
            /*
            1.  vybrat celkoou cenu z dnesniho dne
            1.a jestli dnesni neni, tak proste pridej do db
            
            2. precist celkoou cenu
             */
            Date date = new Date();
            Statement st = connection.createStatement();
            String query = "SELECT totalprice, id FROM `statistics_totalprices` WHERE date=?";
            preparedStatement = (PreparedStatement) connection.prepareStatement(query);
            preparedStatement.setString(1, DATE_FORMAT.format(date));
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next() == true) {
                String getTotalPrice = rs.getString("totalprice");
                int id = rs.getInt("id");
                double currentPrice = Double.valueOf(getTotalPrice);
                double newPrice = Double.valueOf(totalPrice);
                currentPrice += newPrice;
                String saveTotaPrice = DateUtils.format(currentPrice);
                query = "UPDATE `statistics_totalprices` set totalprice = ? WHERE id =?";
                preparedStatement = (PreparedStatement) connection.prepareStatement(query);
                preparedStatement.setInt(2, id);
                preparedStatement.setString(1, saveTotaPrice);
                preparedStatement.execute();
            } else {
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                query = "INSERT INTO `statistics_totalprices` (totalprice,  date) VALUES ( ?, ?) ";
                preparedStatement = (PreparedStatement) connection.prepareStatement(query);
                preparedStatement.setString(1, totalPrice);
                preparedStatement.setDate(2, sqlDate);
                preparedStatement.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addSale(String totalPrice, int category_id) {
        try {
            DecimalFormat df = new DecimalFormat("##0.00");
            /*
            1.  vybrat celkoou cenu z dnesniho dne
            1.a jestli dnesni neni, tak proste pridej do db
            
            2. precist celkoou cenu
             */
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Statement st = connection.createStatement();
            String query = "SELECT totalprice, id FROM statistics WHERE date=? AND category_id =?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dateFormat.format(date));
            preparedStatement.setInt(2, category_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String getTotalPrice = rs.getString("totalprice");
                int id = rs.getInt("id");
                Double currentPrice = new Double(getTotalPrice);
                Double newPrice = new Double(totalPrice);
                currentPrice += newPrice;
                String saveTotaPrice = df.format(currentPrice);
                query = "UPDATE statistics set totalprice = ? WHERE id =? AND category_id =?";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, saveTotaPrice);
                preparedStatement.setInt(2, id);
                preparedStatement.setInt(3, category_id);
                preparedStatement.execute();
            } else {
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                query = "INSERT INTO statistics (totalprice,  date, category_id) VALUES ( ?, ?,?) ";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, totalPrice);
                preparedStatement.setDate(2, sqlDate);
                preparedStatement.setInt(3, category_id);
                preparedStatement.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public double getTotalPrice(String from, String to) {
        try {
            //SELECT `totalprice` FROM `statistics` WHERE `date` BETWEEN '2016-04-29' AND'2016-05-01'
            String query = "SELECT `totalprice` FROM `statistics_totalprices` WHERE `date` BETWEEN ? AND ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, from);
            preparedStatement.setString(2, to);
            ResultSet rs = preparedStatement.executeQuery();
            double sum = 0;
            while (rs.next()) {
                sum += Double.valueOf(rs.getString("totalprice"));
            }
            return sum;
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public SalesDetail getTodaySale() {
        try {
            Date date = new Date();
            String query = "SELECT `totalprice`, `zakl_dan2`, `dan1`, `zakl_dan1`, `dan2` FROM `bills` WHERE `date`=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, DATE_FORMAT.format(date));
            ResultSet rs = preparedStatement.executeQuery();
            double totalPrice = 0;
            double dan1 = 0;
            double dan2 = 0;
            double zakl_dan1 = 0;
            double zakl_dan2 = 0;
            while (rs.next()) {
                totalPrice += Double.parseDouble(rs.getString("totalprice"));
                dan1 += Double.parseDouble(rs.getString("dan1"));
                dan2 += Double.parseDouble(rs.getString("dan2"));
                zakl_dan1 += Double.parseDouble(rs.getString("zakl_dan1"));
                zakl_dan2 += Double.parseDouble(rs.getString("zakl_dan2"));
            }
            String payByCard = todaysTerminalSales(date);
            SalesDetail salesDetail = new SalesDetail(DateUtils.format(zakl_dan2), DateUtils.format(dan2), DateUtils.format(zakl_dan1), DateUtils.format(dan1), String.valueOf(totalPrice), payByCard);
            return salesDetail;
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public SalesDetail getMonthTotalPrice(int month, int year) {
        LocalDate localDate = LocalDate.of(year, Month.of(month), 1);
        java.sql.Date date = java.sql.Date.valueOf(localDate);
        try {
            String query = "SELECT `totalprice`,`zakl_dan2`, `dan1`, `zakl_dan1`, `dan2` FROM `bills` WHERE MONTH(date) = ? AND YEAR(date) = ?";
//            String query = "SELECT `totalprice`,`zakl_dan2`, `dan1`, `zakl_dan1`, `dan2` FROM `bills` WHERE MONTH('2023/02/25 09:08') AND YEAR('2023/02/25 09:08')";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, date);
            preparedStatement.setDate(2, date);
            ResultSet rs = preparedStatement.executeQuery();
            double totalPrice = 0;
            double dan1 = 0;
            double dan2 = 0;
            double zakl_dan1 = 0;
            double zakl_dan2 = 0;
            while (rs.next()) {
                totalPrice += Double.parseDouble(rs.getString("totalprice"));
                dan1 += Double.parseDouble(rs.getString("dan1"));
                dan2 += Double.parseDouble(rs.getString("dan2"));
                zakl_dan1 += Double.parseDouble(rs.getString("zakl_dan1"));
                zakl_dan2 += Double.parseDouble(rs.getString("zakl_dan2"));
            }
            String payByCard = monthTerminalSales(month, year);
            String base_tax2 = PriceUtils.priceMathRound(zakl_dan2);
            String tax2 = PriceUtils.priceMathRound(dan2);
            String base_tax = PriceUtils.priceMathRound(zakl_dan1);
            String tax = PriceUtils.priceMathRound(dan1);
            SalesDetail salesDetail = new SalesDetail(base_tax2, tax2, base_tax, tax);
            salesDetail.setTotalPrice(String.valueOf(totalPrice));
            salesDetail.setPayByCard(payByCard);
            preparedStatement.close();
            return salesDetail;
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public SalesDetail getCurrentData(java.sql.Date date) {
        try {
            String query = "SELECT `totalprice`,`zakl_dan2`, `dan1`, `zakl_dan1`, `dan2` FROM `bills` WHERE `date`=?";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, DATE_FORMAT.format(date));
            ResultSet resulSet = preparedStatement.executeQuery();
            double totalPrice = 0;
            double dan1 = 0;
            double dan2 = 0;
            double zakl_dan1 = 0;
            double zakl_dan2 = 0;
            while (resulSet.next()) {
                totalPrice += Double.parseDouble(resulSet.getString("totalprice"));
                dan1 += Double.parseDouble(resulSet.getString("dan1"));
                dan2 += Double.parseDouble(resulSet.getString("dan2"));
                zakl_dan1 += Double.parseDouble(resulSet.getString("zakl_dan1"));
                zakl_dan2 += Double.parseDouble(resulSet.getString("zakl_dan2"));
            }
            String base_tax2 = PriceUtils.priceMathRound(zakl_dan2);
            String tax2 = PriceUtils.priceMathRound(dan2);
            String base_tax = PriceUtils.priceMathRound(zakl_dan1);
            String tax = PriceUtils.priceMathRound(dan1);
            SalesDetail salesDetail = new SalesDetail(base_tax2, tax2, base_tax, tax);
            salesDetail.setTotalPrice(String.valueOf(totalPrice));
            return salesDetail;
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String monthTerminalSales(int month, int year) {
        LocalDate localDate = LocalDate.of(year, Month.of(month), 1);
        java.sql.Date date = java.sql.Date.valueOf(localDate);
        try {
            String query = "SELECT `totalprice` FROM `bills` WHERE MONTH(date) = ? AND YEAR(date) = ? AND `pay_by_card`=true";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setDate(1, date);
            preparedStatement.setDate(2, date);
            ResultSet resulSet = preparedStatement.executeQuery();
            double totalPrice = 0;
            while (resulSet.next()) {
                totalPrice += Double.parseDouble(resulSet.getString("totalprice"));
            }
            return String.valueOf(totalPrice);
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String todaysTerminalSales(Date date) {
        try {
            String query = "SELECT `totalprice` FROM `bills` WHERE `date`=? AND `pay_by_card`=true";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, DATE_FORMAT.format(date));
            ResultSet resulSet = preparedStatement.executeQuery();
            double totalPrice = 0;
            while (resulSet.next()) {
                totalPrice += Double.parseDouble(resulSet.getString("totalprice"));
            }
            return String.valueOf(totalPrice);
        } catch (SQLException ex) {
            Logger.getLogger(SalesDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
