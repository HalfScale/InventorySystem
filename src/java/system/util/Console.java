/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Muffin
 */
public class Console {
    private static boolean isEnabled = true;
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public static void log (Object... o) {
        
        if (isEnabled) {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            
//            for(StackTraceElement elem: stacktrace) {
//                System.out.println(elem.getClassName());
//            }
//            
            int length = stacktrace.length - 1; // this is where the last call for a method in the stack
            
            StringBuilder sb = new StringBuilder();
            
            for(Object obj: o) {
                sb.append(obj + " ");
            }
            
            System.out.println(date.format(formatter) + " " + stacktrace[2] + "-> " + sb.toString());
        }
         
    }

    public static void setIsEnabled(boolean isEnabled) {
        Console.isEnabled = isEnabled;
    }
}
