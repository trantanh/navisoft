package com.trantanh.navipos.model;

import java.util.Date;

public class BillDetail {

    private String billNumber;
    private Date date;
    private String returnMoney;
    private String totalPrice;
    private String person;
    private String acceptMoney;
    private String FIK;
    private String BKP;
    private String PKP;
    private int poradCis;
    private String dan1;
    private String zaklDan1;
    private String dan2;
    private String zaklDan2;

    private int payCard;

    public BillDetail(builder builder) {
        this.billNumber = builder.billNumber;
        this.date = builder.date;
        this.returnMoney = builder.returnMoney;
        this.totalPrice = builder.totalPrice;
        this.person = builder.person;
        this.acceptMoney = builder.acceptMoney;
        this.FIK = builder.FIK;
        this.BKP = builder.BKP;
        this.PKP = builder.PKP;
        this.poradCis = builder.poradCis;
        this.dan1 = builder.dan1;
        this.zaklDan1 = builder.zaklDan1;
        this.dan2 = builder.dan2;
        this.zaklDan2 = builder.zaklDan2;
        this.payCard = builder.payCard;
    }

    public static class builder {
        private String billNumber;
        private Date date;
        private String returnMoney;
        private String totalPrice;
        private String person;
        private String acceptMoney;
        private String FIK;
        private String BKP;
        private String PKP;
        private int poradCis;
        private String dan1;
        private String zaklDan1;
        private String dan2;
        private String zaklDan2;

        private int payCard;

        public builder() {

        }

        public builder setBillNumber(String billNumber) {
            this.billNumber = billNumber;
            return this;
        }

        public builder setDate(Date date) {
            this.date = date;
            return this;
        }

        public builder setReturnMoney(String returnMoney) {
            this.returnMoney = returnMoney;
            return this;
        }

        public builder setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public builder setPerson(String person) {
            this.person = person;
            return this;
        }

        public builder setAcceptMoney(String acceptMoney) {
            this.acceptMoney = acceptMoney;
            return this;
        }

        public builder setFIK(String FIK) {
            this.FIK = FIK;
            return this;
        }

        public builder setBKP(String BKP) {
            this.BKP = BKP;
            return this;
        }

        public builder setPKP(String PKP) {
            this.PKP = PKP;
            return this;
        }

        public builder setPoradCis(int poradCis) {
            this.poradCis = poradCis;
            return this;
        }

        public builder setDan1(String dan1) {
            this.dan1 = dan1;
            return this;
        }

        public builder setZaklDan1(String zaklDan1) {
            this.zaklDan1 = zaklDan1;
            return this;
        }

        public builder setDan2(String dan2) {
            this.dan2 = dan2;
            return this;
        }

        public builder setZaklDan2(String zaklDan2) {
            this.zaklDan2 = zaklDan2;
            return this;
        }

        public builder setPayCard(int payCard) {
            this.payCard = payCard;
            return this;
        }

        public BillDetail build() {
            return new BillDetail(this);
        }
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setAcceptMoney(String acceptMoney) {
        this.acceptMoney = acceptMoney;
    }

    public void setFIK(String FIK) {
        this.FIK = FIK;
    }

    public void setBKP(String BKP) {
        this.BKP = BKP;
    }

    public void setPKP(String PKP) {
        this.PKP = PKP;
    }

    public void setPoradCis(int poradCis) {
        this.poradCis = poradCis;
    }

    public void setDan1(String dan1) {
        this.dan1 = dan1;
    }

    public void setZaklDan1(String zaklDan1) {
        this.zaklDan1 = zaklDan1;
    }

    public void setDan2(String dan2) {
        this.dan2 = dan2;
    }

    public void setZaklDan2(String zaklDan2) {
        this.zaklDan2 = zaklDan2;
    }

    public void setPayCard(int payCard) {
        this.payCard = payCard;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public Date getDate() {
        return date;
    }

    public String getReturnMoney() {
        return returnMoney;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getPerson() {
        return person;
    }

    public String getAcceptMoney() {
        return acceptMoney;
    }

    public String getFIK() {
        return FIK;
    }

    public String getBKP() {
        return BKP;
    }

    public String getPKP() {
        return PKP;
    }

    public int getPoradCis() {
        return poradCis;
    }

    public String getDan1() {
        return dan1;
    }

    public String getZaklDan1() {
        return zaklDan1;
    }

    public String getDan2() {
        return dan2;
    }

    public String getZaklDan2() {
        return zaklDan2;
    }

    public int getPayCard() {
        return payCard;
    }
}
