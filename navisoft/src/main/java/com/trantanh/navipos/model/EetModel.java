package com.trantanh.navipos.model;

import java.util.Date;

/**
 *
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public class EetModel {

    private String numberbill;
    private double price;
    private String bkp;
    private String pkp;
    private String fik;
    private String dat_prij;
    private int porad_cis;
    private String zakl_dan1;
    private String zakl_dan2;
    private String dan2;
    private String dan1;
    private Date date;

    public String getNumberbill() {
        return numberbill;
    }

    public void setNumberbill(String numberbill) {
        this.numberbill = numberbill;
    }
    public String getFik() {
        return fik;
    }

    public void setFik(String fik) {
        this.fik = fik;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getDat_prij() {
        return dat_prij;
    }

    public void setDat_prij(String dat_prij) {
        this.dat_prij = dat_prij;
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

    public String getDan1() {
        return dan1;
    }

    public void setDan1(String dan1) {
        this.dan1 = dan1;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
