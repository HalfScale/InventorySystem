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
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import system.valueobject.Item;

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
            String query = "select * from items";
            
            myStmt = myConn.prepareStatement(query);
            
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                Item item = new Item();
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                String code = myRs.getString("code");
                String description = myRs.getString("description");
                BigDecimal price = myRs.getBigDecimal("price");
                int stock = myRs.getInt("stock");
                
                System.out.println("item name:" + name);
                
                item.setId(id);
                item.setName(name);
                item.setCode(code);
                item.setDescription(description);
                item.setPrice(price);
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
            String query = "insert into items(name, code, description, price, stock) values(?, ?, ?, ?, ?)";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setString(1, item.getName());
            myStmt.setString(2, item.getCode());
            myStmt.setString(3, item.getDescription());
            myStmt.setBigDecimal(4, item.getPrice());
            myStmt.setInt(5, item.getStock());
            
            myStmt.execute();
        
        }finally{
            close(myConn, myStmt, null);
        }
    }

    public boolean getUser(String user, String pass) throws Exception{
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        boolean userIsFound = false;
        
        try {
            myConn = datasource.getConnection();
            String query = "select * from user where user like ? and pass like ?";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setString(1, user);
            myStmt.setString(2, pass);
            
            myRs = myStmt.executeQuery();
            
            if (myRs.next()) {
                userIsFound = true;
            }
            
            return userIsFound;
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
            String query = "select * from items where id = ?";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, itemId);
            
            myRs = myStmt.executeQuery();
            
            if(myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                String code = myRs.getString("code");
                String description = myRs.getString("description");
                BigDecimal price = myRs.getBigDecimal("price");
                int stock = myRs.getInt("stock");
                
                item = new Item();
                item.setId(id);
                item.setName(name);
                item.setCode(code);
                item.setDescription(description);
                item.setPrice(price);
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
            String query = "update items set name = ? ,code = ?, description = ?, price = ?, stock = ?"
                    + " where id = ?";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setString(1, item.getName());
            myStmt.setString(2, item.getCode());
            myStmt.setString(3, item.getDescription());
            myStmt.setBigDecimal(4, item.getPrice());
            myStmt.setInt(5, item.getStock());
            myStmt.setInt(6, item.getId());
            
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
            String query = "delete from items where id = ?";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, itemId);
            
            myStmt.execute();
        }finally {
            close(myConn, myStmt, null);
        }
    }
    
}
