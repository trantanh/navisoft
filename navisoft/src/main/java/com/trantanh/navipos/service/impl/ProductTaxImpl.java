package com.trantanh.navipos.service.impl;

import com.trantanh.navipos.service.ProductTax;
import com.trantanh.navipos.utils.DateUtils;
import com.trantanh.navipos.utils.TaxType;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class ProductTaxImpl implements ProductTax {

    private TaxType taxType;
    private double value;
    public double TAX21 = 21.00 / 121.00; //21/121 
    public double TAX12 = 12.00 / 112.00; // 12/115
    public double TAX10 = 10.00 / 110.00; // 10/110
    
    public ProductTaxImpl(TaxType taxType, double value) {
        this.taxType = taxType;
        this.value = value;
    }
    
    //dan1 - 2
    @Override
    public String getBaseTax() {
       String tax = DateUtils.format(baseTax()).replace(',','.');
       return tax;
    }

    //zakl_dan1 - 2
    @Override
    public String getValueOfTax() {
        double zakl_dan = value - baseTax();
        return DateUtils.format(zakl_dan).replace(',','.');
    }

    private double baseTax(){
        switch (taxType) {
            case TAX21:
                return TAX21 * value;
            case TAX12:
                return TAX12 * value;
            case TAX10:
                return TAX10 * value;
            default:
                return 0.00;
        }
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
}
