package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;

/**
 * Created by zy on 16-2-21.
 */
public class CmdUtils {
    private static CmdUtils cmdUtils;

    private CmdUtils(){

    }

    private synchronized static void syncInit(){
        if (cmdUtils == null){
            cmdUtils = new CmdUtils();
        }
    }

    public static CmdUtils getInstance(){
        if (cmdUtils == null){
            syncInit();
        }
        return cmdUtils;
    }

    public String getMac(){
        String[] cmd = {"/system/bin/sh", "-c", "cat /sys/class/net/wlan0/address"};
        String mac = "";
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
//            LineNumberReader lineNumberReader = new LineNumberReader(inputStreamReader);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (mac != null){
                mac = bufferedReader.readLine();
                if (mac != null){
                    mac.trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
