/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.sql.DataSource;
import system.valueobject.Item;
import system.valueobject.ItemArchive;
import system.valueobject.ItemLog;
import system.valueobject.User;

/**
 *
 * @author Muffin
 */
public class DbUtil {

    private DataSource datasource;

    public DbUtil(DataSource datasource) {
        this.datasource = datasource;
    }
    
    public List<Item> getAllItems() throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        List<Item> items;
        
        try {
            items = new ArrayList<>();
            myConn = datasource.getConnection();
            String query = "select * from item";
            
            myStmt = myConn.prepareStatement(query);
            
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                Item item = new Item();
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                String code = myRs.getString("code");
                String description = myRs.getString("description");
                BigDecimal price = myRs.getBigDecimal("price");
                BigDecimal resellerPrice = myRs.getBigDecimal("reseller_price");
                int stock = myRs.getInt("stock");
                
                System.out.println("item name:" + name);
                
                item.setId(id);
                item.setName(name);
                item.setCode(code);
                item.setDescription(description);
                item.setPrice(price);
                item.setResellerPrice(resellerPrice);
                item.setStock(stock);
                
                items.add(item);
            }
            
            return items;
            
        }finally {
            close(myConn, myStmt, myRs);
        }
    }
    
    public void addItem(Item item) throws Exception{
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        
        try {
            
            myConn = datasource.getConnection();
            String query = "insert into item(name, code, description, price, reseller_price, stock) values(?, ?, ?, ?, ?, ?)";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setString(1, item.getName());
            myStmt.setString(2, item.getCode());
            myStmt.setString(3, item.getDescription());
            myStmt.setBigDecimal(4, item.getPrice());
            myStmt.setBigDecimal(5, item.getResellerPrice());
            myStmt.setInt(6, item.getStock());
            
            myStmt.execute();
        
        }finally{
            close(myConn, myStmt, null);
        }
    }

    public User getUser(String user, String pass) throws Exception{
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        User activeUser = null;
        
        try {
            myConn = datasource.getConnection();
            String query = "select * from user where user like ? and pass like ?";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setString(1, user);
            myStmt.setString(2, pass);
            
            myRs = myStmt.executeQuery();
            
            if (myRs.next()) {
                activeUser = new User();
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                
                activeUser.setId(id);
                activeUser.setName(name);
            }
            
            return activeUser;
        }finally {
            close(myConn, myStmt, myRs);
        }
    }
    
    public void close(Connection myConn, PreparedStatement myStmt, ResultSet myRs) 
        throws Exception{
        
        if(myConn != null) {
            myConn.close();
        }
        
        if(myStmt != null) {
            myStmt.close();
        }
        
        if(myRs != null) {
            myRs.close();
        }
    }

    public Item loadItem(Integer itemId) throws Exception{
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        Item item = null;
        
        try {
            
            myConn = datasource.getConnection();
            String query = "select * from item where id = ?";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, itemId);
            
            myRs = myStmt.executeQuery();
            
            if(myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                String code = myRs.getString("code");
                String description = myRs.getString("description");
                BigDecimal price = myRs.getBigDecimal("price");
                BigDecimal resellerPrice = myRs.getBigDecimal("reseller_price");
                int stock = myRs.getInt("stock");
                
                item = new Item();
                item.setId(id);
                item.setName(name);
                item.setCode(code);
                item.setDescription(description);
                item.setPrice(price);
                item.setResellerPrice(resellerPrice);
                item.setStock(stock);
            }
            
            return item;
        }finally{
            close(myConn, myStmt, myRs);
        }
    }

    public void updateItem(Item item) throws Exception{
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        
        try {
            
            myConn = datasource.getConnection();
            String query = "update item set name = ?, code = ?, description = ?, price = ?, reseller_price = ?, "
                    + "stock = ? where id = ?";
            myStmt = myConn.prepareStatement(query);
            myStmt.setString(1, item.getName());
            myStmt.setString(2, item.getCode());
            myStmt.setString(3, item.getDescription());
            myStmt.setBigDecimal(4, item.getPrice());
            myStmt.setBigDecimal(5, item.getResellerPrice());
            myStmt.setInt(6, item.getStock());
            myStmt.setInt(7, item.getId());
            
            myStmt.execute();
        }finally{
            close(myConn, myStmt, null);
        }
    }

    public void deleteItem(Integer itemId) throws Exception{
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        
        try {
            myConn = datasource.getConnection();
            String query = "delete from item where id = ?";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, itemId);
            
            myStmt.execute();
        }finally {
            close(myConn, myStmt, null);
        }
    }

    public List<ItemLog> viewItemHistory(int targetId) throws Exception{
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        List<ItemLog> itemLogs;
        
        try{
            itemLogs = new ArrayList<>();
            myConn = datasource.getConnection();
            String query = "select * from item_log where item_id = ? order by id desc";

            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, targetId);

            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                int id = myRs.getInt("id");
                int itemId = myRs.getInt("item_id");
                String name = myRs.getString("name");
                String code = myRs.getString("code");
                String description = myRs.getString("description");
                BigDecimal price = myRs.getBigDecimal("price");
                BigDecimal resellerPrice = myRs.getBigDecimal("reseller_price");
                int stock = myRs.getInt("stock");
                Timestamp datetime = myRs.getTimestamp("datetime");
                String action = myRs.getString("action");
                
                //We use LocalDateTime since we need to use TimeZone "UTC"
                //since timestamp doesn't have a timezone, we use LocalDateTime
                
                ItemLog itemLog = new ItemLog();
                itemLog.setId(id);
                itemLog.setItemId(itemId);
                itemLog.setName(name);
                itemLog.setCode(code);
                itemLog.setDescription(description);
                itemLog.setPrice(price);
                itemLog.setResellerPrice(resellerPrice);
                itemLog.setStock(stock);
                itemLog.setAction(action);
                itemLog.setDatetime(datetime);
                itemLogs.add(itemLog);
            }
            
            return itemLogs;
        
        }finally{
            close(myConn, myStmt, myRs);
        }
    }

    public List<ItemArchive> viewItemArchive() throws Exception{
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        List<ItemArchive> itemArchive = null;
        
        try{
            itemArchive = new ArrayList<>();
            myConn = datasource.getConnection();
            String query = "select * from item_archive order by datetime desc";
            
            myStmt = myConn.prepareStatement(query);
            
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                String name = myRs.getString("name");
                String code = myRs.getString("code");
                String description = myRs.getString("description");
                BigDecimal price = myRs.getBigDecimal("price");
                BigDecimal resellerPrice = myRs.getBigDecimal("reseller_price");
                int stock = myRs.getInt("stock");
                String action = myRs.getString("action");
                Timestamp timestamp = myRs.getTimestamp("datetime");
                
                ItemArchive item = new ItemArchive();
                item.setName(name);
                item.setCode(code);
                item.setDescription(description);
                item.setPrice(price);
                item.setResellerPrice(resellerPrice);
                item.setStock(stock);
                item.setAction(action);
                item.setDatetime(timestamp);
                
                itemArchive.add(item);
            }
            
        }finally {
            close(myConn, myStmt, myRs);
        }
        
        return itemArchive;
    }
    
}
