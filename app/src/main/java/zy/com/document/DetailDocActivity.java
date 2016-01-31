package zy.com.document;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.util.Vector;

import docnetwork.DocNetwork;
import docnetwork.HttpUrl;
import docnetwork.dataobj.Doc;
import docsadapter.DetailDocAdapter;
import fileselecter.OnRecyclerItemClickListener;
import network.MyDownloadManager;
import network.listener.DownloadProcessListener;
import network.ThreadPool;
import utils.GeneralUtils;
import utils.Logger;

/**
 * Created by zy on 15-12-29.
 */
public class DetailDocActivity extends AppCompatActivity{
    private static final int DOWNLOAD_UPDATE = 0;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private DetailDocAdapter adapter;
    private RecyclerView recyclerView;
    private String [] files;

    private Doc.DocMes docMes;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DOWNLOAD_UPDATE){
                Vector v = (Vector) msg.obj;
                if (v.size() < 4){
                    return ;
                }
                int pos = (Integer) v.get(0);
                long hasRead = (Long) v.get(1);
                long length = (Long) v.get(2);
                boolean done = (Boolean) v.get(3);
                ProgressBar progressBar = (ProgressBar) recyclerView.getChildAt(pos)
                        .findViewById(R.id.progress_doc_download);
                progressBar.setMax((int) length);
                progressBar.setProgress((int) hasRead);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_doc);

        init();
    }

    private void init(){
        initData();
        initToolBar();
        initView();
    }

    private void initData(){
        docMes = (Doc.DocMes) this.getIntent().getSerializableExtra("doc");
        files = docMes.getFiles().split(";");
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        collapsingToolbarLayout = (CollapsingToolbarLayout) this.findViewById(R.id.coll);
        collapsingToolbarLayout.setTitle(docMes.getTitle());

        recyclerView = (RecyclerView) this.findViewById(R.id.recycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DetailDocAdapter(this, files);
        adapter.setItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                downloadFile(pos);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void downloadFile(final int pos){
        final String fileUrl = HttpUrl.mainUrl + files[pos];
        String[] fileNames = files[pos].split("/");
        final String fileName = fileNames[fileNames.length - 1];
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Logger.d("start download");
                File toFile = new File(GeneralUtils.getInstance().getFileSavePath() + "/" + fileName);
                MyDownloadManager.getInstance().download(fileUrl, toFile);
//                DocNetwork.getInstance().downloadFile(new DownloadProcessListener() {
//                    @Override
//                    public void update(long hasRead, long length, boolean done) {
//                        Vector v = new Vector();
//                        v.add(pos);
//                        v.add(hasRead);
//                        v.add(length);
//                        v.add(done);
//                        handler.sendMessage(handler.obtainMessage(DOWNLOAD_UPDATE, v));
//                    }
//                }, fileUrl, fileName);
            }
        });
    }
}
