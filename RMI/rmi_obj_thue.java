package RMI;

import java.rmi.Naming;

public class rmi_obj_thue {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        ObjectService svc = (ObjectService) Naming.lookup("rmi://" + host + ":1099/RMIObjectService");
        Product p = (Product) svc.requestObject(studentCode, qCode);

        p.setCode(p.getCode().toUpperCase());
        p.setExportPrice(p.getImportPrice() * 1.2);

        svc.submitObject(studentCode, qCode, p);
    }
}
