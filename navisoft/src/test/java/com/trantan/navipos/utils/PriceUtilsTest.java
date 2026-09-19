package com.trantan.navipos.utils;

import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.utils.PriceUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 * 24.08.2020
 */
public class PriceUtilsTest {

    @Test
    public void testTotalPrice() {
        ObservableList<Product> productObservableList = productObservableList();
        assertEquals(130.0, PriceUtils.totalPrice(productObservableList));
    }


    private ObservableList<Product> productObservableList() {
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        Product product = new Product("Jidlo", "115", "12345", "15", "Potraviny", "1", "");
        Product product2 = new Product("Lahev", "-15", "12345", "15", "Potraviny", "1", "");
        Product product3 = new Product("Lahev", "-15", "12345", "15", "Potraviny", "1", "");
        Product product4 = new Product("Pivo", "15", "12345", "15", "Potraviny", "1", "");
        productObservableList.add(product);
        productObservableList.add(product2);
        productObservableList.add(product3);
        productObservableList.add(product4);
        return productObservableList;
    }
}
