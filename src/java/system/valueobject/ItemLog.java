/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system.valueobject;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Muffin
 */
public class ItemLog {
    private int id = -1;
    private int itemId = -1;
    private String name = "";
    private String code = "";
    private String description = "";
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal resellerPrice = BigDecimal.ZERO;
    private int stock  = 0;
    private String action = "";
    private Date datetime = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getResellerPrice() {
        return resellerPrice;
    }

    public void setResellerPrice(BigDecimal resellerPrice) {
        this.resellerPrice = resellerPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
    
}
