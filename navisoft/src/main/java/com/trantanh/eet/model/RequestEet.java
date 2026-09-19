package com.trantanh.eet.model;

import java.util.Date;

/**
 * @author Tran Tuan Anh, tran.tuan.anh@gem.cz
 * 05.01.2020
 */
public class RequestEet {

    private double price;
    private String bkp;
    private String fik;
    private String pkp;
    private String dat_prij;
    private int porad_cis;
    private String zakl_dan1;
    private String dan1; // 21
    private String zakl_dan2;
    private String dan2; // 15
    private Date date;

    public RequestEet(double price, int porad_cis, Date date) {
        this(price, date, porad_cis, "0.00", "0.00", "0.00", "0.00");
    }

    public RequestEet(double price, Date date, int porad_cis, String zakl_dan1, String dan1, String zakl_dan2, String dan2) {
        this.price = price;
        this.porad_cis = porad_cis;
        this.zakl_dan1 = zakl_dan1;
        this.dan1 = dan1;
        this.zakl_dan2 = zakl_dan2;
        this.dan2 = dan2;
        this.date = date;
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

    public String getFik() {
        return fik;
    }

    public void setFik(String fik) {
        this.fik = fik;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
