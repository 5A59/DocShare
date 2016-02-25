package zy.com.document;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import utils.GeneralUtils;

/**
 * Created by zy on 16-2-22.
 */
public class RegisterActivity extends AppCompatActivity{
    private static final String PHONE = "phone";
    private static final String CHINA_CODE = "86";
    private static final String RES = "res";

    private EditText mobileEdit;
    private EditText authEdit;
    private Button sendAuthButton;
    private Button authSureButton;
    private EventHandler eh;

    private String mobile;
    private String auth;

    private boolean res;

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

        mobileEdit = (EditText) this.findViewById(R.id.edit_mobile);
        authEdit = (EditText) this.findViewById(R.id.edit_auth);
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
            }
        });

        authSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = authEdit.getText().toString();
                if (auth != null && !auth.isEmpty() && mobile != null && !mobile.isEmpty()){
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
//                            GeneralUtils.getInstance().myToast(RegisterActivity.this,
//                                    getResources().getString(R.string.auth_success));
                            res = true;
                            setRes();
                            finish();
                        }else {
//                            GeneralUtils.getInstance().myToast(RegisterActivity.this,
//                                    getResources().getString(R.string.auth_fail));
                        }
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
//                        GeneralUtils.getInstance().myToast(RegisterActivity.this,
//                                getResources().getString(R.string.auth_sended));
                    }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                    GeneralUtils.getInstance().myToast(RegisterActivity.this,
                            getResources().getString(R.string.auth_fail));
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void setRes(){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(PHONE, mobile);
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
