package com.trantanh.eet.impl;

import com.trantanh.navipos.config.ConfigManager;
import com.trantanh.navipos.dao.EetDao;
import com.trantanh.navipos.dao.impl.EetDaoImpl;
import com.trantanh.navipos.model.EetConfigModel;
import com.trantanh.navipos.model.EetRequestModel;
import openeet.lite.EetRegisterRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static openeet.lite.EetRegisterRequest.loadStream;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class EETClient {

    private double price;
    private EetConfigModel eetConfigModel;
    private String bkp;
    private String fik;
    private String pkp;
    private String dat_prij;
    private int porad_cis;
    private String zakl_dan1;
    private String dan1; // 21
    private String zakl_dan2;
    private String dan2; // 15
    private ConfigManager configManager;
    private Date date;

    public EETClient(double price, int porad_cis, Date date) {
        this(price, date, porad_cis, "0.00", "0.00", "0.00", "0.00");
    }

    public EETClient(double price, Date date, int porad_cis, String zakl_dan1, String dan1, String zakl_dan2, String dan2) {
        this.price = price;
        this.date = date;
        this.porad_cis = porad_cis;
        this.zakl_dan1 = zakl_dan1;
        this.dan1 = dan1;
        this.zakl_dan2 = zakl_dan2;
        this.dan2 = dan2;
        EetDao eetDao = new EetDaoImpl();
        this.eetConfigModel = eetDao.getEet();
        this.configManager = new ConfigManager();
    }

    public EETClient(EetRequestModel eetRequestModel) {
        this.price = eetRequestModel.getTotalPrice();
        this.porad_cis = eetRequestModel.getPorad_cis();
        this.zakl_dan1 = eetRequestModel.getZakl_dan1();
        this.dan1 = eetRequestModel.getDan1();
        this.zakl_dan2 = eetRequestModel.getZakl_dan2();
        this.dan2 = eetRequestModel.getDan2();
        EetDao eetDao = new EetDaoImpl();
        this.eetConfigModel = eetDao.getEet();
        this.configManager = new ConfigManager();
    }

    public EetRegisterRequest data() {
//        InputStream is = null;
//        try {
//            String path = eetConfigModel.getPath();
//            is = new FileInputStream(path);
//            if (configManager.getTax().equals("1")) {
//                return tax(is);
//            } else {
//                return withoutTax(is);
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(EETClient.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(EETClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return null;
    }

    public EetRegisterRequest tax(InputStream is) throws IOException {
        EetRegisterRequest request = EetRegisterRequest.builder()
                .dic_popl(eetConfigModel.getDic())
                .id_provoz(eetConfigModel.getProvoz())
                .id_pokl(eetConfigModel.getPokl())
                .porad_cis("" + porad_cis)
                .dat_trzby(EetRegisterRequest.formatDate(date))
                .celk_trzba(price)
                .dan1(dan1)
                .zakl_dan1(zakl_dan1)
                .dan2(dan2)
                .zakl_dan2(zakl_dan2)
                .rezim(0)
                .pkcs12(loadStream(is))
                .pkcs12password(eetConfigModel.getPassword())
                .build();
        return request;
    }

    public EetRegisterRequest withoutTax(InputStream is) throws IOException {
        EetRegisterRequest request = EetRegisterRequest.builder()
                .dic_popl(eetConfigModel.getDic())
                .id_provoz(eetConfigModel.getProvoz())
                .id_pokl(eetConfigModel.getPokl())
                .porad_cis("" + porad_cis)
                .dat_trzby(EetRegisterRequest.formatDate(new Date()))
                .celk_trzba(price)
                .rezim(0)
                .pkcs12(loadStream(is))
                .pkcs12password(eetConfigModel.getPassword())
                .build();
        return request;
    }

    public EetRegisterRequest sendOfflineDate(InputStream inputStream, String dic, String provoz, String pokl, String porad_cis, Date date, String price, int rezim, String password) {
        EetRegisterRequest eetRegisterRequest = EetRegisterRequest.builder()
                .dic_popl(dic).
                        build();
        return eetRegisterRequest;
    }

    public String getBkp() {
        return data().formatBkp();
    }

    public String getPkp() {
        return data().formatPkp();
    }

    public String getFik() {
        String response = "";
        String requestBody = data().generateSoapRequest();
        try {
            response = data().sendRequest(requestBody, new URL(EndpointType.PRODUCTION.url()));
        } catch (Exception ex) {
            Logger.getLogger(EETClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (response.contains("Potvrzeni fik=")) {
            Result rs = new Result(response);
            dat_prij = rs.getDatPrijat();
            return rs.getFik();
        }
        return "";
    }

    public String getDatPrijat() {
        return dat_prij;
    }
}
