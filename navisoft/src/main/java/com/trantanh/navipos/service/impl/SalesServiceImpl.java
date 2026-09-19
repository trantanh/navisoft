package com.trantanh.navipos.service.impl;

import com.trantanh.navipos.dao.SalesDao;
import com.trantanh.navipos.dao.impl.SalesDaoImpl;
import com.trantanh.navipos.dao.impl.SalesDetail;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.model.Statistics;
import com.trantanh.navipos.service.BillService;
import com.trantanh.navipos.service.ProductService;
import com.trantanh.navipos.service.SalesService;
import com.trantanh.navipos.utils.DateUtils;
import javafx.collections.ObservableList;

import java.sql.Date;

public class SalesServiceImpl implements SalesService {
    private static SalesServiceImpl instance;
    private SalesDao salesDao = new SalesDaoImpl();
    private BillService billService = BillServiceImpl.getInstance();
    private ProductService productService = ProductServiceImpl.getInstance();

    public static SalesService getInstance(){
        if (instance == null) {
            instance = new SalesServiceImpl();
        }
        return SalesServiceImpl.instance;
    }

    @Override
    public SalesDetail getCurrentData(Date date) {
        return salesDao.getCurrentData(date);
    }

    @Override
    public void updateValue(String date, String value) {
        salesDao.updateValue(date, value);
    }

    @Override
    public String getCurrentTotalPrice() {
        return salesDao.getCurrentTotalPrice();
    }

    @Override
    public boolean minusTotalPrices(String date, String value) {
        return salesDao.minusTotalPrices(date, value);
    }

    @Override
    public ObservableList<Statistics> getSales(int category) {
        return salesDao.getSales(category);
    }

    @Override
    public void addSale(String totalPrice) {
        salesDao.addSale(totalPrice);
    }

    @Override
    public void addSale(String totalPrice, int category_id) {
        salesDao.addSale(totalPrice, category_id);
    }

    @Override
    public double getTotalPrice(String from, String to) {
        return salesDao.getTotalPrice(from, to);
    }

    @Override
    public SalesDetail getTodaySale() {
        return salesDao.getTodaySale();
    }

    @Override
    public SalesDetail getMonthTotalPrice(int month, int year) {
        return salesDao.getMonthTotalPrice(month, year);
    }

    @Override
    public void saveSale(int billId, ObservableList<Product> data) {
        for (Product product : data) {
            billService.addBill(billId, product.getId(), product.getQuantity(), product.getPrice());
            productService.countSale(product.getId(), Integer.parseInt(product.getQuantity()));
            double productPrice = Double.valueOf(product.getPrice()) * Integer.parseInt(product.getQuantity());
            int categoryId = productService.getCategoryId(product.getBarcode());
            salesDao.addSale(DateUtils.format(productPrice), categoryId);
        }
    }

    @Override
    public void addTodayPrice(String totalPrice) {
        salesDao.addSale(totalPrice);
    }
}
