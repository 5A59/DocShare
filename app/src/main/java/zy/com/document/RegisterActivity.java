package zy.com.document;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import utils.GeneralUtils;

/**
 * Created by zy on 16-2-22.
 */
public class RegisterActivity extends AppCompatActivity{
    public static final String PHONE = "phone";
    public static final String RES = "res";
    public static final String PWD = "pwd";
    private static final String CHINA_CODE = "86";

    private static final int LEFT_SEC = 30;
    private static final int SHOW_TOAST = 0;
    private static final int SEND_SMS_TIMER = 1;

    private EditText mobileEdit;
    private EditText authEdit;
    private EditText pwdEdit;
    private Button sendAuthButton;
    private Button authSureButton;
    private EventHandler eh;

    private String mobile;
    private String auth;
    private String password;

    private int leftSec;

    private boolean res;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_TOAST){
                String content = (String) msg.obj;
                GeneralUtils.getInstance().myToast(RegisterActivity.this, content);
            }else if (msg.what == SEND_SMS_TIMER){
                leftSec -= 1;
                sendAuthButton.setText("" + leftSec + " s");
                if (leftSec <= 0){
                    sendAuthButton.setClickable(true);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initToolBar();
        initView();
        initSms();
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
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
        res = false;
        leftSec = LEFT_SEC;

        mobileEdit = (EditText) this.findViewById(R.id.edit_mobile);
        authEdit = (EditText) this.findViewById(R.id.edit_auth);
        pwdEdit = (EditText) this.findViewById(R.id.edit_pwd);
        sendAuthButton = (Button) this.findViewById(R.id.button_send_auth);
        authSureButton = (Button) this.findViewById(R.id.button_auth_sure);

        sendAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = mobileEdit.getText().toString();
                Pattern pattern = Pattern.compile("[1][0-9]{10}");
                Matcher matcher = pattern.matcher(mobile);
                if (matcher.matches()){
                    SMSSDK.getVerificationCode(CHINA_CODE, mobile);
                }else {
                    GeneralUtils.getInstance().myToast(RegisterActivity.this,
                            getResources().getString(R.string.no_mobile));
                }
                Timer timer = new Timer();
                sendAuthButton.setClickable(false);
                leftSec = LEFT_SEC;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(SEND_SMS_TIMER);
                    }
                }, 0, 1000);
            }
        });

        authSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = authEdit.getText().toString();
                password = pwdEdit.getText().toString();
                if (auth != null && !auth.isEmpty()
                        && mobile != null && !mobile.isEmpty() && password != null && !password.isEmpty()){
                    SMSSDK.submitVerificationCode(CHINA_CODE, mobile, auth);
                }else {
                    GeneralUtils.getInstance().myToast(RegisterActivity.this,
                            getResources().getString(R.string.no_auth));
                }
            }
        });
    }

    private void initSms(){
        eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        HashMap<String, Object> d = (HashMap<String, Object>) data;
                        if (d.get(PHONE).equals(mobile)){
                            handler.sendMessage(handler.obtainMessage(SHOW_TOAST,
                                    getResources().getString(R.string.auth_success)));
                            res = true;
                            setRes();
                            finish();
                        }else {
                            handler.sendMessage(handler.obtainMessage(SHOW_TOAST,
                                    getResources().getString(R.string.auth_fail)));
                        }
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        handler.sendMessage(handler.obtainMessage(SHOW_TOAST,
                                getResources().getString(R.string.auth_sended)));
                    }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                    handler.sendMessage(handler.obtainMessage(SHOW_TOAST,
                            getResources().getString(R.string.auth_fail)));
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void setRes(){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(PHONE, mobile);
        bundle.putString(PWD, password);
        bundle.putBoolean(RES, res);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void finish() {
        super.finish();
        if (eh != null){
            SMSSDK.unregisterAllEventHandler(); //注册短信回调
        }
    }
}
