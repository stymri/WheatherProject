package rai.satyam.wheatherproject.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Utils {
    public static String getStringFromInputStream(InputStream prm_objInputStream) {
        String v_Return = null;
        BufferedReader v_objBufferedReader;
        try {
            v_objBufferedReader = new BufferedReader(new InputStreamReader(prm_objInputStream));
            v_Return = v_objBufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (Exception v_exException) {
            v_exException.printStackTrace();
        }
        return v_Return;
    }
}
