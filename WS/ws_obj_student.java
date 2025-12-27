package WS;

import java.util.*;
import vn.medianews.*;   

public class ws_obj_student {
    public static void main(String[] args) throws Exception {
        ObjectService_Service service = new ObjectService_Service();
        ObjectService port = service.getObjectServicePort();

        String studentCode = "B22DCCNxxx"; 
        String qCode = "qCode";

        List<Student> students = port.requestListStudent(studentCode, qCode);
        if (students == null) students = new ArrayList<>();

        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s == null) continue;
            float score = s.getScore(); 
            if (score >= 8.0f || score < 5.0f) {
                result.add(s);
            }
        }

        port.submitListStudent(studentCode, qCode, result);
    }
}
