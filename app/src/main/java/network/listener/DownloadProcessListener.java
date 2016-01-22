package network.listener;

/**
 * Created by zy on 16-1-18.
 */
public interface DownloadProcessListener {
    void update(long hasRead, long length, boolean done);
}
