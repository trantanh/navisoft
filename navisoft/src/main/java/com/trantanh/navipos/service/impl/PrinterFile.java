package com.trantanh.navipos.service.impl;

import com.trantanh.navipos.dao.PersonDao;
import com.trantanh.navipos.dao.impl.PersonDaoImpl;
import com.trantanh.navipos.model.Person;

import java.io.IOException;

/**
 * Builds the printable receipt text in memory.
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class PrinterFile {

    private final StringBuilder content = new StringBuilder();
    private String name;
    private String street;
    private String code;
    private String city;
    private String dic;
    private String ico;
    private String nameShop;

    public PrinterFile() {
        PersonDao personDao = new PersonDaoImpl();
        Person person = personDao.getPerson();
        this.nameShop = person.getShop();
        this.name = person.getFirstName() + " " + person.getLastName();
        this.street = person.getStreet();
        this.code = person.getPostalCode();
        this.city = person.getCity();
        this.dic = person.getDic();
        this.ico = person.getIco();
    }

    public PrinterFile(String nameShop, String name, String street, String code, String city, String dic, String ico) {
        this.nameShop = nameShop;
        this.name = name;
        this.street = street;
        this.code = code;
        this.city = city;
        this.dic = dic;
        this.ico = ico;
    }

    public void getHeadPrinter() {
        append(String.format("%-15s %5s %10s%n", "Polozka", "Mnozstvi", "Cena"));
        append(String.format("%-15s %5s %10s%n", "----", "---", "-----"));
    }

    public void printProduct(String name, String quantity, String price) {
        append(String.format("%-15.15s %5s %10.15s%n", name, quantity, price));
    }

    public void getMoney(String money) {
        append(String.format("%-15.15s %5s %10.15s%n", "Prijate penez: ", "", money));
    }

    public void getHeadTitle() {
        appendCentered(nameShop);
        appendCentered(name);
        appendCentered(street);
        appendCentered(code + " " + city);
        appendCentered("DIC:" + dic);
        appendCentered("ICO:" + ico);
        appendLine("-----------------------------------");
    }

    public void totalPrice(String price) {
        appendLine("==================================");
        append(String.format("%-15.15s %5s %10.15s%n", "Celkova cena", "", price));
    }

    public void returnMoney(String price) {
        appendLine("==================================");
        append(String.format("%-15.15s %5s %10.15s%n", "Vratit penez", "", price));
    }

    public void getDateTime(String date) {
        appendLine(date);
    }

    public void getDate(String date) {
        appendLine(date);
    }

    public void getTime(String time) {
        appendLine(time);
    }

    public void getIdReceipt(String number) {
        appendLine("Cislo uctenky:" + number);
        appendLine("==================================");
    }

    public void thankU(String message) {
        appendLine(message);
        appendLine("==================================");
    }

    public void getFik(String fik) throws IOException {
        appendLine(fik);
        appendLine("==================================");
    }

    public void getBkp(String bkp) {
        appendLine(bkp);
        appendLine("==================================");
    }

    public void getText(String text) {
        appendLine(text);
        appendLine("==================================");
    }

    public String getContent() {
        return content.toString();
    }

    private void appendCentered(String value) {
        append(String.format("%-10s %5s %10s%n", "", value, ""));
    }

    private void appendLine(String value) {
        append(value);
        content.append('\n');
    }

    private void append(String value) {
        content.append(value);
    }
}
