package com.trantanh.navipos.dao;

import com.trantanh.navipos.dao.impl.SalesDetail;
import com.trantanh.navipos.model.Statistics;
import javafx.collections.ObservableList;

import java.sql.Date;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public interface SalesDao {

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

}
