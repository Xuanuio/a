package UDP;

import java.io.Serializable;

public class Employee implements Serializable {
    private static final long serialVersionUID = 20261107L;

    private String id;
    private String name ;
    private double salary ;
    private String hireDate ;

    public Employee(String name, double salary, String hireDate, String id) {
        this.name = name;
        this.salary = salary;
        this.hireDate = hireDate;
        this.id = id;
    }

    public void fix(){
        String namearr[]=this.name.trim().toLowerCase().split("\\s+");
        String namef="";
        for(int i=0;i<namearr.length;i++){
            namef+=namearr[i].substring(0,1).toUpperCase()+namearr[i].substring(1);
            if(i!= namearr.length-1) namef+=" ";
        }

        this.name=namef;
        int x=0;

        for(int i=0;i<4;i++){
            if(this.hireDate.charAt(i)>='0'&&this.hireDate.charAt(i)<='9'){
                x+=Integer.parseInt(this.hireDate.charAt(i)+"");
            }
        }
        this.salary=this.salary*(1+ (double) x /100);

        String[] date=this.hireDate.split("-");
        String datef=date[2]+"/"+date[1]+"/"+date[0];
        this.hireDate=datef;

        System.out.println(hireDate);
        System.out.println(name);
        System.out.println(salary);


    }
}
