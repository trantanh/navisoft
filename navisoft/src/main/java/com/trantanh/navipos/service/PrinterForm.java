package com.trantanh.navipos.service;

/**
 * @author tuan
 */
public interface PrinterForm {

    void printBillWithoutTax();

    void printBillWithTax();

    void printBillWithoutConnecting();

    void printBill();

    void printDaySales(String dan1, String zaklad_dan1, String dan2, String zaklad_dan2, String totalPrice, String date, String payByCard);

    void printMonthSales();

}
