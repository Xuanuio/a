package RMI;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class rmi_obj_sinhvien {

    static int sumDigits(String s) {
        int sum = 0;
        if (s == null) return 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') sum += (c - '0');
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qAlias = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        ObjectService svc = (ObjectService) registry.lookup("RMIObjectService");

        Serializable obj = svc.requestObject(studentCode, qAlias);
        ProductX p = (ProductX) obj;

        int discount = sumDigits(p.getDiscountCode());
        p.setDiscount(discount);

        svc.submitObject(studentCode, qAlias, p);
    }
}
