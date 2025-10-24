package WS;
import java.net.URL;
import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import vn.medianews.DataService;

public class DoiCoSo {
    public static void main(String[] args) throws Exception {
        String msv = "B22DCCN925", qCode = "";
        
        URL url = new URL("http://203.162.10.109:8080/JNPWS/DataService?wsdl");
        QName qname = new QName("http://medianews.vn/", "DataService");
        Service service = Service.create(url, qname);
        DataService ds = service.getPort(DataService.class);

        List<Integer> data = ds.getData(msv, qCode);

        List<String> result = new ArrayList<>();
        for (int num : data) {
            String oct = Integer.toOctalString(num);
            String hex = Integer.toHexString(num).toUpperCase();
            result.add(oct + "|" + hex);
        }

        ds.submitDataStringArray(msv, qCode, result);
    }
}
