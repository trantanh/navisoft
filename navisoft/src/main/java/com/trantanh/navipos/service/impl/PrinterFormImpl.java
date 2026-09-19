package com.trantanh.navipos.service.impl;

import com.trantanh.navipos.dao.impl.BillDaoImpl;
import com.trantanh.navipos.dao.impl.EetDaoImpl;
import com.trantanh.navipos.model.Bill;
import com.trantanh.navipos.model.Product;
import com.trantanh.navipos.service.PrinterForm;
import com.trantanh.navipos.utils.PrintTextFile;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class PrinterFormImpl implements PrinterForm {

    private String id;
    ObservableList<Product> data;
    private String fik;
    private String bkp;
    private String pkp;
    private String totalPrice;
    private String payPrice;
    private String returnMoney;
    private EetDaoImpl eetDataAccessor;
    private BillDaoImpl billDataAccessor;
    private int poradiCisel;
    private String pokladna;
    private Bill bill;
    private PrinterFile printerFile = new PrinterFile();;

    private String dateTime;

    private String zaklad_dan2;
    private String dan2; // DPH 15
    private String zaklad_dan1;
    private String dan1; // DPH 21


    public PrinterFormImpl() {
    }

    public PrinterFormImpl(String id, ObservableList<Product> data, String date, String fik, String bkp, String pkp, String totalPrice, String payPrice, String returnMoney, String zaklad_dan2, String dan2, String zaklad_dan1, String dan1) {
        this.id = id;
        this.data = data;
        this.dateTime = date;
        this.fik = fik;
        this.bkp = bkp;
        this.pkp = pkp;
        this.totalPrice = totalPrice;
        this.payPrice = payPrice;
        this.returnMoney = returnMoney;
        if (zaklad_dan2 != null) {
            this.zaklad_dan2 = zaklad_dan2;
            this.dan2 = dan2;
            this.zaklad_dan1 = zaklad_dan1;
            this.dan1 = dan1;
        } else {
            this.zaklad_dan2 = "0.00";
            this.dan2 = "0.00";
            this.zaklad_dan1 = "0.00";
            this.dan1 = "0.00";
        }
        eetDataAccessor = new EetDaoImpl();
        billDataAccessor = new BillDaoImpl();
        this.pokladna = eetDataAccessor.getEet().getPokl();
        bill = billDataAccessor.getBill(id);
        this.poradiCisel = billDataAccessor.getPoradCisel();
    }

    @Override
    public void printBillWithoutTax() {
        try {
            printerFile.getHeadTitle();
            printerFile.getHeadPrinter();
            if (!data.isEmpty()) {
                for (Product product : data) {
                    printerFile.printProduct(product.getName(), product.getQuantity(), product.getPrice());
                }
                printerFile.totalPrice(totalPrice);
                printerFile.getMoney(payPrice);
                printerFile.returnMoney(returnMoney);
                printerFile.getDateTime(dateTime);

                printerFile.thankU("Dekujeme za nakup");
                if (!fik.isEmpty()) {
                    printerFile.getBkp("BKP");
                    printerFile.getText(bkp);
                    if (!fik.isEmpty()) {
                        printerFile.getFik("FIK");
                        printerFile.getText(fik);
                    } else {
                        printerFile.getText("PKP: ");
                        printerFile.getText(pkp);
                    }
                    printerFile.getText("Rezim EET: Bezny");
                }
                printerFile.getText("Pokladna:" + pokladna);
                printerFile.getText("Poradove cislo uctenky: " + poradiCisel);
            }
            printFile();

        } catch (IOException ex) {
            Logger.getLogger(PrinterFormImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void printBillWithTax() {
        try {
            printerFile.getHeadTitle();
            printerFile.getHeadPrinter();
            if (!data.isEmpty()) {
                for (Product product : data) {
                    printerFile.printProduct(product.getName(), product.getQuantity(), product.getPrice());
                }
                printerFile.totalPrice(totalPrice);
                printerFile.getMoney(payPrice);
                printerFile.returnMoney(returnMoney);
                printerFile.getDateTime(dateTime);
                printerFile.getText("DPH - Hruby obrat " + totalPrice);
                printerFile.getText("21% DPH 1 " + dan1);
                printerFile.getText("Cisty obrat " + zaklad_dan1);
                printerFile.getText("12% DPH 2 " + dan2);
                printerFile.getText("Cisty obrat " + zaklad_dan2);
                printerFile.thankU("Dekujeme za nakup");
                if (!fik.isEmpty()) {
                    printerFile.getBkp("BKP");
                    printerFile.getText(bkp);
                    if (!fik.isEmpty()) {
                        printerFile.getFik("FIK");
                        printerFile.getText(fik);
                    } else {
                        printerFile.getText("PKP: ");
                        printerFile.getText(pkp);
                    }
                    printerFile.getText("Rezim EET: Bezny");
                }
                printerFile.getText("Pokladna:" + pokladna);
                printerFile.getText("Poradove cislo uctenky:" + this.poradiCisel);
            }
            printFile();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFormImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void printBillWithoutConnecting() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printDaySales(String dan1, String zaklad_dan1, String dan2, String zaklad_dan2, String totalPrice, String date, String payByCard) {
        printerFile.getHeadTitle();
        printerFile.getText("21% DPH 1 " + dan1);
        printerFile.getText("Cisty obrat " + zaklad_dan1);
        printerFile.getText("12% DPH 2 " + dan2);
        printerFile.getText("Cisty obrat " + zaklad_dan2);
        printerFile.getText("Karta :" + payByCard);
        printerFile.totalPrice(totalPrice);
        printerFile.getText("Dne: " + date);
        printFile();
    }

    @Override
    public void printMonthSales() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printBill() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void printFile() {
        PrintTextFile.printReceipt(printerFile.getContent());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObservableList<Product> getData() {
        return data;
    }

    public void setData(ObservableList<Product> data) {
        this.data = data;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }

    public EetDaoImpl getEetDataAccessor() {
        return eetDataAccessor;
    }

    public void setEetDataAccessor(EetDaoImpl eetDataAccessor) {
        this.eetDataAccessor = eetDataAccessor;
    }

    public BillDaoImpl getBillDataAccessor() {
        return billDataAccessor;
    }

    public void setBillDataAccessor(BillDaoImpl billDataAccessor) {
        this.billDataAccessor = billDataAccessor;
    }

}
