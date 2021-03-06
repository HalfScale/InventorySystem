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
    
    //<editor-fold defaultstate="collapsed" desc="Utilities for Strings">
    
    public static String underscoreToCapital(String word) {
        String[] strings = word.split("_");
        
        StringBuilder sb = new StringBuilder();
        
        //We initialize variable i to 1 to avoid the first word.
        for(int i = 1; i < strings.length; i++) {
            String firstLetter = strings[i].substring(0, 1).toUpperCase();
            String newWord = firstLetter + strings[i].substring(1);
            sb.append(newWord);
        }
        
        sb.insert(0, strings[0]); //insert the first word at the first position
        
        return sb.toString();
    }
    
    //</editor-fold>
}
