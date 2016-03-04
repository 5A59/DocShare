package zy.com.document;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import java.util.ArrayList;
import java.util.List;

import docnetwork.DocNetwork;
import docnetwork.dataobj.CollegeRes;
import docnetwork.dataobj.SchoolRes;
import docsadapter.TextAdapter;
import fileselecter.OnRecyclerItemClickListener;
import network.ThreadPool;
import utils.GeneralUtils;

/**
 * Created by zy on 16-2-26.
 */
public class SchoolAndCollegeSelecterActivity extends AppCompatActivity{
    public final static String SCHOOL_TAG = "school";
    public final static String COLLEGE_TAG = "college";
    private final static int SCHOOL_CODE = 0;
    private final static int COLLEGE_CODE = 1;
    private final static int UPLOAD_CODE = 2;

    private int cur;
    private PullToLoadView pullToLoadView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextAdapter textAdapter;

    private List<String> data;
    private boolean isLoading;

    private String school;
    private String college;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPLOAD_CODE){
                boolean res = (Boolean) msg.obj;
                if (res){
                    GeneralUtils.getInstance().myToast(SchoolAndCollegeSelecterActivity.this,
                            getResources().getString(R.string.upload_sch_col_success));
                    setRes(RESULT_OK);
                }else {
                    GeneralUtils.getInstance().myToast(SchoolAndCollegeSelecterActivity.this,
                            getResources().getString(R.string.upload_sch_col_fail));
                    setRes(RESULT_CANCELED);
                }
                finish();
                return;
            }

            pullToLoadView.setComplete();
            List<String> tmpData = (List<String>) msg.obj;
            data.clear();
            data.addAll(tmpData);
            textAdapter.notifyDataSetChanged();
            if (msg.what == SCHOOL_CODE){
                cur = SCHOOL_CODE;
            }else if (msg.what == COLLEGE_CODE){
                cur = COLLEGE_CODE;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sch_col_selecter);

        initToolBar();
        initData();
        initView();
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.settings));
        setSupportActionBar(toolbar);
    }

    private void initData(){
        data = new ArrayList<>();
        isLoading = false;
        cur = SCHOOL_CODE;
    }

    private void initView(){
        pullToLoadView = (PullToLoadView) this.findViewById(R.id.pullloadview);
        pullToLoadView.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
            }

            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return true;
            }
        });
        pullToLoadView.initLoad();

        recyclerView = pullToLoadView.getRecyclerView();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        textAdapter = new TextAdapter(this, data);
        textAdapter.setItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                if (cur == SCHOOL_CODE){
                    school = data.get(pos);
                    getCollege(school);
                }else if (cur == COLLEGE_CODE) {
                    college = data.get(pos);
                    uploadSchAndCol(school, college);
                }
            }
        });

        recyclerView.setAdapter(textAdapter);
    }

    private void refresh(){
        if (cur == SCHOOL_CODE){
            getSchool();
        }else if (cur == COLLEGE_CODE){
            getCollege(school);
        }
    }

    private void getSchool(){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                SchoolRes schoolRes = DocNetwork.getInstance().getSchool();
                List<String> data = new ArrayList<>();
                if (schoolRes != null && schoolRes.getSchool() != null){
                    for (SchoolRes.School s : schoolRes.getSchool()){
                        data.add(s.getName());
                    }
                }
                handler.sendMessage(handler.obtainMessage(SCHOOL_CODE, data));
            }
        });
    }

    private void getCollege(final String school){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                CollegeRes collegeRes = DocNetwork.getInstance().getCollege(school);
                List<String> data = new ArrayList<>();
                if (collegeRes != null && collegeRes.getCollege() != null){
                    for (CollegeRes.College c : collegeRes.getCollege()){
                        data.add(c.getName());
                    }
                }
                handler.sendMessage(handler.obtainMessage(COLLEGE_CODE, data));
            }
        });
    }

    private void setRes(int res){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(SCHOOL_TAG, school);
        bundle.putString(COLLEGE_TAG, college);
        intent.putExtras(bundle);
        setResult(res, intent);
    }

    private void uploadSchAndCol(final String sch, final String col){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                boolean res = DocNetwork.getInstance().changeInfo("", "", sch, col, "");
                handler.sendMessage(handler.obtainMessage(UPLOAD_CODE, res));
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
