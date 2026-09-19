package com.trantanh.navipos.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public final class DateUtils {

    private static final DateFormat FORMAT_DATE = new SimpleDateFormat("dd.MM.yyyy");
    private static final DateFormat CLASSIC_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat FORMATIME = new SimpleDateFormat("HH:mm:ss");
    private static final DateFormat FORMAT_BILL_ID = new SimpleDateFormat("yyyyMddHHmmss");
    private static final DecimalFormat FORMAT_PRICE = new DecimalFormat("#0.00");
    private static final DateFormat BILL_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  
    public static String formatDate(Timestamp date) {
        return FORMAT_DATE.format(date.getTime());
    }

    public static String formatDate(Date date){
        return FORMAT_DATE.format(date);
    }
    public static String classicFormatDate(Timestamp date) {
        return CLASSIC_FORMAT_DATE.format(date.getTime());
    }

    public static String formatTime(Timestamp date) {
        return FORMATIME.format(date.getTime());
    }

    public static String getBillId() {
        return FORMAT_BILL_ID.format(new Date());
    }

    public static String format(String price) {
        return FORMAT_PRICE.format(price);
    }

    public static String format(double price) {
        return FORMAT_PRICE.format(price);
    }

    public static String billDateFormat(Date date){
        return BILL_FORMAT.format(date);
    }
}
