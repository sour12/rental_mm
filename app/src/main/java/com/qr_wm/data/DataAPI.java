package com.qr_wm.data;

import com.qr_wm.data.db.BoardDB;

public class DataAPI {
    public static String getSetName(String name, String number) {
        return name + "_#" + number;
    }
    public static String[] splitScanData(String contents) {
        return contents.trim().split(";");
    }

    public static boolean checkScanData(String[] data) {
        if (data.length != 3) {
            return false;
        }
        try {
            Integer.parseInt(data[2]);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if (data[0].toUpperCase().equals(BoardDB.PREFIX_BOARD) || data[0].toUpperCase().equals("DEV")) {
            return true;
        } else {
            return false;
        }
    }

}
