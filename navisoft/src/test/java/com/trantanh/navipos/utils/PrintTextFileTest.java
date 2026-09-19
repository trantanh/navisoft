package com.trantanh.navipos.utils;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrintTextFileTest {

    @Test
    void createsSingleEscPosPayloadForWholeReceipt() {
        String receipt = "Polozka  Cena\nJablka   39.80\n";

        byte[] payload = PrintTextFile.receiptPayload(receipt);
        byte[] encodedReceipt = receipt.getBytes(Charset.forName("IBM852"));

        assertArrayEquals(new byte[]{27, 64, 27, 116, 18}, Arrays.copyOfRange(payload, 0, 5));
        assertArrayEquals(encodedReceipt, Arrays.copyOfRange(payload, 5, 5 + encodedReceipt.length));
        assertArrayEquals(
                new byte[]{27, 100, 3, 27, 109, 0, 27, 112, 48, 55, 121},
                Arrays.copyOfRange(payload, payload.length - 11, payload.length)
        );
        assertTrue(payload.length > encodedReceipt.length);
        assertEquals(1, countOccurrences(payload, encodedReceipt));
    }

    private int countOccurrences(byte[] source, byte[] value) {
        int count = 0;
        for (int start = 0; start <= source.length - value.length; start++) {
            if (Arrays.equals(value, Arrays.copyOfRange(source, start, start + value.length))) {
                count++;
            }
        }
        return count;
    }
}
