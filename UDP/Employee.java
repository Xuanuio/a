package UDP;

import java.io.*;
import java.util.*;

public class Employee implements Serializable{
    private static final long serialVersionUID = 20261107L;
    private String id , name, hireDate;
    double salary;

    public Employee() {
    }

    public Employee(String id, String name, String hireDate, double salary) {
        this.id = id;
        this.name = name;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHireDate() {
        return hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    
}
