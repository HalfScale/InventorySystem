/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Muffin
 */
public class Util {
    
    public boolean checkSession () {
    
    }
    
    //<editor-fold defaultstate="collapsed" desc="Utilities for dates">
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.UTC);
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    public static String dateFormat(Date date, ZoneOffset zone, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        if (zone != null) {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        return sdf.format(date);

    }
    //</editor-fold>
    
}
