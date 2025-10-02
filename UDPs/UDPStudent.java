package UDP;

import java.io.Serializable;
import java.util.Arrays;

public class Student implements Serializable {
    private static final long serialVersionUID = 20171107;
    private String id;
    private String code ;
    private String name;
    private String email;

    public Student(String id, String code, String name, String email) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
    }

    public Student(String code) {
        this.code = code;
    }

    public static String nor(String s){
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }
    public void fixName(){
        String lc=this.name.toLowerCase().trim();
        String[] rs= Arrays.stream(lc.split("\\s+")).map(Student::nor).toArray(String[]::new);
        this.name=String.join(" ",rs);

        String email=rs[rs.length-1].toLowerCase();
        for(int i=0;i<rs.length-1;i++){
            email+=rs[i].substring(0,1).toLowerCase();
        }
        email+="@ptit.edu.vn";
        this.email=email;
        System.out.println(this.name);
        System.out.println(this.email);

    }


}
