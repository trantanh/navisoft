package com.trantanh.navipos.service.impl;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 *
 * @author tran tuan anh, tran.t.anh@email.cz
 */
public class Printer implements Printable {

    public String text;
    private String name;
    private String price;
    private String unit;
    private String unityPrice;

    public Printer(String text) {
        this.text = text;
    }

    public Printer() {
    }

    public Printer(String name, String price, String unit, String unityPrice) {
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.unityPrice = unityPrice;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public String getUnityPrice() {
        return unityPrice;
    }
    
    public int print(Graphics g, PageFormat pf, int page)
            throws PrinterException {

        // We have only one page, and 'page'
        // is zero-based
        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        // User (0,0) is typically outside the
        // imageable area, so we must translate
        // by the X and Y values in the PageFormat
        // to avoid clipping.
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        g.setFont(new Font("Verdana", Font.ITALIC, 14));
        // Now we perform our rendering
        
        // jmeno
        g.drawString(name, 10, 18);
        // Now we perform our rendering

        
        g.setFont(new Font("Verdana", Font.ITALIC, 14));
    
        //jednotka
        g.drawString(this.getUnit(), 10, 50);

        g.setFont(new Font("Verdana", Font.BOLD, 24));
        
        //cena
        g.drawString(this.getPrice(), 70, 50);

        g.setFont(new Font("Verdana", Font.ITALIC, 14));
        //jednotka v jednotkach
        g.drawString(this.getUnityPrice(), 10, 70);
        // Now we perform our rendering
//        g.drawString("Hello", 10, 40);
//        g.setFont(new Font("Verdana", Font.ITALIC, 1));
//        // Now we perform our rendering
//        g.drawString("Hello", 10, 50);

        // tell the caller that this page is part
        // of the printed document
        return PAGE_EXISTS;
    }

    public static void main(String[] args) throws PrinterException {

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pageFormat = job.defaultPage();
        Paper paper = new Paper();

        paper.setImageableArea(0, 0, pageFormat.getWidth(), pageFormat.getHeight());

        pageFormat.setPaper(paper);
        job.setPrintable(new Printer("Jupi","24Kč" , "14L", "1l = 36Kc"), pageFormat);
        job.print();

    }
}
