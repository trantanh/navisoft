package com.trantanh.navipos.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public class BillModel {
    
    private int id;
    private String numberBill;
    private Timestamp created;
    private String totalPrice;
    private String returnMoney;
    private String acceptMoney;
    private String person;
    private Date date;
    private String fik;
    private String bkp;
    private String pkp;
    private int porad_cis;
    private String zakl_dan1;
    private String dan1;
    private String zakl_dan2;
    private String dan2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberBill() {
        return numberBill;
    }

    public void setNumberBill(String numberBill) {
        this.numberBill = numberBill;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }

    public String getAcceptMoney() {
        return acceptMoney;
    }

    public void setAcceptMoney(String acceptMoney) {
        this.acceptMoney = acceptMoney;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFik() {
        return fik;
    }

    public void setFik(String fik) {
        this.fik = fik;
    }

    public String getBkp() {
        return bkp;
    }

    public void setBkp(String bkp) {
        this.bkp = bkp;
    }

    public String getPkp() {
        return pkp;
    }

    public void setPkp(String pkp) {
        this.pkp = pkp;
    }

    public int getPorad_cis() {
        return porad_cis;
    }

    public void setPorad_cis(int porad_cis) {
        this.porad_cis = porad_cis;
    }

    public String getZakl_dan1() {
        return zakl_dan1;
    }

    public void setZakl_dan1(String zakl_dan1) {
        this.zakl_dan1 = zakl_dan1;
    }

    public String getDan1() {
        return dan1;
    }

    public void setDan1(String dan1) {
        this.dan1 = dan1;
    }

    public String getZakl_dan2() {
        return zakl_dan2;
    }

    public void setZakl_dan2(String zakl_dan2) {
        this.zakl_dan2 = zakl_dan2;
    }

    public String getDan2() {
        return dan2;
    }

    public void setDan2(String dan2) {
        this.dan2 = dan2;
    }
          
}
