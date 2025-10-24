package WS;
import java.net.URL;
import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import vn.medianews.*;

public class LocSinhVien {
    public static void main(String[] args) throws Exception {
        String msv = "B22DCCN925", qCode = "";
        
        URL url = new URL("http://203.162.10.109:8080/JNPWS/ObjectService?wsdl");
        QName qname = new QName("http://medianews.vn/", "ObjectService");
        Service service = Service.create(url, qname);
        ObjectService os = service.getPort(ObjectService.class);

        List<Student> list = os.requestListStudent(msv, qCode);

        List<Student> result = new ArrayList<>();
        for (Student s : list) {
            float sc = s.getScore();
            if (sc >= 8.0 || sc < 5.0) result.add(s);
        }

        os.submitListStudent(msv, qCode, result);
    }
}
