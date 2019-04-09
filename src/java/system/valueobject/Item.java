/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.valueobject;

import java.math.BigDecimal;

/**
 *
 * @author Muffin
 */
public class Item {
    
    private int id = -1;
    private String name = "";
    private String code = "";
    private String description = "";
    private String brand = "";
    private String category = "";
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal resellerPrice = BigDecimal.ZERO;
    private int stock = 0;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getResellerPrice() {
        return resellerPrice;
    }

    public void setResellerPrice(BigDecimal reseller_price) {
        this.resellerPrice = reseller_price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    
}
