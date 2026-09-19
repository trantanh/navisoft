package com.trantanh.navipos.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class Statistics implements Comparable<Statistics> {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty total_price;
    private final SimpleStringProperty created;

    private final ReadOnlyIntegerWrapper idCounter = new ReadOnlyIntegerWrapper(this, "id", idSequence.incrementAndGet());
    private static AtomicInteger idSequence = new AtomicInteger(0);

    public Statistics(int id, String total_price, String created) {
        this.id = new SimpleIntegerProperty(id);
        this.total_price = new SimpleStringProperty(total_price);
        this.created = new SimpleStringProperty(created);
    }

    public int getId() {
        return id.get();
    }

    public String getTotal_price() {
        return total_price.get();
    }

    public String getCreated() {
        return created.get();
    }

    public final int getIdCounter() {
        return idCounter.get();
    }

    public final ReadOnlyIntegerProperty IdProperty() {
        return idCounter.getReadOnlyProperty();
    }

    @Override
    public int compareTo(Statistics t) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date date = formatter.parse(t.getCreated());
            return formatter.parse(getCreated()).compareTo(date);
        } catch (ParseException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
