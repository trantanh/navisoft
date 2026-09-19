package com.trantanh.navipos.model;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class Tax {

    private String tax;
    private String price;

    public Tax(String tax, String price) {
        this.tax = tax;
        this.price = price;
    }

    public String getTax() {
        return tax;
    }

    public String getPrice() {
        return price;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
