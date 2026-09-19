package com.trantanh.navipos.model;

import java.text.DecimalFormat;
/**
 *
 * @author tuan
 */
public class TaxProduct {

    static final double TAX21 = 0.1735537190082645;
    static final double TAX12 = 0.1304347826086957;
    static final double TAX10 = 0.0909090909090909;

    private double celk_trzba;
    private double celk_trzba2;
    private double celk_trzba3;
    DecimalFormat df;

    private double zakl_dan1;
    private double zakl_dan2;
    private double zakl_dan3;

    private double dan3;
    private double dan2;
    private double dan1;

    public TaxProduct(double celk_trzba, double celk_trzba2, double celk_trzba3) {
        this.celk_trzba = celk_trzba;
        this.celk_trzba2 = celk_trzba2;
        this.celk_trzba3 = celk_trzba3;
        df = new DecimalFormat("####0.00");
    }

    public String getZakl_dan1() {
      
         double value = celk_trzba - (celk_trzba * TAX21);
        return df.format(value);
    }

    public String getZakl_dan2() {
       double value = celk_trzba2 - (celk_trzba2 * TAX12);
        return df.format(value);
    }

    public String getZakl_dan3() {
        double value = celk_trzba3 - (celk_trzba3 * TAX10);
        return df.format(value);
    }

    public String getDan1() {

        
        return df.format(celk_trzba * TAX21);
    }

    public String getDan2() {

        return df.format(celk_trzba2 * TAX12);
    }

    public String getDan3() {
        return df.format(celk_trzba3 * TAX10);
    }

    public static void main(String[] args) {
        TaxProduct product = new TaxProduct(146, 115, 0);
        System.out.println("zakl_dan1: " + product.getZakl_dan1());
        System.out.println("dan1: " + product.getDan1());
        System.out.println("zakl_dan2: " + product.getZakl_dan2());
        System.out.println("dan2: " + product.getDan2());

    }

}
