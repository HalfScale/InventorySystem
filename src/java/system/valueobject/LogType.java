/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.valueobject;

/**
 *
 * @author Muffin
 */
public class LogType {
    public static final int LOGIN = 1;
    public static final int LOGOUT = 2;
    public static final int ADD_ITEM = 3;
    public static final int DELETE_ITEM = 4;
    public static final int UPDATE_ITEM = 5;
    public static final int CHECKOUT_ITEM = 6;
    public static final int ADD_BRAND = 7;
    public static final int ADD_CATEGORY = 8;
    
    private int id = -1;
    private String type = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
