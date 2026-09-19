package com.trantanh.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public final class Database {

    private static final String IP = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = " pricetags";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final String PATH = "D:\\pricsetags.sql";
    private static final String MYSQ_PATH = "D:\\xampp\\mysql\\bin\\mysqldump ";

    public static void export(String path, String mysql) {
        try {
            String dumpCommand = mysql + DATABASE + " -h " + IP + " -u " + USER;
            Runtime rt = Runtime.getRuntime();
            File test = new File(path);
            PrintStream ps;
            Process child = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", dumpCommand});
            ps = new PrintStream(test);
            InputStream in = child.getInputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                ps.write(ch);
                System.out.write(ch); //to view it by console
            }
            InputStream err = child.getErrorStream();
            while ((ch = err.read()) != -1) {
                System.out.write(ch);
            }
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
