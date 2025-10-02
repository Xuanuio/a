package UDP;

import java.io.*;
import java.util.*;

public class Product implements Serializable {
    private static final long serialVersionUID = 20161107;
    private String id;
    private String code;
    private String name;
    private int quantity; 

    public Product() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", code=" + code + ", name=" + name + ", quantity=" + quantity + '}';
    }

}
