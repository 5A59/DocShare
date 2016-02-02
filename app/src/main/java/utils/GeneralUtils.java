package utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

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
            return null;
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
}
