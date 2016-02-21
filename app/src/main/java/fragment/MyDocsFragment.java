package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;
import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import docnetwork.DocNetwork;
import docnetwork.dataobj.Doc;
import docsadapter.DocAdapter;
import fileselecter.OnRecyclerItemClickListener;
import network.ThreadPool;
import utils.Logger;
import zy.com.document.DetailDocActivity;
import zy.com.document.R;
import zy.com.document.UploadDocActivity;

/**
 * Created by zy on 16-1-2.
 */
public class MyDocsFragment extends Fragment {
    private final int LOADMORE = 0;
    private final int REFRESH = 1;

    private PullToLoadView pullToLoadView;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private RecyclerView.LayoutManager layoutManager;

    private View rootView;

    private DocAdapter docAdapter;
    private Doc doc;

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
                    doc.reAddDocMes(tmpDoc);
                    break;
            }
            docAdapter.notifyDataSetChanged();
        }
    };

    public MyDocsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_docs, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    private void initData(){
        doc = new Doc();
        page = 1;
        isLoading = false;
        hasLoadAll = false;
    }

    private void initView(){
        pullToLoadView = (PullToLoadView) rootView.findViewById(R.id.pullloadview);
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
        pullToLoadView.initLoad();

        recyclerView = pullToLoadView.getRecyclerView();
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadDocActivity.class);
                startActivity(intent);
            }
        });
        docAdapter = new DocAdapter(this.getActivity(), doc);
        docAdapter.setItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), DetailDocActivity.class);
                intent.putExtra("doc", doc.getDoc().get(pos));
                startActivity(intent);
            }
        });

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(docAdapter);
        recyclerView.setOnTouchListener(new ShowHideOnScroll(floatingActionButton));
    }

    private void getDoc(final int what){
        isLoading = true;

        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Doc doc = DocNetwork.getInstance().getMyDoc(page);
                handler.sendMessage(handler.obtainMessage(what, doc));
            }
        });

    }

    private void loadMore(){
        getDoc(0);
    }

    private void refresh(){
        page = 1;
        getDoc(1);
    }
}
