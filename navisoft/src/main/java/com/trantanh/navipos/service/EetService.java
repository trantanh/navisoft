package com.trantanh.navipos.service;

import com.trantanh.navipos.model.EetRequestModel;

/**
 *
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public interface EetService {

    void offLineEET();

    EetRequestModel getEetRequestModel(boolean redundance, String totalPrice, int porad_cis, String zakl_dan1, String dan1, String zaklad_dan2, String dan2);
}
