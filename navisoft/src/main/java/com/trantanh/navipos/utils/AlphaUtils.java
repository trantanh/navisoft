package com.trantanh.navipos.utils;

/**
 * @author Ivan Tran, tran.tuan.anh@starkysclub.com
 * 07.10.2018
 */
public final class AlphaUtils {

    //zjistit jestli v poli jsou pismena nebo ne
    public static boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }
}
