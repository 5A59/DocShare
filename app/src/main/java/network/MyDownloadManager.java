package network;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import docnetwork.HttpUrl;
import network.listener.DownloadProcessListener;
import utils.Logger;

/**
 * Created by zy on 16-1-20.
 */
public class MyDownloadManager {

    private Network network;
    private static MyDownloadManager myDownloadManager = null;
    private List<Long> downloadIds;
    private Map<String, DownloadMes> downloadMesMap;

    private synchronized static void syncInit(){
        if (myDownloadManager == null){
            myDownloadManager = new MyDownloadManager();
        }
    }

    private MyDownloadManager(){
        downloadIds = new ArrayList<>();
        downloadMesMap = new HashMap<>();
        network = Network.getInstance();
    }

    public static MyDownloadManager getInstance(){
        if (myDownloadManager == null){
            syncInit();
        }
        return myDownloadManager;
    }

    public void downloadWithSys(Context context, String url, File toFile){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationUri(Uri.fromFile(toFile));

        long id = downloadManager.enqueue(request);
        downloadIds.add(id);
    }

    public List<Long> getSysDownloadIds(){
        return downloadIds;
    }

    public DownloadMes getSysDownMesById(Context context, Long id){
        DownloadMes mes = new DownloadMes();
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);

        while (cursor.moveToNext()){
            int totalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int downloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            int filaNameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);

            mes.setAllLength(cursor.getInt(totalIndex));
            mes.setCurLength(cursor.getInt(downloadedIndex));
            mes.setFileName(cursor.getString(filaNameIndex));
        }
        if (!cursor.isClosed()){
            cursor.close();
        }

        return mes;
    }

    public List<DownloadMes> getSysDownMes(Context context){
        List<DownloadMes> res = new ArrayList<>();
        for (Long id : downloadIds){
            DownloadMes mes = getSysDownMesById(context, id);
            res.add(mes);
        }

        return res;
    }

    public void download(String url, final File toFile) {
        network.downloadFile(new DownloadProcessListener() {
            @Override
            public void update(long hasRead, long length, boolean done) {
                updateDownloadMes(getFileHashCode(toFile), hasRead, length);
            }
        }, url, toFile);

        addDownloadMes(toFile, 0, 100);
    }

    private void addDownloadMes(File toFile, long curLength, long allLength){
        DownloadMes downloadMes = new DownloadMes();
        downloadMes.setCurLength(curLength);
        downloadMes.setAllLength(allLength);
        downloadMes.setFileName(toFile.getName());
        downloadMesMap.put(getFileHashCode(toFile), downloadMes);
    }

    private void updateDownloadMes(String hashCode, long curLength, long allLength) {
        if (downloadMesMap.containsKey(hashCode)){
            DownloadMes downloadMes = downloadMesMap.get(hashCode);
            downloadMes.setCurLength(curLength);
            downloadMes.setAllLength(allLength);
        }
    }

    public Collection<DownloadMes> getDownMes() {

        return downloadMesMap.values();
    }

    private String getFileHashCode(File toFile) {
        return toFile.getAbsolutePath();
    }

}
