package UDP;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 20151107;
    private String id;
    private String code ;
    private String name ;
    private String dayOfBirth ;
    private String userName  ;


    public Customer(String id, String code, String name, String dayOfBirth, String userName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.dayOfBirth = dayOfBirth;
        this.userName = userName;
    }
    public void fix(){
        String s=this.name.trim().toLowerCase();
        String[] arr=s.split("\\s+");

        String ok=arr[arr.length-1].toUpperCase()+",";
        String un="";
        for(int i=0;i<arr.length-1;i++){
            ok+= " "+arr[i].substring(0,1).toUpperCase()+arr[i].substring(1);
            un+= arr[i].substring(0,1);
        }
        this.name=ok;
        un+=arr[arr.length-1];
        this.userName=un;
        String[] ss=this.dayOfBirth.split("-");
        String tmp=ss[0];
        ss[0]=ss[1];
        ss[1]=tmp;

        this.dayOfBirth=String.join("/",ss);
        System.out.println(this.name);
        System.out.println(this.userName);
        System.out.println(this.dayOfBirth);
    }
}
