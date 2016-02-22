package zy.com.document;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import docnetwork.DocNetwork;
import fileselecter.FileSelecterActivity;
import fileselecter.FileList;
import network.ThreadPool;
import network.listener.UploadProcessListener;
import utils.Logger;

/**
 * Created by zy on 16-1-6.
 */
public class UploadDocActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int UPLOAD_RES_CODE = 0;

    private EditText titleEdit;
    private EditText contentEdit;
    private ImageView fileImg;
    private TextView fileText;

    private List<File> fileList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPLOAD_RES_CODE){
                boolean res = (Boolean) msg.obj;
                Logger.d("upload file " + res);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_doc);
        init();
    }

    private void init(){
        initToolBar();
        initData();

        titleEdit = (EditText) this.findViewById(R.id.edit_title);
        contentEdit = (EditText) this.findViewById(R.id.edit_content);
        fileImg = (ImageView) this.findViewById(R.id.img_add_file);

        fileImg.setOnClickListener(this);

        fileText = (TextView) this.findViewById(R.id.text_files);
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){
        fileList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_add_file:
                Intent intent = new Intent(this, FileSelecterActivity.class);
                startActivityForResult(intent, FileSelecterActivity.FILE_SELECT_RES_CODE);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_upload:
                upload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void upload(){
        Logger.d("upload");
        final String title = "dddl";
        final String content = "ddd";
        final String school = "大连理工大学";
        final String college = "软件学院";
        final String subject = "2";
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                boolean res = DocNetwork.getInstance()
                        .uploadDoc(title, content, school, college, subject, fileList, null);
                handler.sendMessage(handler.obtainMessage(UPLOAD_RES_CODE, res));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FileSelecterActivity.FILE_SELECT_RES_CODE){
            FileList tmpFile = (FileList) data.getSerializableExtra(FileSelecterActivity.FILE_SELECT_RES_KEY);
            if (tmpFile.getFiles() != null){
                fileList.addAll(tmpFile.getFiles());
                String fileString = "";
                for (File f : fileList){
                    fileString += f.getPath();
                }
                fileText.setText(fileString);
            }
        }
    }
}
