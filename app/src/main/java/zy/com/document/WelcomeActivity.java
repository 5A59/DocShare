package zy.com.document;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import java.util.Timer;

import docnetwork.DocNetwork;
import docnetwork.HttpData;
import docnetwork.dataobj.Login;
import network.ThreadPool;
import utils.CmdUtils;
import utils.Logger;
import utils.MyPreference;

/**
 * Created by zy on 16-2-21.
 */
public class WelcomeActivity extends AppCompatActivity{

    private static int LOGIN_CODE = 0;
    private static int TIMER_CODE = 1;

    private Timer timer;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOGIN_CODE){
                Login login = (Login) msg.obj;
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.LOGIN_MES, login);
                startActivity(intent);
                finish();
            }else if (msg.what == TIMER_CODE){

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
    }

    private void init(){
        initView();

//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.sendEmptyMessage(TIMER_CODE);
//            }
//        }, 0, 1000);
        initHttpCookie();
        login();
    }

    private void initView(){

    }

    private void login(){
        Logger.d("login");
        if (MyPreference.getInstance().ifBound(this)){
            ThreadPool.getInstance().submit(new Runnable() {
                @Override
                public void run() {
                    String userName = MyPreference.getInstance().getUserName(WelcomeActivity.this);
                    String pwd = MyPreference.getInstance().getPwd(WelcomeActivity.this);
                    Login login = DocNetwork.getInstance().login(userName, pwd);
                    handler.sendMessage(handler.obtainMessage(LOGIN_CODE, login));
                }
            });
        }else {
            //获取设备id
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String deviceId = telephonyManager.getDeviceId();
            //获取mac地址
            String mac = CmdUtils.getInstance().getMac();

            final String userName = deviceId + mac;
            HttpData.userName.setLength(0);
            HttpData.userName.append(userName);

            ThreadPool.getInstance().submit(new Runnable() {
                @Override
                public void run() {
                    Login login = DocNetwork.getInstance().noBoundLogin(userName);
                    handler.sendMessage(handler.obtainMessage(LOGIN_CODE, login));
                }
            });
        }
    }

    private void initHttpCookie(){
        DocNetwork.getInstance().setCookie(this);
    }
}
