package zy.com.document;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import docnetwork.DocNetwork;
import docnetwork.HttpData;
import docnetwork.dataobj.Info;
import network.ThreadPool;
import utils.GeneralUtils;
import utils.Logger;

/**
 * Created by zy on 16-2-21.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int ALBUM_CODE = 0;
    public static final int UPLOAD_HEADIMG_CODE = 1;
    public static final String ALBUM_TAG = "select from album";
    public static final String UPLOAD_HEADIMG_RES_TAG = "res";
    public static final String UPLOAD_HEADIMG_PATH_TAG = "path";

    private Toolbar toolbar;

    private RelativeLayout headLayout;
    private RelativeLayout nameLayout;
    private RelativeLayout schoolLayout;
    private RelativeLayout collegeLayout;
    private RelativeLayout boundLayout;

    private CircleImageView headImg;
    private TextView nameText;
    private TextView schoolText;
    private TextView collegeText;
    private TextView mobileText;
    private TextView mobileTitleText;

    private boolean ifChangeInfo;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPLOAD_HEADIMG_CODE){
                Bundle bundle = (Bundle) msg.obj;
                if (bundle.getBoolean(UPLOAD_HEADIMG_RES_TAG)){
                    ifChangeInfo = true;
                    GeneralUtils.getInstance().myToast(SettingActivity.this,
                            getResources().getString(R.string.upload_headimg_success));
                    GeneralUtils.getInstance()
                            .setHeadImg(SettingActivity.this, headImg, new File(bundle.getString(UPLOAD_HEADIMG_PATH_TAG)));
                }else {
                    GeneralUtils.getInstance().myToast(SettingActivity.this,
                            getResources().getString(R.string.upload_headimg_fail));
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        initToolBar();
        initView();
        initInfo();
    }

    private void initToolBar(){
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.settings));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRes();
                finish();
            }
        });
    }

    private void initView(){
        headLayout = (RelativeLayout) this.findViewById(R.id.layout_head);
        nameLayout = (RelativeLayout) this.findViewById(R.id.layout_name);
        schoolLayout = (RelativeLayout) this.findViewById(R.id.layout_school);
        collegeLayout = (RelativeLayout) this.findViewById(R.id.layout_college);
        boundLayout = (RelativeLayout) this.findViewById(R.id.layout_bound);

        headImg = (CircleImageView) this.findViewById(R.id.img_head);
        nameText = (TextView) this.findViewById(R.id.text_name);
        schoolText = (TextView) this.findViewById(R.id.text_school);
        collegeText = (TextView) this.findViewById(R.id.text_college);
        mobileText = (TextView) this.findViewById(R.id.text_mobile);
        mobileTitleText = (TextView) this.findViewById(R.id.text_mobile_title);

        headLayout.setOnClickListener(this);
        nameLayout.setOnClickListener(this);
        schoolLayout.setOnClickListener(this);
        collegeLayout.setOnClickListener(this);
        boundLayout.setOnClickListener(this);

        ifChangeInfo = false;
    }

    private void initInfo(){
        Info.Inf info = HttpData.info.getInf();
        GeneralUtils.getInstance().setHeadImg(this, headImg, info.getHeadImgUrl());
        nameText.setText(info.getName());
        schoolText.setText(info.getSchool());
        collegeText.setText(info.getCollege());
        if (info.getMobile() == null || info.getMobile().isEmpty()){
            mobileTitleText.setText(getResources().getString(R.string.bound_mobile));
        }else {
            mobileText.setText(info.getMobile());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_head:
                addHeadImg();
                break;
            case R.id.layout_name:
                break;
            case R.id.layout_school:
                break;
            case R.id.layout_college:
                break;
            case R.id.layout_bound:
                break;
        }
    }

    private void addHeadImg(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 80);
//        intent.putExtra("outputY", 80);
//        intent.putExtra("scale", true);
//        intent.putExtra("return-data", true);

        startActivityForResult(Intent.createChooser(intent, ALBUM_TAG), ALBUM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK){
            Logger.d("return album fail");
            return ;
        }
        if (requestCode == ALBUM_CODE){
            Uri uri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            CursorLoader cursorLoader = new CursorLoader(this, uri, projection, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);

            uploadHeadImg(path);
        }
    }

    private void uploadHeadImg(final String path){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                boolean res = DocNetwork.getInstance().changeInfo("", "", "", "", path);
                Bundle bundle = new Bundle();
                bundle.putString("path", path);
                bundle.putBoolean("res", res);
                handler.sendMessage(handler.obtainMessage(UPLOAD_HEADIMG_CODE, bundle));
            }
        });
    }

    public void setRes(){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(UPLOAD_HEADIMG_RES_TAG, ifChangeInfo);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        setRes();
        super.onBackPressed();
    }
}
