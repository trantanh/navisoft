package com.trantan.navipos.utils;

import com.trantanh.navipos.dao.impl.SalesDetail;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.utils.TaxUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 * 09.07.2020
 */
public class TaxUtilsTest {

    @Test
    public void testTax12() {
        ObservableList<Product> productObservableList = productTax12();
        SalesDetail salesDetail = TaxUtils.taxProduct(productObservableList);
        String zakl_dan2 = salesDetail.getZakl_dan2();
        String dan2 = salesDetail.getDan2();
        assertEquals("100.00", zakl_dan2);
        assertEquals("12.00", dan2);
    }


    @Test
    public void testTax21(){
        ObservableList<Product> productObservableList = productTax21();
        SalesDetail salesDetail = TaxUtils.taxProduct(productObservableList);
        String zakl_dan1 = salesDetail.getZakl_dan1();
        String dan1 = salesDetail.getDan1();
        assertEquals("17.36", zakl_dan1);
        assertEquals("3.64", dan1);
    }

    @Test
    public void testMixTax(){
        ObservableList<Product> productObservableList = mixProductsTax();
        SalesDetail salesDetail = TaxUtils.taxProduct(productObservableList);
        String zakl_dan1 = salesDetail.getZakl_dan1();
        String dan1 = salesDetail.getDan1();

        String zakl_dan2 = salesDetail.getZakl_dan2();
        String dan2 = salesDetail.getDan2();
        assertEquals("120.66", zakl_dan1);
        assertEquals("25.34", dan1);

        assertEquals("381.25", zakl_dan2);
        assertEquals("45.75", dan2);
    }


    @Test
    public void testClotheTax21(){
        ObservableList<Product> productObservableList = clotheTax21();
        SalesDetail salesDetail = TaxUtils.taxProduct(productObservableList);
        String zakl_dan1 = salesDetail.getZakl_dan1();
        String dan1 = salesDetail.getDan1();
        assertEquals("1200.00", zakl_dan1);
        assertEquals("252.00", dan1);
    }

    @Test
    public void testWithBottle(){
        ObservableList<Product> productObservableList = productTax12WithBottle();
        SalesDetail salesDetail = TaxUtils.taxProduct(productObservableList);
        String zakl_dan2 = salesDetail.getZakl_dan2();
        String dan2 = salesDetail.getDan2();
        assertEquals("100.00", zakl_dan2);
        assertEquals("12.00", dan2);
    }
    private ObservableList<Product> productTax12(){
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        Product product = new Product("Jidlo", "112", "12345", "12", "Potraviny", "1", "");
        productObservableList.add(product);
        return productObservableList;
    }

    private ObservableList<Product> productTax21(){
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        Product product = new Product("Pivo", "21", "12345", "21", "Alkohol", "1", "");
        productObservableList.add(product);
        return productObservableList;
    }


    private ObservableList<Product> mixProductsTax() {
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        Product product = new Product("Potraviny", "427", "12345", "12", "Potraviny", "1","");
        Product product2 = new Product("Drogerie", "146", "12348", "21", "Drogerie", "1","");
        Product product3 = new Product("Lahev", "-3", "12345", "12", "Potraviny", "1", "");
        productObservableList.add(product);
        productObservableList.add(product2);
        productObservableList.add(product3);
        return productObservableList;
    }


    private ObservableList<Product> clotheTax21(){
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        Product product = new Product("Obleceni", "1452", "12345", "21", "Obleceni", "1", "");
        productObservableList.add(product);
        return productObservableList;
    }


    private ObservableList<Product> productTax12WithBottle(){
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        Product product = new Product("Jidlo", "112", "12345", "12", "Potraviny", "1","");
        Product product2 = new Product("Lahev", "-3", "12345", "12", "Potraviny", "1", "");
        productObservableList.add(product);
        productObservableList.add(product2);
        return productObservableList;
    }
}
