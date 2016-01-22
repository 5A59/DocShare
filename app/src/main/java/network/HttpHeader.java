package network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zy on 15-11-21.
 */
public class HttpHeader {
    public static String userAgentTitle = "User-Agent";
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0";

    public static String acceptTitle = "Accept";
    public static String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";

    public static String acceptLangTitle = "Accept-Language";
    public static String acceptLang = "zh,en-US;q=0.7,en;q=0.3";

    public static String acceptCodeTitle = "Accept-Encoding";
    public static String acceptCode = "gzip, deflate";

    public static Map<String,String> header;

    {
        header = new HashMap<>();

        header.put(acceptTitle,accept);
        header.put(acceptLangTitle,acceptLang);
        header.put(acceptCodeTitle,acceptCode);
        header.put(userAgentTitle,userAgent);
    }

    public static Map<String,String> getHeader(){
        return header;
    }

    public Map<String,String> addHeader(Map<String,String> para){
        Set<String> key = para.keySet();
        for (String k : key){
            header.put(k,para.get(k));
        }

        return header;
    }
}
