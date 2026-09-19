package com.trantanh.navipos.utils;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public class PrintTextFile {

    public PrintTextFile() {
    }

    public static void openCashDriwer() {
        DocPrintJob job = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
        byte[] bytes = {27, 112, 48, 55, 121};
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        try {
            job.print(doc, null);
        } catch (PrintException ex) {
            Logger.getLogger(Class.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cutPapir() {
        try {
            DocPrintJob job = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
            byte[] bytes = {27, 109, 0};
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(bytes, flavor, null);
            job.print(doc, null);
        } catch (PrintException ex) {
            Logger.getLogger(PrintTextFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void feedPaper() {
        try {
            DocPrintJob job = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
            byte[] bytes = {27, 100, 3};
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(bytes, flavor, null);
            job.print(doc, null);
        } catch (PrintException ex) {
            Logger.getLogger(PrintTextFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void print() {
        try {
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            FileInputStream in = new FileInputStream(new File("file.txt"));

            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(new Copies(1));

            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(in, flavor, null);

            DocPrintJob job = service.createPrintJob();
            PrintJobWatcher pjw = new PrintJobWatcher(job);
            job.print(doc, pras);
            pjw.waitForDone();
            in.close();

            // send FF to eject the page
            InputStream ff = new ByteArrayInputStream("\f".getBytes());
            Doc docff = new SimpleDoc(ff, flavor, null);
            DocPrintJob jobff = service.createPrintJob();
            pjw = new PrintJobWatcher(jobff);
            jobff.print(docff, null);
            pjw.waitForDone();
        } catch (PrintException | IOException ex) {
            Logger.getLogger(PrintTextFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void printToVFDCustomerDisplay(String line, String line2) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("COM4 : Prolific PL2303GC USB Serial COM Port");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (outputStream != null) {
            PrintStream ps = new PrintStream(outputStream);
            ps.flush();
            ps.print("\f");
            ps.print(line);
            ps.print("\r\n");
            ps.print(line2);
            ps.close();
        }
    }

    public static void main(String[] args) {

        String productPrice = "129.00Kc";
        String productName = "Bozkov Rum 0.5L";
        if (productName.length() > 10) {
            productName = productName.substring(0, 7) + "...";
        }
        System.out.println("Product price length:" + productPrice.length());
        System.out.println("Product Name length : " + productName.length());
        //  printToVFDCustomerDisplay("Bozkov Rum 0.5L 129.00Kc", "Celkem 130 Kc");
        printToVFDCustomerDisplay(productName + productPrice, "Celkem 130 Kc");
    }
}
