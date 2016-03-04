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

import de.hdodenhof.circleimageview.CircleImageView;
import docnetwork.DocNetwork;
import docnetwork.HttpData;
import docnetwork.SuccessCheck;
import docnetwork.dataobj.Info;
import docnetwork.dataobj.Login;
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
    public static final int UNBOUND_MOBILE_CODE = 4;
    public static final int SCHOOL_CODE = 5;
    public static final int LOGIN_CODE = 6;
    public static final String ALBUM_TAG = "select from album";
    public static final String UPLOAD_HEADIMG_RES_TAG = "res";
    public static final String UPLOAD_HEADIMG_PATH_TAG = "path";
    public static final String UPLOAD_NAME_TAG = "name";
    public static final String BOUND_MOBILE_TAG = "mobile";
    public static final String PWD_TAG = "pwd";
    public static final String LOGIN_TAG = "login";

    private String sure;
    private String cancel;

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
    private TextView loginText;

    private AlertDialog pwdDialog;
    private AlertDialog sureDialog;

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
                    String mobile = bundle.getString(BOUND_MOBILE_TAG);
                    String pwd = bundle.getString(PWD_TAG);
                    mobileText.setText(mobile);
                    mobileTitleText.setText(getResources().getString(R.string.un_bound_mobile));
                    GeneralUtils.getInstance().myToast(SettingActivity.this,
                            getResources().getString(R.string.bound_mobile_success));
                    MyPreference.getInstance().setBound(SettingActivity.this, true);
                    MyPreference.getInstance().setNameAndPwd(SettingActivity.this, mobile, pwd);
                }else {
                    GeneralUtils.getInstance().myToast(SettingActivity.this,
                            getResources().getString(R.string.bound_mobile_fail));
                }
            }else if (msg.what == UNBOUND_MOBILE_CODE){
                boolean res = (Boolean) msg.obj;
                if (res){
                    GeneralUtils.getInstance().myToast(SettingActivity.this,
                            getResources().getString(R.string.unbound_mobile_success));
                    MyPreference.getInstance().setBound(SettingActivity.this, false);
                }else {
                    GeneralUtils.getInstance().myToast(SettingActivity.this,
                            getResources().getString(R.string.unbound_mobile_fail));
                }
            }else if (msg.what == SCHOOL_CODE){
                ifChangeInfo = true;
                String sch = bundle.getString(SchoolAndCollegeSelecterActivity.SCHOOL_TAG);
                String col = bundle.getString(SchoolAndCollegeSelecterActivity.COLLEGE_TAG);
                schoolText.setText(sch);
                collegeText.setText(col);
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
        loginText = (TextView) this.findViewById(R.id.text_login);

        headLayout.setOnClickListener(this);
        nameLayout.setOnClickListener(this);
        schoolLayout.setOnClickListener(this);
        collegeLayout.setOnClickListener(this);
        boundLayout.setOnClickListener(this);
        loginText.setOnClickListener(this);

        ifChangeInfo = false;
        sure = getResources().getString(R.string.sure);
        cancel = getResources().getString(R.string.cancel);
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
            mobileTitleText.setText(getResources().getString(R.string.un_bound_mobile));
            mobileText.setText(info.getMobile());
            loginText.setText(getResources().getString(R.string.logout));
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
            case R.id.layout_college:
                setSchAndCol();
                break;
            case R.id.layout_bound:
                if (!MyPreference.getInstance().ifBound(this)){
                    boundMobile();
                }else {
                    unBoundMobile();
                }
                break;
            case R.id.text_login:
                if (!MyPreference.getInstance().ifBound(this)){
                    login();
                }else {
                    logout();
                }

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

    private void setSchAndCol(){
        Intent intent = new Intent(this, SchoolAndCollegeSelecterActivity.class);
        startActivityForResult(intent, SCHOOL_CODE);
    }

    private void boundMobile(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, BOUND_MOBILE_CODE);
    }

    private void unBoundMobile(){
        final EditText editText = new EditText(this);
        editText.setHint(getResources().getString(R.string.input_pwd));
        if (pwdDialog == null){
            pwdDialog = new AlertDialog.Builder(this)
                    .setView(editText)
                    .setPositiveButton(sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pwd = editText.getText().toString();
                            if (pwd != null && !pwd.isEmpty()){
                                uploadUnBoundMobile(mobileText.getText().toString(), pwd);
                                pwdDialog.dismiss();
                            }
                        }
                    })
                    .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pwdDialog.dismiss();
                        }
                    })
                    .create();

        }
        if (sureDialog == null){
            sureDialog = new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.if_unbound))
                    .setPositiveButton(sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pwdDialog.show();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();

        }

        sureDialog.show();
    }

    private void login(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_CODE);
    }

    private void logout(){
        MyPreference.getInstance().setNameAndPwd(this, "", "");
        MyPreference.getInstance().setBound(this, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK){
            Logger.d("activity result return fail");
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
        }else if (requestCode == BOUND_MOBILE_CODE){
            Bundle bundle = data.getExtras();
            boolean res = bundle.getBoolean(RegisterActivity.RES);
            if (res){
                String mobile = bundle.getString(RegisterActivity.PHONE);
                String password = bundle.getString(RegisterActivity.PWD);
                uploadBoundMobile(mobile, password);
            }
        }else if (requestCode == SCHOOL_CODE){
            Bundle bundle = data.getExtras();
            handler.sendMessage(handler.obtainMessage(SCHOOL_CODE, bundle));
        }else if (requestCode == LOGIN_CODE){
            Bundle bundle = data.getExtras();
            Login login = (Login) bundle.getSerializable(LoginActivity.LOGIN_MES_TAG);
            String mobile = bundle.getString(LoginActivity.USER_NAME_TAG);
            String pwd = bundle.getString(LoginActivity.PWD_TAG);
            if (SuccessCheck.ifSuccess(login.getCode())){
                ifChangeInfo = true;
                loginText.setText(getResources().getString(R.string.logout));
                HttpData.userNum.setLength(0);
                HttpData.userNum.append(login.getUserNum());
                MyPreference.getInstance().setBound(this, true);
                MyPreference.getInstance().setNameAndPwd(this, mobile, pwd);
            }
//            handler.sendMessage(handler.obtainMessage());
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

    private void uploadBoundMobile(final String mobile, final String password){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                boolean res = DocNetwork.getInstance().boundMobile(HttpData.userNum.toString(), mobile, password);
                Bundle bundle = new Bundle();
                bundle.putString(BOUND_MOBILE_TAG, mobile);
                bundle.putString(PWD_TAG, password);
                bundle.putBoolean(UPLOAD_HEADIMG_RES_TAG, res);
                handler.sendMessage(handler.obtainMessage(BOUND_MOBILE_CODE, bundle));
            }
        });
    }

    private void uploadUnBoundMobile(final String mobile, final String password){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                boolean res = DocNetwork.getInstance().unBoundMobile(HttpData.userNum.toString(), mobile, password);
                handler.sendMessage(handler.obtainMessage(UNBOUND_MOBILE_CODE, res));
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
