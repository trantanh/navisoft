package com.trantanh.eet.impl;

import com.trantanh.eet.EetClientService;
import com.trantanh.eet.model.RequestEet;
import com.trantanh.eet.model.ResponseEet;

/**
 *
 * @author Tran Tuan Anh tran.t.anh@email.cz
 */
public class EeClientServiceImpl implements EetClientService {

    @Override
    public ResponseEet sendEet(RequestEet requestEet) {
        EETClient eetClient = new EETClient(requestEet.getPrice(), requestEet.getDate(),requestEet.getPorad_cis(), requestEet.getZakl_dan1(), requestEet.getDan1(), requestEet.getZakl_dan2(),requestEet.getDan2());
        eetClient.data();
        String fik = eetClient.getFik();
        String bkp = eetClient.getBkp();
        String pkp = eetClient.getBkp();
        ResponseEet responseEet = new ResponseEet();
        responseEet.setFik(fik);
        requestEet.setBkp(bkp);
        requestEet.setPkp(pkp);
        return responseEet;
    }
}
