package RMI;
import java.rmi.*;
import java.rmi.registry.*;
import java.io.*;
import RMI.ObjectService;
import RMI.ProductX;

public class KhuyenMai {
    public static void main(String[] args) throws Exception {
        Registry rg = LocateRegistry.getRegistry("203.162.10.109", 1099);
        ObjectService sv = (ObjectService) rg.lookup("RMIObjectService");

        String msv = "B22DCCN925", qCode = "";
        ProductX p = (ProductX) sv.requestObject(msv, qCode);

        int sum = 0;
        for (char c : p.getDiscountCode().toCharArray())
            if (Character.isDigit(c)) sum += c - '0';

        p.setDiscount(sum);
        sv.submitObject(msv, qCode, p);
    }
}
