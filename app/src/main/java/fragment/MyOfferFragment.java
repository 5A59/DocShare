package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import docnetwork.dataobj.OfferReword;
import docsadapter.OfferAdapter;
import fileselecter.OnRecyclerItemClickListener;
import network.ThreadPool;
import zy.com.document.DetailOfferActivity;
import zy.com.document.R;
import zy.com.document.UploadOfferActivity;

/**
 * Created by zy on 16-1-2.
 */
public class MyOfferFragment extends Fragment {
    private final int LOADMORE = 0;
    private final int REFRESH = 1;

    private PullToLoadView pullToLoadView;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private RecyclerView.LayoutManager layoutManager;

    private OfferAdapter offerAdapter;
    private OfferReword offer;

    private View rootView;

    private String school;
    private String college;
    private int page;
    private boolean isLoading;
    private boolean hasLoadAll;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            OfferReword tmpOffer = (OfferReword) msg.obj;
            pullToLoadView.setComplete();
            isLoading = false;
            if (tmpOffer != null && tmpOffer.getOffer() != null && !tmpOffer.getOffer().isEmpty()){
                page ++;
            }
            switch (msg.what){
                case LOADMORE:
                    offer.appendOffer(tmpOffer);
                    break;
                case REFRESH:
                    offer.reAddOffer(tmpOffer);
                    break;
            }
            offerAdapter.notifyDataSetChanged();
        }
    };

    public MyOfferFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_offer, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    private void initData(){
        offer = new OfferReword();
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
                Intent intent = new Intent(getActivity(), UploadOfferActivity.class);
                startActivity(intent);
            }
        });
        offerAdapter = new OfferAdapter(this.getActivity(), offer);
        offerAdapter.setItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), DetailOfferActivity.class);
                intent.putExtra("offer", offer.getOffer().get(pos));
                startActivity(intent);
            }
        });

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(offerAdapter);
        recyclerView.setOnTouchListener(new ShowHideOnScroll(floatingActionButton));
    }

    private void getOffer(final int what){
        isLoading = true;

        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                OfferReword offer = DocNetwork.getInstance().getMyOffer(page);
                handler.sendMessage(handler.obtainMessage(what, offer));
            }
        });

    }

    private void loadMore(){
        getOffer(0);
    }

    private void refresh(){
        page = 1;
        getOffer(1);
    }
}
