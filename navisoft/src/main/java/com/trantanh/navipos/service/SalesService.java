package com.trantanh.navipos.service;

import com.trantanh.navipos.dao.impl.SalesDetail;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.model.Statistics;
import javafx.collections.ObservableList;

import java.sql.Date;

public interface SalesService {

    SalesDetail getCurrentData(Date date);

    void updateValue(String date, String value);

    String getCurrentTotalPrice();

    boolean minusTotalPrices(String date, String value);

    ObservableList<Statistics> getSales(int category);

    void addSale(String totalPrice);

    void addSale(String totalPrice, int category_id);

    double getTotalPrice(String from, String to);

    SalesDetail getTodaySale();

    SalesDetail getMonthTotalPrice(int month, int year);

    void saveSale(int bill_id, ObservableList<Product> data);

    void addTodayPrice(String totalPrice);
}
