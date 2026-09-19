package com.trantanh.navipos.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tran tuan anh, tran.t.anh@email.cz
 */
public final class ConnectorUtils {

    private ConnectorUtils() {
    }

    public static boolean checkConnection() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(ConnectorUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
