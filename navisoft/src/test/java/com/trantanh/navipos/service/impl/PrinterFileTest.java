package com.trantanh.navipos.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrinterFileTest {

    @Test
    void buildsReceiptContentInMemory() {
        PrinterFile receipt = new PrinterFile(
                "Prodejna", "Jan Novak", "Hlavni 1", "110 00", "Praha", "CZ12345678", "12345678");

        receipt.getHeadTitle();
        receipt.getHeadPrinter();
        receipt.printProduct("Jablka", "2", "39.80");
        receipt.totalPrice("79.60");
        receipt.getMoney("100.00");
        receipt.returnMoney("20.40");

        String content = receipt.getContent();
        assertTrue(content.contains("Prodejna"));
        assertTrue(content.contains("Jablka"));
        assertTrue(content.contains("79.60"));
        assertTrue(content.contains("100.00"));
        assertTrue(content.contains("20.40"));
        assertFalse(content.isBlank());
    }
}
