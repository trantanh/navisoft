package com.trantanh.navipos.model;

/**
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class Bill {

    private int id;
    private String numberBill;
    private String created;
    private String time;
    private String returnPrice;
    private String totalPrice;
    private String namePerson;
    private String acceptMoney;
    private String fik;
    private String bkp;
    private String pkp;
    private String poradi_cis;
    private String dan1;
    private String dan2;
    private String zakl_dan1;
    private String zakl_dan2;
    private boolean payByCard;
    public Bill(int id, String numberBill,
                String created, String time, String returnPrice, String totalPrice,
                String person, String acceptMoney, String fik, String bkp, String pkp,
                String poradi_cis, String dan1, String dan2,
                String zakl_dan1, String zakl_dan2,
    boolean payByCard) {
        this.id = id;
        this.numberBill = numberBill;
        this.created = created;
        this.time = time;
        this.returnPrice = returnPrice;
        this.totalPrice = totalPrice;
        this.namePerson = person;
        this.acceptMoney = acceptMoney;
        this.fik = fik;
        this.bkp = bkp;
        this.pkp = pkp;
        this.poradi_cis = poradi_cis;
        this.dan1 = dan1;
        this.dan2 = dan2;
        this.zakl_dan1 = zakl_dan1;
        this.zakl_dan2 = zakl_dan2;
        this.payByCard = payByCard;
    }

    public String getDan1() {
        return dan1;
    }

    public String getZakl_dan1() {
        return zakl_dan1;
    }

    public String getZakl_dan2() {
        return zakl_dan2;
    }

    public String getDan2() {
        return dan2;
    }

    public String getPoradiCis() {
        return poradi_cis;
    }

    public String getPkp() {
        return pkp;
    }

    public String getFik() {
        return fik;
    }

    public String getBkp() {
        return bkp;
    }

    public String getPersonName() {
        return namePerson;
    }

    public String getAcceptMoney() {
        return acceptMoney;
    }

    public String getReturnPrice() {
        return returnPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public String getNumberBill() {
        return numberBill;
    }

    public String getDate() {
        return created;
    }

    public void setNumberBill(String numberBill) {
        this.numberBill = numberBill;
    }

    public boolean isPayByCard() {
        return payByCard;
    }

    public void setPayByCard(boolean payByCard) {
        this.payByCard = payByCard;
    }
}
