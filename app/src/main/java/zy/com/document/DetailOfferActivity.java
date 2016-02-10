package zy.com.document;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shamanland.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import docnetwork.DocNetwork;
import docnetwork.dataobj.Answer;
import docnetwork.dataobj.OfferReword;
import docsadapter.DetailOfferAdapter;
import network.ThreadPool;
import utils.Logger;

/**
 * Created by zy on 15-12-29.
 */
public class DetailOfferActivity extends AppCompatActivity {
    private final static int ANSWER = 0;
    private final static int GET_ANSWER = 1;
    private final static int REGET_ANSWER = 2;

    private RelativeLayout layoutMain;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;
    private DetailOfferAdapter detailAdapter;

    private FloatingActionButton floatingActionButton;
    private PopupWindow ansWindow;
    private EditText ansEdit;
    private Button sendButton;
    private View ansView;
    private Animation dismissAnim;
    private Animation showAnim;
    private List<Answer.AnsMes> ansMesList;

    private OfferReword.OfferMes offerMes;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ANSWER:
                    if ((boolean) msg.obj){
                        showToast("发表成功");
                        getAns(true);
                    }else {
                        showToast("sorry , 发表失败");
                    }
                    break;
                case REGET_ANSWER:
                    ansMesList.clear();
                case GET_ANSWER:
                    Answer answer = (Answer) msg.obj;
                    ansMesList.addAll(answer.getAns());
                    detailAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_offer);

        init();
    }

    private void init(){
        initData();
        initToolBar();
        initView();
        initAnim();
        getAns(false);
    }

    private void initData(){
        offerMes = (OfferReword.OfferMes) this.getIntent().getSerializableExtra("offer");
        ansMesList = new ArrayList<>();
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        layoutMain = (RelativeLayout) this.findViewById(R.id.layout_detail_offer);

        collapsingToolbarLayout = (CollapsingToolbarLayout) this.findViewById(R.id.coll);
        collapsingToolbarLayout.setTitle(offerMes.getTitle());
        recyclerView = (RecyclerView) this.findViewById(R.id.recycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        detailAdapter = new DetailOfferAdapter(this, offerMes.getContent(), ansMesList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(detailAdapter);

        floatingActionButton = (FloatingActionButton) this.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissFab();
            }
        });
    }

    private void initAnim(){
        dismissAnim = AnimationUtils.loadAnimation(this, R.anim.right_dismiss);
        dismissAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showAnsWindow();
//                dismissFab();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        showAnim = AnimationUtils.loadAnimation(this, R.anim.right_show);
    }

    private void showAnsWindow(){
        if (ansWindow == null){
            ansView = LayoutInflater.from(this).inflate(R.layout.upwindow_ans, null);
            ansEdit = (EditText) ansView.findViewById(R.id.edit_ans);
            ansEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0){
                        ableSend();
                    }else {
                        unableSend();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            sendButton = (Button) ansView.findViewById(R.id.button_send);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendAns(ansEdit.getText().toString());
                    ansWindow.dismiss();
                }
            });

            ansWindow = new PopupWindow(ansView);
            ansWindow.setWidth(layoutMain.getWidth());
            ansWindow.setHeight(100);
            ansWindow.setBackgroundDrawable(new BitmapDrawable());
            ansWindow.setFocusable(true);
            ansWindow.setOutsideTouchable(true);
            ansWindow.setAnimationStyle(R.style.popup_window_anim);
            ansWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            ansWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    showFab();
                }
            });
        }
        ansWindow.showAtLocation(layoutMain, Gravity.BOTTOM, 0, 0);
        ansWindow.update();
    }

    private void unableSend(){
        setSendButton(R.drawable.round_button_unable, R.color.unableText, false);
    }

    private void ableSend(){
        setSendButton(R.drawable.round_button, R.color.white, true);
    }

    private void setSendButton(int back, int color, boolean click){
        sendButton.setBackgroundResource(back);
        sendButton.setTextColor(getResources().getColor(color));
        sendButton.setClickable(click);
    }

    private void showAns(){
        //显示评论的操作具体在animationend中
    }

    private void dismissFab(){
        floatingActionButton.startAnimation(dismissAnim);
        floatingActionButton.setVisibility(View.GONE);
    }

    private void showFab(){
        floatingActionButton.startAnimation(showAnim);
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    private void sendAns(final String content){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                boolean res = DocNetwork.getInstance().answer(offerMes.getOfferId(), content, null);
                handler.sendMessage(handler.obtainMessage(ANSWER, res));
            }
        });
    }

    private void getAns(final boolean reGet){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Answer answer = DocNetwork.getInstance().getAnswer(1, offerMes.getOfferId());
                if (reGet){
                    handler.sendMessage(handler.obtainMessage(REGET_ANSWER, answer));
                }else {
                    handler.sendMessage(handler.obtainMessage(GET_ANSWER, answer));
                }
            }
        });
    }

    private void showToast(String content){
        Toast.makeText(DetailOfferActivity.this, content, Toast.LENGTH_SHORT).show();
    }

}
