package com.trantanh.navipos.service;

import javafx.scene.Node;

/**
 * @author Tran Tuan Anh, tran.tuan.anh@gem.cz
 * 01.12.2019
 */
public interface PrinterService {
    void printPriceTag(String name, String price, String unit, String unitCount);
    void printPriceTagWithBarcode(String name, String price, String unit, String unitCount, String barcode);
}
