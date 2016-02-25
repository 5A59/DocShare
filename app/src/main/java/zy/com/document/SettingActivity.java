package zy.com.document;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import de.hdodenhof.circleimageview.CircleImageView;
import docnetwork.DocNetwork;
import docnetwork.HttpData;
import docnetwork.dataobj.Info;
import network.ThreadPool;
import utils.GeneralUtils;
import utils.Logger;
import utils.MyPreference;

/**
 * Created by zy on 16-2-21.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int ALBUM_CODE = 0;
    public static final int UPLOAD_HEADIMG_CODE = 1;
    public static final int UPLOAD_NAME_CODE = 2;
    public static final int BOUND_MOBILE_CODE = 3;
    public static final String ALBUM_TAG = "select from album";
    public static final String UPLOAD_HEADIMG_RES_TAG = "res";
    public static final String UPLOAD_HEADIMG_PATH_TAG = "path";
    public static final String UPLOAD_NAME_TAG = "name";
    public static final String BOUND_MOBILE_TAG = "mobile";

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
            Bundle bundle = (Bundle) msg.obj;
            if (msg.what == UPLOAD_HEADIMG_CODE){
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
            }else if (msg.what == UPLOAD_NAME_CODE){
                if (bundle.getBoolean(UPLOAD_HEADIMG_RES_TAG)){
                    ifChangeInfo = true;
                    nameText.setText(bundle.getString(UPLOAD_NAME_TAG));
                    GeneralUtils.getInstance().myToast(SettingActivity.this,
                            getResources().getString(R.string.upload_name_success));
                }else {
                    GeneralUtils.getInstance().myToast(SettingActivity.this,
                            getResources().getString(R.string.upload_name_fail));
                }
            }else if (msg.what == BOUND_MOBILE_CODE){
                if (bundle.getBoolean(UPLOAD_HEADIMG_RES_TAG)){
                    ifChangeInfo = true;
                    mobileText.setText(bundle.getString(BOUND_MOBILE_TAG));
                    mobileTitleText.setText(getResources().getString(R.string.un_bound_mobile));
                    GeneralUtils.getInstance().myToast(SettingActivity.this,
                            getResources().getString(R.string.bound_mobile_success));
                    MyPreference.getInstance().setBound(SettingActivity.this, true);
                }else {
                    GeneralUtils.getInstance().myToast(SettingActivity.this,
                            getResources().getString(R.string.bound_mobile_fail));
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
                setNickName();
                break;
            case R.id.layout_school:
                break;
            case R.id.layout_college:
                break;
            case R.id.layout_bound:
                if (!MyPreference.getInstance().ifBound(this)){
                    boundMobile();
                }
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

    private void setNickName(){
        final EditText editText = new EditText(this);
        AlertDialog dialog = new AlertDialog
                .Builder(this)
                .setView(editText)
                .setPositiveButton(getResources().getString(R.string.sure),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editText.getText().toString();
                        uploadNickName(name);
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    private void boundMobile(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
//        RegisterPage registerPage = new RegisterPage();
//        registerPage.setRegisterCallback(new EventHandler() {
//            public void afterEvent(int event, int result, Object data) {
//                // 解析注册结果
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    @SuppressWarnings("unchecked")
//                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                    String country = (String) phoneMap.get("country");
//                    String phone = (String) phoneMap.get("phone");
//
//                    // 提交用户信息
//                    uploadBoundMobile(phone);
//                }
//            }
//        });
//        registerPage.show(this);
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
                bundle.putString(UPLOAD_HEADIMG_PATH_TAG, path);
                bundle.putBoolean(UPLOAD_HEADIMG_RES_TAG, res);
                handler.sendMessage(handler.obtainMessage(UPLOAD_HEADIMG_CODE, bundle));
            }
        });
    }

    private void uploadNickName(final String name){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                boolean res = DocNetwork.getInstance().changeInfo(name, "", "", "", "");
                Bundle bundle = new Bundle();
                bundle.putString(UPLOAD_NAME_TAG, name);
                bundle.putBoolean(UPLOAD_HEADIMG_RES_TAG, res);
                handler.sendMessage(handler.obtainMessage(UPLOAD_NAME_CODE, bundle));
            }
        });
    }

    private void uploadBoundMobile(final String mobile){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                boolean res = DocNetwork.getInstance().boundMobile(HttpData.userNum.toString(), mobile);
                Bundle bundle = new Bundle();
                bundle.putString(BOUND_MOBILE_TAG, mobile);
                bundle.putBoolean(UPLOAD_HEADIMG_RES_TAG, res);
                handler.sendMessage(handler.obtainMessage(BOUND_MOBILE_CODE, bundle));
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
