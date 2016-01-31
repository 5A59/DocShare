package zy.com.document;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import docnetwork.DocNetwork;
import docnetwork.dataobj.Login;
import network.MyDownloadManager;
import network.ThreadPool;

/**
 * Created by zy on 16-1-6.
 */
public class LoginActivity extends AppCompatActivity{
    private static final int LOGIN_ID = 0;

    private EditText userNameEdit;
    private EditText pwdEdit;
    private Button loginButton;

    private Login loginMes;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOGIN_ID){
                loginMes = (Login) msg.obj;
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("login", loginMes);
                startActivity(intent);
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
                String userName = userNameEdit.getText().toString();
                String pwd = pwdEdit.getText().toString();

                login(userName, pwd);
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
}

