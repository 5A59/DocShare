package zy.com.document;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import docnetwork.HttpUrl;
import network.MyDownloadManager;
import network.ThreadPool;
import utils.GeneralUtils;

/**
 * Created by zy on 16-1-21.
 */
public class TestActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        init();
    }

    private void init(){
        final String url = HttpUrl.mainUrl + "/media/files/poi-bin-3.10-FINAL-20140208.zip";
        final String path = GeneralUtils.getInstance().getFileSavePath() + "tt.zip";

        Button download = (Button) this.findViewById(R.id.button_download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] i = {0};
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ThreadPool.getInstance().submit(new Runnable() {
                            @Override
                            public void run() {
                                File file = new File(path + i[0]);
//                            MyDownloadManager.getInstance().downloadWithSys(TestActivity.this, url + "" + finalI, file);
                                MyDownloadManager.getInstance().download(url + i[0] ++, file);
                            }
                        });

                    }
                }, 0, 1000);
            }
        });

        Button downloadManager = (Button) this.findViewById(R.id.button_download_manager);
        downloadManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, DownloadActivity.class);
                startActivity(intent);
            }
        });
    }
}
