package network;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import utils.GeneralUtils;
import utils.Logger;

/**
 * Created by zy on 16-2-21.
 */
public class MyUploadManager {

    private static MyUploadManager uploadManager = null;
    private Map<String, LoadingMes> uploadMesMap;

    private MyUploadManager(){
        uploadMesMap = new HashMap<>();
    }

    private synchronized static void syncInit(){
        if (uploadManager == null){
            uploadManager = new MyUploadManager();
        }
    }

    public static MyUploadManager getInstance(){
        if (uploadManager == null){
            syncInit();
        }
        return uploadManager;
    }

    public void updateUploadMes(File file, long cur, long all){
        String hashCode = getFileHashCode(file);
        if (uploadMesMap.containsKey(hashCode)){
            LoadingMes loadingMes = uploadMesMap.get(hashCode);
            loadingMes.setAllLength(all);
            loadingMes.setCurLength(cur);
            return ;
        }

        LoadingMes loadingMes = new LoadingMes();
        loadingMes.setCurLength(cur);
        loadingMes.setAllLength(all);
        loadingMes.setSymbol(hashCode);
        loadingMes.setHashCode(hashCode);
        uploadMesMap.put(hashCode, loadingMes);
        Logger.d("add uploading mes " + hashCode);
    }

    public Collection<LoadingMes> getUploadMes(){
        return uploadMesMap.values();
    }

    public String getFileHashCode(File file){
        return file.getAbsolutePath();
    }
}
