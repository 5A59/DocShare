package utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import docnetwork.HttpUrl;
import zy.com.document.R;

/**
 * Created by zy on 15-12-21.
 */
public class GeneralUtils {
    private static GeneralUtils utils;

    private GeneralUtils(){

    }

    public static GeneralUtils getInstance(){
        if (utils == null){
            utils = new GeneralUtils();
        }
        return utils;
    }

    public String getSdcardPath(){
        return android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public String getFileSavePath(){
        return getSdcardPath() + "/document/";
    }

    public String getFilePostfix(String fileName){
        String[] sub = fileName.split("\\.");
        Logger.d(fileName);
        Logger.d("" + sub.length);
        if (sub.length <= 1){
            return "";
        }
        return sub[sub.length - 1].toUpperCase();
    }

    public int getBackByPostfix(String postfix){
        if (postfix == null){
            return R.mipmap.unknow;
        }
        if (postfix.equals("WORD") || postfix.equals("DOC") || postfix.equals("DOCX")){
            return R.mipmap.word;
        }else if (postfix.equals("PDF")){
            return R.mipmap.pdf;
        }else if (postfix.equals("EXCLE") || postfix.equals("XLS") || postfix.equals("XLSX")) {
            return R.mipmap.excle;
        }else if (postfix.equals("PPT") || postfix.equals("PPTX")) {
            return R.mipmap.ppt;
        }else if (postfix.equals("ZIP") || postfix.equals("RAR") || postfix.equals("JAR")) {
            return R.mipmap.zip;
        }else if (postfix.equals("TXT")) {
            return R.mipmap.txt;
        }else if (postfix.equals("HTML")) {
            return R.mipmap.html;
        }else if (postfix.equals("APK")){
            return R.mipmap.apk;
        }else if (postfix.equals("MP4") || postfix.equals("RMVB") || postfix.equals("AVI")
                || postfix.equals("WMV")){
            return R.mipmap.video;
        }else if (ifPic(postfix)){
            return R.mipmap.pic;
        }

        return R.mipmap.unknow;
    }

    public boolean ifPic(String postfix){
        if (postfix == null){
            return false;
        }
        if (postfix.equals("JPG") || postfix.equals("PNG") || postfix.equals("JPEG")
                || postfix.equals("BMP")){
            return true;
        }
        return false;
    }

    public boolean ifApk(String postfix){
        if (postfix == null){
            return false;
        }
        if (postfix.equals("APK")){
            return true;
        }
        return false;
    }

    public void setApk(Context context, ImageView imageView, File file){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = packageManager.getPackageArchiveInfo(file.getAbsolutePath(),
                PackageManager.GET_ACTIVITIES);
        if (info == null){
            return ;
        }
        ApplicationInfo applicationInfo = info.applicationInfo;
        applicationInfo.sourceDir = file.getAbsolutePath();
        applicationInfo.publicSourceDir = file.getAbsolutePath();
        Drawable drawable = applicationInfo.loadIcon(packageManager);
        imageView.setImageDrawable(drawable);
    }

    public void setIcon(Context context, ImageView imageView, File resFile){
        if (resFile == null){
            return ;
        }
        if (resFile.isDirectory()){
            setPic(context, imageView,
                    R.mipmap.folder, R.mipmap.folder, R.mipmap.folder);
            return ;
        }
        String postfix = getFilePostfix(resFile.getName());
        if (ifPic(postfix)) {
            setPic(context, imageView, resFile, R.mipmap.pic, R.mipmap.pic);
        }else if (ifApk(postfix)){
            setApk(context, imageView, resFile);
        }else {
//            imageView.setImageResource(getBackByPostfix(postfix));
            setPic(context, imageView, getBackByPostfix(postfix), R.mipmap.unknow, R.mipmap.unknow);
        }
    }

    public void setIcon(Context context, ImageView imageView, String fileName){
        String postfix = getFilePostfix(fileName);
        imageView.setImageResource(getBackByPostfix(postfix));
    }

    public void setHeadImg(Context context, ImageView view, String url){
        setPic(context, view, url, R.mipmap.def_headimg, R.mipmap.def_headimg);
    }

    public void setPic(Context context, ImageView view, String uri, int errid, int loading){
        Glide.with(context)
                .load(uri)
                .error(errid)
//                .placeholder(loading)
                .into(view);

    }

    public void setPic(Context context, ImageView view, int id, int errid, int loading){
        Glide.with(context)
                .load(id)
                .error(errid)
                .placeholder(loading)
                .into(view);
    }

    public void setPic(Context context, ImageView imageView, File resFile, int errid, int loading){
        Glide.with(context)
                .load(resFile)
                .error(errid)
                .placeholder(loading)
//                .error(R.mipmap.pic)
//                .placeholder(R.mipmap.pic)
                .into(imageView);
    }

    public String getFullUrl(String url){
        return HttpUrl.mainUrl + url;
    }

    public String getFileName(String file){
        String [] tmp = file.split("\\/");
        return tmp[tmp.length - 1];

    }

    public void copyToClipper(Context context, String label, String content){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText(label, content);
        clipboardManager.setPrimaryClip(data);
    }

    public String getFromClipper(Context context){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        String data = "";
        if (clipboardManager.hasPrimaryClip()){
            ClipData clip =clipboardManager.getPrimaryClip();
            data = clip.getItemAt(clip.getItemCount() - 1).coerceToText(context).toString();
        }
        return data;
    }

    public void myToast(Context context, String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public void hideSoftInput(Activity activity){
        ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showSoftInput(EditText editText, Activity activity){
        ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE))
//                .showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
                .toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public String getMIME(File file){
        String postfix = "." + getFilePostfix(file.getName()).toLowerCase();
        String[][] mimeTable = getMIMETable();
        for (String[] mime : mimeTable){
            if (mime[0].equals(postfix)){
                return mime[1];
            }
        }
        return "";
    }

    public void openFile(Context context, File file){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String mime = getMIME(file);
        intent.setDataAndType(Uri.fromFile(file), mime);
        try{
            context.startActivity(intent);
        }catch (Exception e){
            myToast(context, "未安装可以打开此文件的软件");
        }
    }

    public String[][] getMIMETable(){
        String[][] MIMEMapTable={
                //{后缀名，MIME类型}
                {".3gp",    "video/3gpp"},
                {".apk",    "application/vnd.android.package-archive"},
                {".asf",    "video/x-ms-asf"},
                {".avi",    "video/x-msvideo"},
                {".bin",    "application/octet-stream"},
                {".bmp",    "image/bmp"},
                {".c",  "text/plain"},
                {".class",  "application/octet-stream"},
                {".conf",   "text/plain"},
                {".cpp",    "text/plain"},
                {".doc",    "application/msword"},
                {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
                {".xls",    "application/vnd.ms-excel"},
                {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
                {".exe",    "application/octet-stream"},
                {".gif",    "image/gif"},
                {".gtar",   "application/x-gtar"},
                {".gz", "application/x-gzip"},
                {".h",  "text/plain"},
                {".htm",    "text/html"},
                {".html",   "text/html"},
                {".jar",    "application/java-archive"},
                {".java",   "text/plain"},
                {".jpeg",   "image/jpeg"},
                {".jpg",    "image/jpeg"},
                {".js", "application/x-javascript"},
                {".log",    "text/plain"},
                {".m3u",    "audio/x-mpegurl"},
                {".m4a",    "audio/mp4a-latm"},
                {".m4b",    "audio/mp4a-latm"},
                {".m4p",    "audio/mp4a-latm"},
                {".m4u",    "video/vnd.mpegurl"},
                {".m4v",    "video/x-m4v"},
                {".mov",    "video/quicktime"},
                {".mp2",    "audio/x-mpeg"},
                {".mp3",    "audio/x-mpeg"},
                {".mp4",    "video/mp4"},
                {".mpc",    "application/vnd.mpohun.certificate"},
                {".mpe",    "video/mpeg"},
                {".mpeg",   "video/mpeg"},
                {".mpg",    "video/mpeg"},
                {".mpg4",   "video/mp4"},
                {".mpga",   "audio/mpeg"},
                {".msg",    "application/vnd.ms-outlook"},
                {".ogg",    "audio/ogg"},
                {".pdf",    "application/pdf"},
                {".png",    "image/png"},
                {".pps",    "application/vnd.ms-powerpoint"},
                {".ppt",    "application/vnd.ms-powerpoint"},
                {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
                {".prop",   "text/plain"},
                {".rc", "text/plain"},
                {".rmvb",   "audio/x-pn-realaudio"},
                {".rtf",    "application/rtf"},
                {".sh", "text/plain"},
                {".tar",    "application/x-tar"},
                {".tgz",    "application/x-compressed"},
                {".txt",    "text/plain"},
                {".wav",    "audio/x-wav"},
                {".wma",    "audio/x-ms-wma"},
                {".wmv",    "audio/x-ms-wmv"},
                {".wps",    "application/vnd.ms-works"},
                {".xml",    "text/plain"},
                {".z",  "application/x-compress"},
                {".zip",    "application/x-zip-compressed"},
                {"",        "*/*"}};
        return MIMEMapTable;
    }
}
