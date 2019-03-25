/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            close(myConn, myRs, myStmt);
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
            close(myConn, null, myStmt);
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
            close(myConn, myRs, myStmt);
        }
    }
    
    public void close(Connection myConn, ResultSet myRs, PreparedStatement... myStmt) 
        throws Exception{
        
        if(myConn != null) {
            myConn.close();
        }
        
        for(PreparedStatement stmt : myStmt) {
            if(stmt != null) {
                stmt.close();
            }
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
            close(myConn, myRs, myStmt);
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
            close(myConn, null, myStmt);
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
            close(myConn, null, myStmt);
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
            close(myConn, myRs, myStmt);
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
            close(myConn, myRs, myStmt);
        }
        
        return itemArchive;
    }

    public void checkOutItems(JsonArray items) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        PreparedStatement mySecondStmt = null;
        Statement queryStmt = null;
        ResultSet myRs = null;
        
        try {
            myConn = datasource.getConnection();
            String insertToTransac = "insert into transaction (total_amount, total_quantity, timestamp) values(?, ?, ?)";
            myStmt = myConn.prepareStatement(insertToTransac, Statement.RETURN_GENERATED_KEYS);
            
            //Get the total quantity and total amount of all the items
            int totalQuantity = 0;
            BigDecimal totalAmount = BigDecimal.ZERO;
            for(JsonElement item : items) {
                int quantity = item.getAsJsonObject().get("quantity").getAsInt();
                BigDecimal total = item.getAsJsonObject().get("total").getAsBigDecimal();
                
                totalQuantity += quantity;
                totalAmount = totalAmount.add(total);
            }
            
            myStmt.setBigDecimal(1, totalAmount);
            myStmt.setInt(2, totalQuantity);
            myStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            
            //We need to get the id of the last inserted row
            //in the transaction table to use it on another table
            int affectedRows = myStmt.executeUpdate();
            int insertedId = -1;
            
            if (affectedRows == 0) {
                throw new SQLException("No rows affected.");
            }
            
            try (ResultSet generatedKeys = myStmt.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    insertedId = generatedKeys.getInt(1);
                    System.out.println("insertedId" + insertedId);
                }else {
                    throw new SQLException("No id obtained!");
                }
            }
            
            System.out.println("inserted a transaction row!");
            System.out.println("last inserted id " + insertedId);
            
            String insertToTransacDetails = "insert into transaction_detail (transaction_id, item_id, quantity, total_amount) values (?, ?, ?, ?)";
            mySecondStmt = myConn.prepareStatement(insertToTransacDetails);
            
            //Inserting transaction's details to transaction_detail table
            myConn.setAutoCommit(false);
            for(JsonElement item : items) {
                int quantity = item.getAsJsonObject().get("quantity").getAsInt();
                BigDecimal total = item.getAsJsonObject().get("total").getAsBigDecimal();
                int id = item.getAsJsonObject().get("id").getAsInt();
                
                mySecondStmt.setInt(1, insertedId);
                mySecondStmt.setInt(2, id);
                mySecondStmt.setInt(3, quantity);
                mySecondStmt.setBigDecimal(4, total);
                mySecondStmt.addBatch();
            }
            
            //Commit the inserted row after the loop is finished
            mySecondStmt.executeBatch();
            myConn.commit();
            myConn.setAutoCommit(true);
            
            //This query is for updating the stocks of the item
            //after the checkout
            for(JsonElement item : items) {
                int id = item.getAsJsonObject().get("id").getAsInt();
                int quantity = item.getAsJsonObject().get("quantity").getAsInt();
                
                queryStmt = myConn.createStatement();
                queryStmt = myConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                myRs = queryStmt.executeQuery("select * from item");
                
                while(myRs.next()) {
                    int itemId = myRs.getInt("id");
                    int stock = myRs.getInt("stock");
                    
                    if (itemId == id) {
                        myRs.updateInt("stock", stock - quantity);
                        myRs.updateRow();
                        break;
                    }
                }
                
            }
            
            System.out.println("Updating the items successful!");
        
        }finally {
            close(myConn, myRs, myStmt, mySecondStmt);
            
            //addidtional statement
            if (queryStmt != null) {
                queryStmt.close();
            }
        }
    }
    
}
