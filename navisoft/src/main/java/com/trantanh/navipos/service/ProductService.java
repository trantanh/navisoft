package com.trantanh.navipos.service;

import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.model.Tax;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz Service layer for {@link  Product}
 */
public interface ProductService {

    Product searchProduct(String barcode);

    int getCategoryId(String barcode);

    void addProduct(String name, String priceText, String barcode, String quantity, String categoryName);

    boolean validatorBarcodeProduct(String barcode);

    List<Product> favoriteProducts();

    void updateUnit(String unit, String barcode);

    void countSale(int product_id, int count);

    void deleteProduct(String barcode);

    int getId(String barcode);

    ObservableList<Product> findAllByCategory(int id);

    ObservableList<Product> findAllByCategory(String name);

    Tax getTax(String barcode);

    ObservableList<Product> getProductList();

    ObservableList<Product> getProductListSale();

    void addDiscount(String price, String from_date, String to_date, int id);

    void updateProduct(String name, String price, String quantity, String category, String barcode, String id);

    void updateProduct(String name, String newName, String barcode);

}
