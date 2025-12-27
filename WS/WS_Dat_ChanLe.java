package WS;

import java.util.*;
import vn.medianews.*;

public class WS_Dat_ChanLe {

    public static List<Integer> xuLyList(List<Integer> list) {
        if (list == null) return new ArrayList<>();

        List<Integer> even = new ArrayList<>();
        List<Integer> odd  = new ArrayList<>();

        for (Integer x : list) {
            if (x == null) continue; 
            if (x % 2 == 0) even.add(x);
            else odd.add(x);
        }

        int i = 0, j = 0;
        List<Integer> result = new ArrayList<>(even.size() + odd.size());

        while (i < even.size() && j < odd.size()) {
            result.add(even.get(i++));
            result.add(odd.get(j++));
        }
        while (i < even.size()) result.add(even.get(i++));
        while (j < odd.size()) result.add(odd.get(j++));

        return result;
    }

    public static void main(String[] args) throws Exception {
        DataService_Service service = new DataService_Service();
        DataService port = service.getDataServicePort();

        String studentCode = "B22DCCNxxx"; 
        String qCode = "qCode";           

        List<Integer> list = port.getData(studentCode, qCode);
        List<Integer> answer = xuLyList(list);

        port.submitDataIntArray(studentCode, qCode, answer);
    }
}
