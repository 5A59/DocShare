package network;

import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zy on 15-12-26.
 */
public class ThreadPool {
    private final int MAX_THREAD = 5;

    private static ThreadPool threadPool;
    private ExecutorService executorService;

    private ThreadPool(){
        executorService = Executors.newFixedThreadPool(MAX_THREAD);
    }

    private static synchronized void syncInit(){
        if (threadPool == null){
            threadPool = new ThreadPool();
        }
    }

    public static  ThreadPool getInstance(){
        if (threadPool == null){
            syncInit();
        }
        return threadPool;
    }

    public void submit(Runnable runnable){
        executorService.submit(runnable);
    }

    public List<Runnable> shutdownNow(){
        return executorService.shutdownNow();
    }

    public void shutdown(){
        executorService.shutdown();
    }

}
