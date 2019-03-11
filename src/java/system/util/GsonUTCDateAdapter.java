/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Muffin
 */
public class GsonUTCDateAdapter implements JsonSerializer<Date> {
    
    private final DateFormat dateFormat;
    
    public GsonUTCDateAdapter() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        System.out.println("Serialize invoked!");
        return new JsonPrimitive(dateFormat.format(date));
    }
    
}
