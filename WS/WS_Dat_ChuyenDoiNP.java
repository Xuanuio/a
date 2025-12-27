package WS;

import java.io.*;
import java.util.*;
import java.net.*;
import vn.medianews.*;

public class WS_Dat_ChuyenDoiNP {

    public static void main(String[] args) {
        DataService_Service service = new DataService_Service();
        DataService port = service.getDataServicePort();

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        List<Integer> list = port.getData(studentCode, qCode);
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String binary = Integer.toBinaryString(list.get(i));
            list2.add(binary);
        }
        port.submitDataStringArray(studentCode, qCode, list2);
    }
}
