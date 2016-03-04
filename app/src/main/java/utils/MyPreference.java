package utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zy on 16-2-21.
 */
public class MyPreference {
    private final static String NAME = "document";
    private final static int MODE = Context.MODE_PRIVATE;
    private final static String USERNAME = "userName";
    private final static String PWD = "pwd";
    private final static String IF_BOUND = "bound";
    private final static String IF_FIRST = "first";
    private final static String DEFAULT = "";
    private static MyPreference preference;

    private MyPreference(){

    }

    private static synchronized void syncInit(){
        if (preference == null){
            preference = new MyPreference();
        }
    }

    public static MyPreference getInstance(){
        if (preference == null){
            syncInit();
        }

        return preference;
    }

    public SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(NAME, MODE);
    }

    public SharedPreferences.Editor getEditor(SharedPreferences preferences){
        return preferences.edit();
    }

    public void setFirst(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = getEditor(sharedPreferences);
        editor.putBoolean(IF_FIRST, false).commit();
    }

    public boolean ifFirst(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        boolean res = sharedPreferences.getBoolean(IF_FIRST, true);
        return res;
    }

    public void setNameAndPwd(Context context, String userName, String pwd){
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = getEditor(sharedPreferences);
        editor.putString(USERNAME, userName).commit();
        editor.putString(PWD, pwd).commit();
    }

    public String getUserName(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getString(USERNAME, DEFAULT);
    }

    public String getPwd(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getString(PWD, DEFAULT);
    }

    public boolean ifBound(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getBoolean(IF_BOUND, false);
    }

    public void setBound(Context context, boolean bound){
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = getEditor(sharedPreferences);
        editor.putBoolean(IF_BOUND, bound).commit();
    }
}
