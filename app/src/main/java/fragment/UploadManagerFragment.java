package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import docsadapter.DownloadAdapter;
import network.DownloadMes;
import network.MyDownloadManager;
import zy.com.document.R;

/**
 * Created by zy on 16-1-20.
 */
public class UploadManagerFragment extends Fragment {
    private static final int UPDATE_FLAG = 0;

    private DownloadAdapter downloadAdapter;
    private RecyclerView recyclerView;

    private List<DownloadMes> downloadMes;

    private View rootView;

    private Timer timer;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_FLAG){
                updateDownload();
            }
        }
    };

    public UploadManagerFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_download_manager, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        initUpdate();
    }

    private void initData(){
        downloadMes = new ArrayList<>();
    }

    private void initView(){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_download);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        downloadAdapter = new DownloadAdapter(this.getActivity(), downloadMes);
        recyclerView.setAdapter(downloadAdapter);
    }

    private void initUpdate(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getDownloadData();
                handler.sendEmptyMessage(UPDATE_FLAG);
            }
        }, 100, 1000);
    }

    private void getDownloadData(){
        downloadMes.clear();
//        downloadMes.addAll(MyDownloadManager.getInstance().getSysDownMes(this.getActivity()));
        downloadMes.addAll(MyDownloadManager.getInstance().getDownMes());
    }

    private void updateDownload(){
        downloadAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }
    }
}
