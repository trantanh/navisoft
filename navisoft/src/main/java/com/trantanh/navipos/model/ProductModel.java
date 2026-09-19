package com.trantanh.navipos.model;

import com.trantanh.navipos.manager.DatabaseManager;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.List;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 * 14.02.2021
 */
@Entity
@Table(name = "products")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String price;
    private String barcode;
    private String quantity;
    private Category category;
    private String dph;
    private String priceWithoutDph;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDph() {
        return dph;
    }

    public void setDph(String dph) {
        this.dph = dph;
    }

    public String getPriceWithoutDph() {
        return priceWithoutDph;
    }

    public void setPriceWithoutDph(String priceWithoutDph) {
        this.priceWithoutDph = priceWithoutDph;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn



    public static void main(String[] args) {
        DatabaseManager<ProductModel> productModelDatabaseManager = new DatabaseManager<>();
        productModelDatabaseManager.setup();
        List<ProductModel> list =productModelDatabaseManager.findAll(ProductModel.class);
        productModelDatabaseManager.exit();
    }
}
