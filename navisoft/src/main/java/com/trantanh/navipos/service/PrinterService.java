package com.trantanh.navipos.service;

/**
 * @author Tran Tuan Anh, tran.tuan.anh@gem.cz
 * 01.12.2019
 */
public interface PrinterService {
    boolean printPriceTag(String name, String price, String unit, String unitCount);
    boolean printPriceTagWithBarcode(String name, String price, String unit, String unitCount, String barcode);
}
