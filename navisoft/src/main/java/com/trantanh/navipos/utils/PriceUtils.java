package com.trantanh.navipos.utils;

import com.trantanh.navipos.model.Product;
import javafx.collections.ObservableList;

/**
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public final class PriceUtils {

    private PriceUtils() {
    }

    public static String replacePriceValue(String value) {
        if (value.contains(",")) {
            String replace = value.replace(',', '.');
            return replace;
        }
        return value;
    }

    public static String priceMathRound(String price) {
        double result = Math.round(Double.parseDouble(price));
        return DateUtils.format(result);
    }

    public static String priceMathRound(double price) {
        double result = Math.round(price);
        return DateUtils.format(result);
    }

    public static String negativePrice(String price) {
        if (price.equals("0.00")) {
            return price;
        } else {
            return "-" + price;
        }
    }

    public static double totalPrice(ObservableList<Product> products) {
        double totalPrice = 0;
        for (Product product : products) {
            double price = Double.parseDouble(product.getPrice());
            if (price > 0) {
                totalPrice += price * Double.parseDouble(product.getQuantity());
            }
        }
        return totalPrice;
    }
}
