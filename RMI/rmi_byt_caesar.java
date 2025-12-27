package RMI;

import java.io.*;
import java.util.*;
import java.rmi.*;

public class rmi_byt_caesar {
    public static void main(String[] args) throws Exception {
        String url = "rmi://203.162.10.109:1099/RMIByteService";
        ByteService bs = (ByteService) Naming.lookup(url);

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        byte[] data = bs.requestData(studentCode, qCode);
        int shift = data.length;

        for (int i = 0; i < data.length; i++) {
            int v = data[i] & 0xFF;          
            v = (v + shift) & 0xFF;   
            data[i] = (byte) v;
        }

        bs.submitData(studentCode, qCode, data);
    }
}
