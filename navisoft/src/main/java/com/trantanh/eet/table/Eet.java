package com.trantanh.eet.table;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class Eet {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty numberBill;
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleStringProperty status;

    public Eet(int id,String numberBill, String date, String time) {
        this(id, numberBill, date, time, "Neodesláno");
    }

    public Eet(int id, String numberBill, String date, String time, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.numberBill = new SimpleStringProperty(numberBill);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.status = new SimpleStringProperty(status);
    }

    public int getId(){
        return id.get();
    }
    public String getNumberBill() {
        return numberBill.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getStatus() {
        return status.get();
    }

}
