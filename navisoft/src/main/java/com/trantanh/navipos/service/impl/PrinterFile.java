package com.trantanh.navipos.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.trantanh.navipos.dao.PersonDao;
import com.trantanh.navipos.dao.impl.PersonDaoImpl;
import com.trantanh.navipos.model.Person;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class PrinterFile {

    private BufferedWriter bw;
    private String name;
    private String street;
    private String code;
    private String city;
    private String dic;
    private String ico;
    private String nameShop;
    private PersonDao personDao;

    public PrinterFile() {
        try {
            bw = new BufferedWriter(new FileWriter("file.txt"));
            personDao = new PersonDaoImpl();
            Person person = personDao.getPerson();
            this.nameShop = person.getShop();
            this.name = person.getFirstName() + " " + person.getLastName();
            this.street = person.getStreet();
            this.code = person.getPostalCode();
            this.city = person.getCity();
            this.dic = person.getDic();
            this.ico = person.getIco();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PrinterFile(String nameShop, String name, String street, String code, String city, String dic, String ico) {
        try {
            this.nameShop = nameShop;
            this.name = name;
            this.street = street;
            this.code = code;
            this.city = city;
            this.dic = dic;
            this.ico = ico;
            bw = new BufferedWriter(new FileWriter("file.txt"));
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getHeadPrinter() {
        try {
            bw.write(String.format("%-15s %5s %10s\n", "Polozka", "Mnozstvi", "Cena"));
            bw.write(String.format("%-15s %5s %10s\n", "----", "---", "-----"));
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printProduct(String name, String quantity, String price) {
        try {
            bw.write(String.format("%-15.15s %5s %10.15s\n", name, quantity, price));
//        bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getMoney(String money) {
        try {
            bw.write(String.format("%-15.15s %5s %10.15s\n", "Prijate penez: ", "", money));
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeFile() {
        try {
            bw.flush();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
            String s;
            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }
        } catch (Exception e) {
            System.err.println("Chyba při četení ze souboru.");
        }
    }

    public void getHeadTitle() {
        try {
            bw.write(String.format("%-10s %5s %10s", "", nameShop, ""));
            bw.newLine();
            bw.write(String.format("%-10s %5s %10s", "", name, ""));
            bw.newLine();
            bw.write(String.format("%-10s %5s %10s", "", street, ""));
            bw.newLine();
            bw.write(String.format("%-10s %5s %10s", "", code + " " + city, ""));
            bw.newLine();
            bw.write(String.format("%-10s %5s %10s", "", "DIC:" + dic, ""));
            bw.newLine();
            bw.write(String.format("%-10s %5s %10s", "", "ICO:" + ico, ""));
            bw.newLine();
            bw.write("-----------------------------------");
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void totalPrice(String price) {
        try {
            bw.write("==================================");
            bw.newLine();
            bw.write(String.format("%-15.15s %5s %10.15s\n", "Celkova cena", "", price));
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void returnMoney(String price) {
        try {
            bw.write("==================================");
            bw.newLine();
            bw.write(String.format("%-15.15s %5s %10.15s\n", "Vratit penez", "", price));
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getDateTime(String date) {
        try {
            bw.write(date);
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getDate(String date) {
        try {
            bw.write(date);
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getTime(String time) {
        try {
            bw.write(time);
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getIdReceipt(String number) {
        try {
            bw.write("Cislo uctenky:" + number);
            bw.newLine();
            bw.write("==================================");
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void thankU(String message) {
        try {
            bw.write(message);
            bw.newLine();
            bw.write("==================================");
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getFik(String fik) throws IOException {
        bw.write(fik);
        bw.newLine();
        bw.write("==================================");
        bw.newLine();
    }

    public void getBkp(String bkp) {
        try {
            bw.write(bkp);
            bw.newLine();
            bw.write("==================================");
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getText(String text) {
        try {
            bw.write(text);
            bw.newLine();
            bw.write("==================================");
            bw.newLine();
        } catch (IOException ex) {
            Logger.getLogger(PrinterFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws IOException {

        String nameShop = String.format("%1$15s", "Ovoce a Zelenina");
        String name = "Tran Van Dien";
        String street = "Konevova 171";
        String code = "130 00";
        String city = "Praha";
        String dic = "CZ123123321";
        String ico = "2321312321";
        PrinterFile p;
        p = new PrinterFile(nameShop, name, street, code, city, dic, ico);
        p.getHeadTitle();
        p.getHeadPrinter();
        p.printProduct("Voda", "3", "3232.32");
        p.printProduct("Voda2", "3", "32.32");
        p.printProduct("Voda3", "3", "32.32");
        p.printProduct("Voda4", "3", "32.32");
        p.printProduct("Voda5", "3", "32.2");
        p.totalPrice("3900.00");
        p.returnMoney("465456.000");

        p.getIdReceipt("321312321123sxx");

      //  p.getDateTime();
        p.thankU("Dekujme za nakup");
        p.getFik("FIK: asdasdasdsdas");
        p.getBkp("BKP: asdsadsasd");

        p.closeFile();
        p.readFile();
    }

}
