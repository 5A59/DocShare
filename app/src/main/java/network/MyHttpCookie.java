package network;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zy on 15-11-21.
 */
public class MyHttpCookie implements CookieStore {

    private static Map<String,HttpCookie> cookies;

    public MyHttpCookie(){
        cookies = new HashMap<>();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        cookies.put(uri.getHost(),cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        List<HttpCookie> cookieList = new ArrayList<>();

        if (cookies.containsKey(uri.getHost())){
            cookieList.add(cookies.get(uri.getHost()));
        }

        return cookieList;
    }

    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookie> cookieList = new ArrayList<>();
        cookieList.addAll(cookies.values());
        return cookieList;
    }

    @Override
    public List<URI> getURIs() {

        Set<String> key = cookies.keySet();

        List<URI> list = new ArrayList<>();

        for (String k : key){
            try {
                list.add(new URI(k));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return false;
    }

    @Override
    public boolean removeAll() {
        cookies.clear();
        return true;
    }
}
