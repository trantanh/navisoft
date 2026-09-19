package com.trantanh.navipos.dao.impl;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class SalesDetail {
    
   protected String zakl_dan2;
   protected String dan2;
   protected String zakl_dan1;
   protected String dan1;
   private String totalPrice = "0";
   private String payByCard = "0";
   
   public SalesDetail(String zakl_dan2, String dan2, String zakl_dan1, String dan1) {
        this.zakl_dan2 = zakl_dan2;
        this.dan2 = dan2;
        this.zakl_dan1 = zakl_dan1;
        this.dan1 = dan1;
    }

    public SalesDetail(String zakl_dan2, String dan2, String zakl_dan1, String dan1, String totalPrice, String payByCard) {
        this.zakl_dan2 = zakl_dan2;
        this.dan2 = dan2;
        this.zakl_dan1 = zakl_dan1;
        this.dan1 = dan1;
        this.totalPrice = totalPrice;
        this.payByCard = payByCard;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getZakl_dan2() {
        return zakl_dan2;
    }

    public String getDan2() {
        return dan2;
    }

    public String getZakl_dan1() {
        return zakl_dan1;
    }

    public String getDan1() {
        return dan1;
    }

    public void setPayByCard(String payByCard) {
        this.payByCard = payByCard;
    }

    public String getPayByCard() {
        return payByCard;
    }
}
