package com.trantanh.navipos.service.impl;

import com.trantanh.navipos.model.EetRequestModel;
import com.trantanh.navipos.service.EetService;
import com.trantanh.navipos.utils.DateUtils;

/**
 *
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public class EetServiceImpl implements EetService {
    
    @Override
    public void offLineEET() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public EetRequestModel getEetRequestModel(boolean redundance, String totalPrice, int porad_cis, String zakl_dan1, String dan1, String zaklad_dan2, String dan2) {
        EetRequestModel eetRequestModel;
        if (redundance) {
            eetRequestModel = new EetRequestModel();
            double price = Double.parseDouble(totalPrice);
            price = Math.abs(price);
            eetRequestModel.setTotalPrice(price);
            eetRequestModel.setPorad_cis(porad_cis);
            double zaklDan1 = Double.parseDouble(zakl_dan1);
            zaklDan1 = Math.abs(zaklDan1);
            eetRequestModel.setZakl_dan1(DateUtils.format(zaklDan1));
            double tax1 = Double.parseDouble(dan1);
            tax1 = Math.abs(tax1);
            eetRequestModel.setDan1(DateUtils.format(tax1));
            double zakladDan2 = Double.valueOf(zaklad_dan2);
            zakladDan2 = Math.abs(zakladDan2);
            eetRequestModel.setZakl_dan2(DateUtils.format(zakladDan2));
            double tax2 = Double.valueOf(dan2);
            tax2 = Math.abs(tax2);
            eetRequestModel.setDan2(DateUtils.format(tax2));
        } else {
            eetRequestModel = new EetRequestModel();
            double price = Double.parseDouble(totalPrice);
            eetRequestModel.setTotalPrice(price);
            eetRequestModel.setPorad_cis(porad_cis);
            eetRequestModel.setZakl_dan1(zakl_dan1);
            eetRequestModel.setDan1(dan1);
            eetRequestModel.setZakl_dan2(zaklad_dan2);
            eetRequestModel.setDan2(dan2);
        }
        return eetRequestModel;
    }
    
}
