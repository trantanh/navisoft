package com.trantanh.navipos.service.impl;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.trantanh.navipos.service.PrinterService;
import javafx.print.Printer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.PrintService;
import java.io.IOException;

/**
 * @author Tran Tuan Anh, tran.tuan.anh@gem.cz
 * 01.12.2019
 */
public class PrinterServiceImpl implements PrinterService {

    private final Logger logger = LoggerFactory.getLogger(PrinterServiceImpl.class);

    @Override
    public boolean printPriceTag(String name, String price, String unit, String unitCount) {
        return printTag(name, price, unit, unitCount, null);
    }

    @Override
    public boolean printPriceTagWithBarcode(String name, String price, String unit, String unitCount, String barcodeText) {
        if (barcodeText == null || barcodeText.isBlank()) {
            logger.warn("Nelze vytisknout cenovku: chybí čárový kód");
            return false;
        }
        return printTag(name, price, unit, unitCount, barcodeText);
    }

    private boolean printTag(String name, String price, String unit, String unitCount, String barcodeText) {
        EscPos escpos = null;
        try {
            escpos = getEscPos();
            if (escpos == null) {
                return false;
            }
            writePrice(escpos, name, price, unit, unitCount);
            if (barcodeText != null) {
                BarCode barCode = new BarCode();
                barCode.setBarCodeSize(2, 50);
                Style styleBarcode = new Style().setJustification(EscPosConst.Justification.Left_Default);
                escpos.write(barCode, barcodeText);
                escpos.write(styleBarcode, barcodeText);
            }
            escpos.feed(5);
            escpos.cut(EscPos.CutMode.FULL);
            escpos.close();
            escpos = null;
            return true;
        } catch (IOException | RuntimeException exception) {
            logger.error("Tisk cenovky selhal", exception);
            return false;
        } finally {
            if (escpos != null) {
                try {
                    escpos.close();
                } catch (IOException exception) {
                    logger.warn("Nepodařilo se korektně uzavřít tiskovou úlohu", exception);
                }
            }
        }
    }

    private void writePrice(EscPos escPos, String name, String price, String unit, String unitCount) throws IOException {
        Style styleName = new Style().setFontSize(Style.FontSize._2, Style.FontSize._2);
        Style styleUnit = new Style().setFontSize(Style.FontSize._1, Style.FontSize._1);
        Style stylePrice = new Style().setFontSize(Style.FontSize._3, Style.FontSize._3).setBold(true).setJustification(EscPosConst.Justification.Left_Default);
        escPos.writeLF(styleName, name);
        escPos.writeLF(stylePrice, price);
        escPos.writeLF(styleUnit, unit + "  ");
        escPos.writeLF(styleUnit, unitCount);
    }

    private EscPos getEscPos() {
        javafx.print.Printer defaultprinter = Printer.getDefaultPrinter();
        if (defaultprinter == null) {
            logger.error("Není nastavena výchozí tiskárna cenovek");
            return null;
        }
        PrintService printService = PrinterOutputStream.getPrintServiceByName(defaultprinter.getName());
        if (printService == null) {
            logger.error("Tisková služba '{}' není dostupná", defaultprinter.getName());
            return null;
        }

        PrinterOutputStream printerOutputStream;
        try {
            printerOutputStream = new PrinterOutputStream(printService);
        } catch (IOException | RuntimeException exception) {
            logger.error("Nelze se připojit k tiskárně '{}'", defaultprinter.getName(), exception);
            return null;
        }
        EscPos escpos = new EscPos(printerOutputStream);
        try {
            escpos.setCharacterCodeTable(EscPos.CharacterCodeTable.CP852_Latin2);
        } catch (IOException exception) {
            logger.error("Nelze nastavit znakovou sadu tiskárny", exception);
            try {
                escpos.close();
            } catch (IOException closeException) {
                exception.addSuppressed(closeException);
            }
            return null;
        }
        return escpos;
    }
}
