package network.listener;

/**
 * Created by zy on 16-1-18.
 */
public interface UploadProcessListener {
    void update(long hasWrite, long length, boolean done);
}
