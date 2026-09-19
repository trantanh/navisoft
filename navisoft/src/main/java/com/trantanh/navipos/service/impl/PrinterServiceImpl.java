package com.trantanh.navipos.service.impl;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.trantanh.navipos.service.PrinterService;
import javafx.print.Printer;
import org.apache.log4j.Logger;

import javax.print.PrintService;
import java.io.IOException;

/**
 * @author Tran Tuan Anh, tran.tuan.anh@gem.cz
 * 01.12.2019
 */
public class PrinterServiceImpl implements PrinterService {

    private final Logger logger = Logger.getLogger(PrinterServiceImpl.class.getName());

    @Override
    public void printPriceTag(String name, String price, String unit, String unitCount) {
        EscPos escpos = getEscPos();
        try {
            writePrice(escpos, name, price, unit, unitCount);
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            escpos.close();
        } catch (IOException e) {
            logger.error("Printer:" + e.getMessage());
        }
    }

    @Override
    public void printPriceTagWithBarcode(String name, String price, String unit, String unitCount, String barcodeText) {
        EscPos escpos = getEscPos();
        BarCode barCode = new BarCode();
        barCode.setBarCodeSize(2, 50);
        Style styleBarcode = new Style().setJustification(EscPosConst.Justification.Left_Default);
        writePrice(escpos, name, price, unit, unitCount);
        try {
            escpos.write(barCode, barcodeText);
            escpos.write(styleBarcode, barcodeText);
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            escpos.close();
        } catch (IOException e) {
            logger.error("Printer:" + e.getMessage());
        }
    }


    private void writePrice(EscPos escPos, String name, String price, String unit, String unitCount) {
        Style styleName = new Style().setFontSize(Style.FontSize._2, Style.FontSize._2);
        Style styleUnit = new Style().setFontSize(Style.FontSize._1, Style.FontSize._1);
        Style stylePrice = new Style().setFontSize(Style.FontSize._3, Style.FontSize._3).setBold(true).setJustification(EscPosConst.Justification.Left_Default);
        try {
            escPos.writeLF(styleName, name);
            escPos.writeLF(stylePrice, price);
            escPos.writeLF(styleUnit, unit + "  ");
            escPos.writeLF(styleUnit, unitCount);
        } catch (IOException e) {
            logger.error("Printer:" + e.getMessage());
        }
    }

    private EscPos getEscPos() {
        javafx.print.Printer defaultprinter = Printer.getDefaultPrinter();
        PrintService printService = PrinterOutputStream.getPrintServiceByName(defaultprinter.getName());
        PrinterOutputStream printerOutputStream = null;
        try {
            printerOutputStream = new PrinterOutputStream(printService);
        } catch (IOException e) {
            logger.error("Printer:" + e.getMessage());
        }
        EscPos escpos = new EscPos(printerOutputStream);
        try {
            escpos.setCharacterCodeTable(EscPos.CharacterCodeTable.CP852_Latin2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return escpos;
    }
}
