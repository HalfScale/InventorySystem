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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.sql.DataSource;
import system.valueobject.Brand;
import system.valueobject.Category;
import system.valueobject.Item;
import system.valueobject.ItemArchive;
import system.valueobject.ItemLog;
import system.valueobject.LogType;
import system.valueobject.SystemLog;
import system.valueobject.Transaction;
import system.valueobject.TransactionDetail;
import system.valueobject.TransactionType;
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
        List<Item> items = new ArrayList<>();
        
        try {
            
            myConn = datasource.getConnection();
            String query = "select i.id, i.name, i.code, i.description, i.price, i.reseller_price,  "
                    + "i.stock, b.id as brand_id, b.name as brand_name, c.id as category_id, c.name as category_name from item i "
                    + "inner join brand b on i.brand = b.id inner join category c on i.category = c.id;";
            
            myStmt = myConn.prepareStatement(query);
            
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                Item item = new Item();
                Brand brand = new Brand();
                Category category = new Category();
                
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                String code = myRs.getString("code");
                String description = myRs.getString("description");
                BigDecimal price = myRs.getBigDecimal("price");
                BigDecimal resellerPrice = myRs.getBigDecimal("reseller_price");
                int stock = myRs.getInt("stock");
                
                int brandId = myRs.getInt("brand_id");
                String brandName = myRs.getString("brand_name");
                int categoryId = myRs.getInt("category_id");
                String categoryName = myRs.getString("category_name");
                
                brand.setId(brandId);
                brand.setName(brandName);
                category.setId(categoryId);
                category.setName(categoryName);
                
                item.setId(id);
                item.setName(name);
                item.setCode(code);
                item.setBrand(brand);
                item.setCategory(category);
                item.setDescription(description);
                item.setBrand(brand);
                item.setCategory(category);
                item.setPrice(price);
                item.setResellerPrice(resellerPrice);
                item.setStock(stock);
                
                items.add(item);
            }
            
            Console.log("items", items);
            return items;
            
        }finally {
            close(myConn, myRs, myStmt);
        }
    }
    
    public Item getItemById(int targetId) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        Item item = new Item();
        try {
            myConn = datasource.getConnection();
            String query = "select * from item where id = ?";
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, targetId);
            myRs = myStmt.executeQuery();
            
            if(myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                String code = myRs.getString("code");
                int brandId = myRs.getInt("brand");
                int categoryId = myRs.getInt("category");
                String description = myRs.getString("description");
                BigDecimal price = myRs.getBigDecimal("price");
                BigDecimal resellerPrice = myRs.getBigDecimal("reseller_price");
                int stock = myRs.getInt("stock");
                
                Brand brand = getBrandById(brandId);
                Category category = getCategoryById(categoryId);
                
                item.setId(id);
                item.setName(name);
                item.setCode(code);
                item.setBrand(brand);
                item.setCategory(category);
                item.setDescription(description);
                item.setPrice(price);
                item.setResellerPrice(resellerPrice);
                item.setStock(stock);
                
            }
            
        }finally {
            close(myConn, myRs, myStmt);
        }
        
        return item;
    }
    
    public void addItem(Item item) throws Exception{
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        
        try {
            
            myConn = datasource.getConnection();
            myConn.setAutoCommit(false);
            String query = "insert into item(name, code, brand, category, description, price, reseller_price, stock) values(?, ?, ?, ?, ?, ?, ?, ?)";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setString(1, item.getName());
            myStmt.setString(2, item.getCode());
            myStmt.setInt(3, item.getBrand().getId());
            myStmt.setInt(4, item.getCategory().getId());
            myStmt.setString(5, item.getDescription());
            myStmt.setBigDecimal(6, item.getPrice());
            myStmt.setBigDecimal(7, item.getResellerPrice());
            myStmt.setInt(8, item.getStock());
            
            myStmt.execute();
            
            myConn.commit();
        
        }catch(Exception e) {
            e.printStackTrace();
            myConn.rollback();
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
            String query = "select i.id, i.name, i.code, i.description, i.price, i.reseller_price,  "
                    + "i.stock, b.id as brand_id, b.name as brand_name, c.id as category_id, c.name as category_name from item i "
                    + "inner join brand b on i.brand = b.id inner join category c on i.category = c.id "
                    + "where i.id = ?";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, itemId);
            
            myRs = myStmt.executeQuery();
            
            if(myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                String code = myRs.getString("code");
                int brandId = myRs.getInt("brand_id");
                String brandName = myRs.getString("brand_name");
                int categoryId = myRs.getInt("category_id");
                String categoryName = myRs.getString("category_name");
                String description = myRs.getString("description");
                BigDecimal price = myRs.getBigDecimal("price");
                BigDecimal resellerPrice = myRs.getBigDecimal("reseller_price");
                int stock = myRs.getInt("stock");
                
                Brand brand = new Brand();
                Category category = new Category();
                
                brand.setId(brandId);
                brand.setName(brandName);
                
                category.setId(categoryId);
                category.setName(categoryName);
                
                item = new Item();
                item.setId(id);
                item.setName(name);
                item.setCode(code);
                item.setBrand(brand);
                item.setCategory(category);
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
            myConn.setAutoCommit(false);
            
            String query = "update item set name = ?, code = ?, brand = ?, category = ?, description = ?, price = ?, reseller_price = ?, "
                    + "stock = ? where id = ?";
            myStmt = myConn.prepareStatement(query);
            myStmt.setString(1, item.getName());
            myStmt.setString(2, item.getCode());
            myStmt.setInt(3, item.getBrand().getId());
            myStmt.setInt(4, item.getCategory().getId());
            myStmt.setString(5, item.getDescription());
            myStmt.setBigDecimal(6, item.getPrice());
            myStmt.setBigDecimal(7, item.getResellerPrice());
            myStmt.setInt(8, item.getStock());
            myStmt.setInt(9, item.getId());
            
            myStmt.execute();
            
            myConn.commit();
        }catch(Exception e) {
            e.printStackTrace();
            myConn.rollback();
        }finally{
            close(myConn, null, myStmt);
        }
    }

    public void deleteItem(Integer itemId) throws Exception{
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        
        try {
            myConn = datasource.getConnection();
            myConn.setAutoCommit(false);
            String query = "delete from item where id = ?";
            
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, itemId);
            
            myStmt.execute();
            
            myConn.commit();
            
        }catch(Exception e) {
            myConn.rollback();
            e.printStackTrace();
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

    public String checkOutItems(JsonArray items, String type) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        PreparedStatement mySecondStmt = null;
        Statement queryStmt = null;
        ResultSet myRs = null;
        String message = "Checkout unsuccessful.";
        
        try {
            myConn = datasource.getConnection();
            myConn.setAutoCommit(false);
            String insertToTransac = "insert into transaction (type, total_amount, total_quantity, timestamp) values(?, ?, ?, ?)";
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
            
            myStmt.setString(1, type);
            myStmt.setBigDecimal(2, totalAmount);
            myStmt.setInt(3, totalQuantity);
            myStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            
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
                    Console.log("insertedId", insertedId);
                }else {
                    throw new SQLException("No id obtained!");
                }
            }
            
            System.out.println("inserted a transaction row!");
            System.out.println("last inserted id " + insertedId);
            
            String insertToTransacDetails = "insert into transaction_detail (transaction_id, item_id, quantity, total_amount, is_reseller_price) values (?, ?, ?, ?, ?)";
            mySecondStmt = myConn.prepareStatement(insertToTransacDetails);
            
            //Inserting transaction's details to transaction_detail table
            for(JsonElement item : items) {
                int quantity = item.getAsJsonObject().get("quantity").getAsInt();
                BigDecimal total = item.getAsJsonObject().get("total").getAsBigDecimal();
                int id = item.getAsJsonObject().get("id").getAsInt();
                String priceType = item.getAsJsonObject().get("priceType").getAsString();
                
                boolean isResellerPrice = priceType.equalsIgnoreCase("reseller") ? true : false;
                
                mySecondStmt.setInt(1, insertedId);
                mySecondStmt.setInt(2, id);
                mySecondStmt.setInt(3, quantity);
                mySecondStmt.setBigDecimal(4, total);
                mySecondStmt.setBoolean(5, isResellerPrice);
                mySecondStmt.addBatch();
            }
            
            //Commit the inserted row after the loop is finished
            mySecondStmt.executeBatch();
            
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
            
            myConn.commit();
            message = "Checkout successful!";
            Console.log("Updating the items successful!");
        
        }catch(Exception e) {
            e.printStackTrace();
            myConn.rollback();
        }finally {
            close(myConn, myRs, myStmt, mySecondStmt);
            
            //addidtional statement
            if (queryStmt != null) {
                queryStmt.close();
            }
        }
        
        return message;
    }

    public List<LogType> getAllLogTypes() throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        List<LogType> logTypes = new ArrayList<>();
        
        try {
            myConn = datasource.getConnection();
            String query = "select * from log_type";
            
            myStmt = myConn.prepareStatement(query);
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                int id = myRs.getInt("id");
                String type = myRs.getString("type");
                
                LogType logType = new LogType();
                logType.setId(id);
                logType.setType(type);
                
                logTypes.add(logType);
            }
            
        }finally {
            close(myConn, myRs, myStmt);
        }
        
        return logTypes;
    }

    public List<Map> getAllLogs() throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        List<Map> systemLogs = new ArrayList<>();
        
        try {
            
            myConn = datasource.getConnection();
            String query = "select sl.id, sl.log_type, sl.timestamp, sl.user_id, lt.type, "
                    + "u.name from system_log sl inner join log_type lt on sl.log_type = lt.id"
                    + " inner join user u on sl.user_id = u.id order by sl.id desc";
            
            myStmt = myConn.prepareStatement(query);
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                int id = myRs.getInt("id");
                int logType = myRs.getInt("log_type");
                Timestamp timestamp = myRs.getTimestamp("timestamp");
                int userId = myRs.getInt("user_id");
                String type = myRs.getString("type");
                String username = myRs.getString("name");
                
                Map logMap = new HashMap();
                logMap.put("id", id);
                logMap.put("logType", logType);
                logMap.put("timestamp", timestamp);
                logMap.put("userId", userId);
                logMap.put("username", username);
                logMap.put("type", type);
                
                systemLogs.add(logMap);
                
            }
            
        }finally {
            close(myConn, myRs, myStmt);
        }
        
        return systemLogs;
    }

    public void registerLog(int type, User user) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        
        //The JDBC always use the default timezone for dates which is (UTC)
        Calendar calendar = Calendar.getInstance(new Locale("PH"));
        
        try {
            
            myConn = datasource.getConnection();
            myConn.setAutoCommit(false);
            String insert = "insert into system_log (log_type, timestamp, user_id) values(?, ?, ?)";
            myStmt = myConn.prepareStatement(insert);
            myStmt.setInt(1, type);
            System.out.println("timestamp " +  Timestamp.valueOf(LocalDateTime.now()));
            myStmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()), calendar);
            myStmt.setInt(3, user.getId());
            
            
            myStmt.execute();
            myConn.commit();
            System.out.println("Log register sucessful!");
            
        }catch(Exception e) {
            e.printStackTrace();
            myConn.rollback();
        }finally{
            close(myConn, null, myStmt);
        }
    }

    public Map addBrand(String param) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        String message = "Brand insertion unsuccessful!";
        Map response = new HashMap();
        response.put("message", message);
        
        try {
            myConn = datasource.getConnection();
            myConn.setAutoCommit(false);
            String insert = "insert into brand (name) values (?)";
            myStmt = myConn.prepareStatement(insert);
            
            myStmt.setString(1, param);
            int result = myStmt.executeUpdate();
            
            if(result == 1) {
                message = "Brand insertion successful!";
                response.put("message", message);
                response.put("item", param);
            }
            
            myConn.commit();
            
        }catch(Exception e) {
            myConn.rollback();
            throw new Exception(e.getMessage(), e);
        }finally {
            close(myConn, null, myStmt);
        }
        
        return response;
    }

    public List<Brand> listBrand() throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        List<Brand> brands = new ArrayList<>();
        
        try {
            myConn = datasource.getConnection();
            String select = "select * from brand";
            
            myStmt = myConn.prepareStatement(select);
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                
                Brand brand = new Brand();
                brand.setId(id);
                brand.setName(name);
                
                brands.add(brand);
            }
        
        }finally {
            close(myConn, myRs, myStmt);
        }
        
        return brands;
    }
    
    public Brand getBrandById(int targetId) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        Brand brand = new Brand();
        
        try {
            myConn = datasource.getConnection();
            String query = "select * from brand where id = ?";
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, targetId);
            myRs = myStmt.executeQuery();
            
            if(myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                
                brand.setId(id);
                brand.setName(name);
            }
            
        }finally {
            close(myConn, myRs, myStmt);
        }
        
        return brand;
    }

    public List<Category> listCategory() throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        List<Category> categories = new ArrayList<>();
        
        try {
            myConn = datasource.getConnection();
            String query = "select * from category";
            myStmt = myConn.prepareStatement(query);
            
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                
                Category category = new Category();
                category.setId(id);
                category.setName(name);
                
                categories.add(category);
            }
            
        }finally {
            close(myConn, myRs, myStmt);
        }
        
        return categories;
    }

    public Map addCategory(String param) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        String message = "Category insertion unsuccessful!";
        Map response = new HashMap();
        response.put("message", message);
        
        try {
            myConn = datasource.getConnection();
            myConn.setAutoCommit(false);
            String insert = "insert into category (name) values (?)";
            myStmt = myConn.prepareStatement(insert);
            myStmt.setString(1, param);
            int result = myStmt.executeUpdate();
            
            if(result == 1) {
                message = "Category insertion successful!";
                response.put("message", message);
                response.put("item", param);
            }
            
            myConn.commit();
                    
        }catch(Exception e) {
            e.printStackTrace();
            myConn.rollback();
        }finally {
            close(myConn, null, myStmt);
        }
        
        return response;
    }
    
    public Category getCategoryById(int targetId) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        Category category = new Category();
        
        try {
            myConn = datasource.getConnection();
            String query = "select * from category where id = ?";
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, targetId);
            myRs = myStmt.executeQuery();
            
            if(myRs.next()) {
                int id  = myRs.getInt("id");
                String name = myRs.getString("name");
                
                category.setId(id);
                category.setName(name);
            }
            
        }finally {
            close(myConn, myRs, myStmt);
        }
        
        return category;
    }

    public void deleteBrand(Integer id) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        
        try {
            myConn = datasource.getConnection();
            myConn.setAutoCommit(false);
            String delete = "delete from brand where id = ?";
            
            myStmt = myConn.prepareStatement(delete);
            myStmt.setInt(1, id);
            myStmt.executeUpdate();
            
            myConn.commit();
            
        }catch(Exception e) {
            myConn.rollback();
            e.printStackTrace();
            
        }finally {
            close(myConn, null, myStmt);
        }
    }

    public void deleteCategory(Integer id) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            myConn = datasource.getConnection();
            myConn.setAutoCommit(false);
            String delete = "delete from category where id = ?";

            myStmt = myConn.prepareStatement(delete);
            myStmt.setInt(1, id);
            myStmt.executeUpdate();
            
            myConn.commit();

        }catch(Exception e) {
            myConn.rollback();
            e.printStackTrace();
        }finally {
            close(myConn, null, myStmt);
        }
    }

    public List<TransactionType> listTransactionType() throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        
        List<TransactionType> transactionTypes = new ArrayList<>();
        
        try {
            myConn = datasource.getConnection();
            String query = "select * from transaction_type";
            
            myStmt = myConn.prepareStatement(query);
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                
                TransactionType transactionType = new TransactionType();
                transactionType.setId(id);
                transactionType.setName(name);
                
                transactionTypes.add(transactionType);
            }
            
        }finally {
            close(myConn, myRs, myStmt);
        }
        
        return transactionTypes;
    }

    public Map addTransactionType(String param) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        String message = "Transaction type insertion unsuccessful!";
        Map response = new HashMap();
        response.put("message", message);
        
        try {
            myConn = datasource.getConnection();
            myConn.setAutoCommit(false);
            String insert = "insert into transaction_type(name) values (?)";
            
            myStmt = myConn.prepareStatement(insert);
            myStmt.setString(1, param);
            
            int result = myStmt.executeUpdate();
            
            if(result == 1) {
                message = "Transaction type insertion successful!";
                response.put("message", message);
                response.put("item", param);
            }
            
            myConn.commit();
            Console.log("message", message);
        }catch(Exception e) {
            myConn.rollback();
            throw new Exception(e.getMessage(), e);
        }finally {
            close(myConn, null, myStmt);
        }
        
        return response;
    }

    public void deleteTransactionType(int id) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        
        try {
            
            myConn = datasource.getConnection();
            myConn.setAutoCommit(false);
            String delete = "delete from transaction_type where id = ?";
            
            myStmt = myConn.prepareStatement(delete);
            myStmt.setInt(1, id);
            myStmt.executeUpdate();
            
            myConn.commit();
        }catch(Exception e) {
            e.printStackTrace();
            myConn.rollback();
        }finally {
            close(myConn, null, myStmt);
        }
    }
    
    public TransactionType getTransactionTypeById(int targetId) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        TransactionType transactionType = null;
        
        try {
            myConn = datasource.getConnection();
            String query = "select * from transaction_type where id = ?";
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, targetId);
            myRs = myStmt.executeQuery();
            
            if(myRs.next()) {
                transactionType = new TransactionType();
                
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                
                transactionType.setId(id);
                transactionType.setName(name);
            }

        } finally {
            close(myConn, myRs, myStmt);
        }
        
        return transactionType;
    }
    
    public Transaction getTransactionById(int targetId) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        Transaction transaction = null;
        try {
            myConn = datasource.getConnection();
            String query = "select * from transaction where id = ?";
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, targetId);
            myRs = myStmt.executeQuery();
            
            if(myRs.next()) {
                transaction = new Transaction();
                
                int id = myRs.getInt("id");
                int transactionTypeId = myRs.getInt("type");
                BigDecimal totalAmount = myRs.getBigDecimal("total_amount");
                int totalQuantity = myRs.getInt("total_quantity");
                Timestamp timestamp = myRs.getTimestamp("timestamp");
                
                TransactionType transactionType = getTransactionTypeById(transactionTypeId);
                
                transaction.setId(id);
                transaction.setType(transactionType);
                transaction.setTotalAmount(totalAmount);
                transaction.setTotalQuantity(totalQuantity);
                transaction.setTimestamp(timestamp);
            }
            
        }finally {
            close(myConn, myRs, myStmt);
        }
        
        return transaction;
    }

    public List<Transaction> getAllTransactions() throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        List<Transaction> transactions = new ArrayList<>();
        try {
            myConn = datasource.getConnection();
            String query = "select t.id, t.type, t.total_amount, t.total_quantity\n"
                    + ", t.timestamp, tt.id as transaction_type_id, tt.name as transaction_type_name from transaction t \n"
                    + "inner join transaction_type tt\n"
                    + "on t.type = tt.id order by t.id desc";
            
            myStmt = myConn.prepareStatement(query);
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                int transactionId = myRs.getInt("id");
                BigDecimal totalAmounht = myRs.getBigDecimal("total_amount");
                int quantity = myRs.getInt("total_quantity");
                Timestamp timestamp = myRs.getTimestamp("timestamp");
                
                int transactionTypeId = myRs.getInt("transaction_type_id");
                String transactionTypeName = myRs.getString("transaction_type_name");
                
                Transaction transaction = new Transaction();
                transaction.setId(transactionId);
                transaction.setTotalAmount(totalAmounht);
                transaction.setTotalQuantity(quantity);
                transaction.setTimestamp(timestamp);
                
                TransactionType transactionType = new TransactionType();
                transactionType.setId(transactionTypeId);
                transactionType.setName(transactionTypeName);
                
                transaction.setType(transactionType);
                
                transactions.add(transaction);
            }
            
        }finally{
            close(myConn, myRs, myStmt);
        }
        
        return transactions;
    }
    
    public List<TransactionDetail> getTransactionDetailByTransactionId(int targetId) throws Exception{
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        List<TransactionDetail> transactionDetails = new ArrayList<>();
        
        try {
            myConn = datasource.getConnection();
            String query = "select * from transaction_detail where transaction_id = ?";
            myStmt = myConn.prepareStatement(query);
            myStmt.setInt(1, targetId);
            
            myRs = myStmt.executeQuery();
            
            while(myRs.next()) {
                TransactionDetail transactionDetail = new TransactionDetail();
                
                int id = myRs.getInt("id");
                int transactionId = myRs.getInt("transaction_id");
                int itemId = myRs.getInt("item_id");
                int quantity = myRs.getInt("quantity");
                BigDecimal totalAmount = myRs.getBigDecimal("total_amount");
                boolean isResellerPrice = myRs.getBoolean("is_reseller_price");
                
                Item item = getItemById(itemId);
                Transaction transaction = getTransactionById(transactionId);
                
                transactionDetail.setId(id);
                transactionDetail.setTransaction(transaction);
                transactionDetail.setItem(item);
                transactionDetail.setQuantity(quantity);
                transactionDetail.setTotalAmount(totalAmount);
                transactionDetail.setIsResellerPrice(isResellerPrice);
                
                transactionDetails.add(transactionDetail);
            }
            
        }finally {
            close(myConn, myRs, myStmt);
        }
        
        return transactionDetails;
    }
    
}
