package zy.com.document;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import docnetwork.DocNetwork;
import docnetwork.dataobj.Doc;
import docsadapter.DocAdapter;
import fileselecter.OnRecyclerItemClickListener;
import network.ThreadPool;
import utils.GeneralUtils;
import utils.Logger;

/**
 * Created by zy on 16-2-19.
 */
public class SearchActivity extends AppCompatActivity{
    private final int LOADMORE = 0;
    private final int REFRESH = 1;

    private PullToLoadView pullToLoadView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EditText searchEdit;
    private TextView noResText;
    private ImageView backImg;
    private ImageView clearImg;

    private DocAdapter docAdapter;
    private Doc doc;

    private String school;
    private String college;
    private String content;

    private int page;
    private boolean isLoading;
    private boolean hasLoadAll;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Doc tmpDoc = (Doc) msg.obj;
            isLoading = false;
            pullToLoadView.setComplete();
            if (tmpDoc != null && tmpDoc.getDoc() != null && !tmpDoc.getDoc().isEmpty()){
                page ++;
            }
            switch (msg.what){
                case LOADMORE:
                    doc.appendDocMes(tmpDoc);
                    break;
                case REFRESH:
                    if (tmpDoc.getDoc().isEmpty()){
                        noResText.setVisibility(View.VISIBLE);
                        pullToLoadView.setVisibility(View.GONE);
                    }else {
                        noResText.setVisibility(View.GONE);
                        pullToLoadView.setVisibility(View.VISIBLE);
                    }
                    doc.reAddDocMes(tmpDoc);
                    break;
            }
            docAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        initData();
        initView();
        setClipper();
    }

    private void initData(){
        doc = new Doc();
        page = 1;
        isLoading = false;
        hasLoadAll = false;
    }

    private void initView(){
        pullToLoadView = (PullToLoadView) this.findViewById(R.id.pullloadview);
        pullToLoadView.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                loadMore();
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
                return hasLoadAll;
            }
        });
        pullToLoadView.isLoadMoreEnabled(true);

        recyclerView = pullToLoadView.getRecyclerView();

        docAdapter = new DocAdapter(this, doc);
        docAdapter.setItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Intent intent = new Intent(SearchActivity.this, DetailDocActivity.class);
                intent.putExtra("doc", doc.getDoc().get(pos));
                startActivity(intent);
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(docAdapter);

        searchEdit = (EditText) this.findViewById(R.id.edit_search);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    search();
                }
                return false;
            }
        });
        noResText = (TextView) this.findViewById(R.id.text_no_res);

        searchEdit.setFocusable(true);
        searchEdit.requestFocus();
//        GeneralUtils.getInstance().showSoftInput(searchEdit, this);

        backImg = (ImageView) this.findViewById(R.id.img_back);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clearImg = (ImageView) this.findViewById(R.id.img_clear);
        clearImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
                content = "";
            }
        });
    }

    private void setClipper(){
        String data = GeneralUtils.getInstance().getFromClipper(this);
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher matcher = pattern.matcher(data);
        if (matcher.matches()){
            searchEdit.setText(data);
            search();
        }
    }

    private void search(){
        GeneralUtils.getInstance().hideSoftInput(this);
        content = searchEdit.getText().toString();
        if (!content.isEmpty()){
            refresh();
        }else {
            GeneralUtils.getInstance().myToast(SearchActivity.this, "你在逗我?");
        }

    }

    private void getDoc(final int what, final String content){
        school = "大连理工大学";
        college = "软件学院";
        isLoading = true;

        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Doc doc = DocNetwork.getInstance().search(page, school, college, content);
                handler.sendMessage(handler.obtainMessage(what, doc));
            }
        });

    }

    private void loadMore(){
        getDoc(0, content);
    }

    private void refresh(){
        page = 1;
        getDoc(1, content);
    }
}
