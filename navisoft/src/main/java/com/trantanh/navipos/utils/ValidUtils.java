package com.trantanh.navipos.utils;

/**
 * @author Tran Tuan Anh, tran.tuan.anh@gem.cz
 * 07.12.2019
 */
public class ValidUtils {

    private ValidUtils() {
    }

    public static boolean validInput(String name, String price) {
        String errorMessage = "";
        if (name == null || name.length() == 0) {
            errorMessage += "Zadejte prosím název produktu \n";
        }
        if (price == null || price.length() == 0) {
            errorMessage += "Zadejte prosím cenu produktu \n";
        } else {
            boolean isNumeric = price.chars().allMatch(Character::isDigit);
            if (!isNumeric) {
                errorMessage += "Cena produktu musí být čísla \n";
            }
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            AlertDialogUtils.getWarning("Neplatná data", "Prosím vyplňte ve správném tvaru", errorMessage);
            return false;
        }
    }
}
