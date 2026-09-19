package com.trantanh.navipos.utils;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sends receipt text and ESC/POS commands directly to the printer.
 *
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public final class PrintTextFile {

    private static final Logger LOGGER = Logger.getLogger(PrintTextFile.class.getName());
    private static final Charset PRINTER_CHARSET = Charset.forName("IBM852");
    private static final byte[] INITIALIZE = {27, 64};
    private static final byte[] SELECT_CP852 = {27, 116, 18};
    private static final byte[] FEED_THREE_LINES = {27, 100, 3};
    private static final byte[] CUT_PAPER = {27, 109, 0};
    private static final byte[] OPEN_CASH_DRAWER = {27, 112, 48, 55, 121};

    private PrintTextFile() {
    }

    public static void printReceipt(String receiptText) {
        submit(receiptPayload(receiptText), true);
    }

    public static void openCashDriwer() {
        submit(OPEN_CASH_DRAWER, false);
    }

    public static void cutPapir() {
        submit(CUT_PAPER, false);
    }

    public static void feedPaper() {
        submit(FEED_THREE_LINES, false);
    }

    static byte[] receiptPayload(String receiptText) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.writeBytes(INITIALIZE);
        output.writeBytes(SELECT_CP852);
        output.writeBytes(receiptText.getBytes(PRINTER_CHARSET));
        output.writeBytes(FEED_THREE_LINES);
        output.writeBytes(CUT_PAPER);
        output.writeBytes(OPEN_CASH_DRAWER);
        return output.toByteArray();
    }

    private static void submit(byte[] payload, boolean waitForCompletion) {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        if (service == null) {
            LOGGER.severe("Není nastavena výchozí tiskárna účtenek");
            return;
        }

        DocPrintJob job = service.createPrintJob();
        Doc doc = new SimpleDoc(payload, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
        try {
            if (waitForCompletion) {
                PrintJobWatcher watcher = new PrintJobWatcher(job);
                job.print(doc, null);
                watcher.waitForDone();
            } else {
                job.print(doc, null);
            }
        } catch (PrintException exception) {
            LOGGER.log(Level.SEVERE, "Tisková úloha selhala", exception);
        }
    }

    public static void printToVFDCustomerDisplay(String line, String line2) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("COM4 : Prolific PL2303GC USB Serial COM Port");
        } catch (FileNotFoundException exception) {
            LOGGER.log(Level.SEVERE, "Nelze otevřít zákaznický displej", exception);
        }
        if (outputStream != null) {
            try (PrintStream printStream = new PrintStream(outputStream, true, PRINTER_CHARSET)) {
                printStream.print("\f");
                printStream.print(line);
                printStream.print("\r\n");
                printStream.print(line2);
            }
        }
    }
}
