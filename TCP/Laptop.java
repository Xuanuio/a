package TCP;

import java.io.*;
import java.util.*;

public class Laptop implements Serializable {
    private String code, name;
    private int id, quantity;
    private static final long serialVersionUID = 20150711L;

    public Laptop() {
    }

    public Laptop(String code, String name, int id, int quantity) {
        this.code = code;
        this.name = name;
        this.id = id;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
