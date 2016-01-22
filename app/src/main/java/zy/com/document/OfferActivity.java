package zy.com.document;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import docnetwork.DocNetwork;
import network.ThreadPool;
import utils.Logger;

/**
 * Created by zy on 16-1-2.
 */
public class OfferActivity extends AppCompatActivity{
    private static final int UPLOAD_OFFER_RES_CODE = 0;

    private EditText titleEdit;
    private EditText contentEdit;
    private Button uploadButton;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPLOAD_OFFER_RES_CODE){
                boolean res = (Boolean) msg.obj;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offer);
        init();
    }

    private void init(){
        initToolBar();

        titleEdit = (EditText) this.findViewById(R.id.edit_title);
        contentEdit = (EditText) this.findViewById(R.id.edit_content);
        uploadButton = (Button) this.findViewById(R.id.button_upload);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
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

    private void upload(){
        final String title = "test";
        final String content = "dddk";
        final String school = "大连理工大学";
        final String college = "软件学院";
        final String subject = "2";

        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                boolean res = DocNetwork.getInstance().uploadOfferReword(title, content, school, college, subject);
                handler.sendMessage(handler.obtainMessage(UPLOAD_OFFER_RES_CODE, res));
                Logger.d("upload offer  " + res);
            }
        });
    }
}
