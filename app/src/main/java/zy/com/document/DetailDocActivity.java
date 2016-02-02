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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import docnetwork.HttpUrl;
import docnetwork.dataobj.Doc;
import docsadapter.DetailAdapter;
import docsadapter.DetailDocAdapter;
import fileselecter.OnRecyclerItemClickListener;
import network.MyDownloadManager;
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
    private List<String> files;

    private Doc.DocMes docMes;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DOWNLOAD_UPDATE){
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
        String [] tmpFiles = docMes.getFiles().split(";");
        files = new ArrayList<>();
        Collections.addAll(files, tmpFiles);
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
        adapter = new DetailDocAdapter(this, docMes.getContent(), files);
        adapter.setItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                downloadFile(pos);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void downloadFile(final int pos){
        final String fileUrl = HttpUrl.mainUrl + files.get(pos);
        String[] fileNames = files.get(pos).split("/");
        final String fileName = fileNames[fileNames.length - 1];
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Logger.d("start download");
                File toFile = new File(GeneralUtils.getInstance().getFileSavePath() + "/" + fileName);
                MyDownloadManager.getInstance().download(fileUrl, toFile);
            }
        });
    }
}
