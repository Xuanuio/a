package WS;

import java.util.*;
import java.net.*;
import javax.xml.datatype.XMLGregorianCalendar;
import vn.medianews.*;

public class ws_obj_nhanvien {
    public static void main(String[] args) throws Exception {
        ObjectService_Service service = new ObjectService_Service();
        ObjectService port = service.getObjectServicePort();

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        List<EmployeeY> list = port.requestListEmployeeY(studentCode, qCode);

        List<EmployeeY> sorted = new ArrayList<>(list);
        sorted.sort((a, b) -> {
            XMLGregorianCalendar da = a.getStartDate();
            XMLGregorianCalendar db = b.getStartDate();
            if (da == null && db == null) return 0;
            if (da == null) return 1;
            if (db == null) return -1;
            long ta = da.toGregorianCalendar().getTimeInMillis();
            long tb = db.toGregorianCalendar().getTimeInMillis();
            return Long.compare(ta, tb); 
        });

        port.submitListEmployeeY(studentCode, qCode, sorted);
    }
}
