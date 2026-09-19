package com.trantanh.navipos.dao;

import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.model.Tax;
import javafx.collections.ObservableList;

import java.util.List;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz DAO layer for {@link Product}
 */
public interface ProductDao {

    void countSale(int product_id, int count);

    void addProduct(String name, String price, String barcode, String quantity, int idCategory);

    void deleteProduct(String barcode);

    Product findByBarcode(String barcode);

    int getCategoryId(String barcode);

    List<Product> findAllByCategory(int id);

    void updateUnit(String unit, String barcode);

    Tax getTax(String barcode);

    void updateProduct(String name, String newName, String barcode);

    Product findById(int id, String quantity, String price);

    List<Product> findAll();

    List<Product> getProductListSale();

    void addDiscount(String price, String from_date, String to_date, int id);

    void updateProduct(String name, String price, String quantity, String category, String barcode, String id);
}
