package zy.com.document;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;

import docnetwork.DocNetwork;
import docnetwork.HttpData;
import docnetwork.SuccessCheck;
import docnetwork.dataobj.Login;
import network.LoadingMes;
import network.MyDownloadManager;
import network.ThreadPool;

/**
 * Created by zy on 16-1-6.
 */
public class LoginActivity extends AppCompatActivity{
    public static final String LOGIN_MES_TAG = "loginmes";
    public static final String USER_NAME_TAG = "username";
    public static final String PWD_TAG = "pwd";
    private static final int LOGIN_ID = 0;

    private EditText userNameEdit;
    private EditText pwdEdit;
    private Button loginButton;

    private Login loginMes;

    private Boolean logining;

    private String userName;
    private String pwd;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            logining = false;
            if (msg.what == LOGIN_ID){
                loginMes = (Login) msg.obj;
                if (SuccessCheck.ifSuccess(loginMes.getCode())){
                    HttpData.loginSuccess = true;
                }else {
                    HttpData.loginSuccess = false;
                }
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.putExtra("login", loginMes);
//                startActivity(intent);
                setRes(loginMes);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        init();
    }

    private void init(){
        initHttpCookie();
        logining = false;

        userNameEdit = (EditText) this.findViewById(R.id.edit_username);
        pwdEdit = (EditText) this.findViewById(R.id.edit_password);
        userNameEdit.setText("18840829553");
        pwdEdit.setText("zhangyi5");

        loginButton = (Button) this.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, TestActivity.class);
//                startActivity(intent);
                if (!logining){
                    logining = true;
                    userName = userNameEdit.getText().toString();
                    pwd = pwdEdit.getText().toString();

                    login(userName, pwd);
                }
            }
        });
    }

    private void initHttpCookie(){
        DocNetwork.getInstance().setCookie(this);
    }

    private void login(final String userName, final String pwd){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Login loginMes = DocNetwork.getInstance().login(userName, pwd);
                handler.sendMessage(handler.obtainMessage(LOGIN_ID, loginMes));
            }
        });
    }

    private void setRes(Login loginMes){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOGIN_MES_TAG, loginMes);
        bundle.putString(USER_NAME_TAG, userName);
        bundle.putString(PWD_TAG, pwd);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }
}

