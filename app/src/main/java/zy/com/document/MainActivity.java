package zy.com.document;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import docnetwork.DocNetwork;
import docnetwork.HttpData;
import docnetwork.SuccessCheck;
import docnetwork.dataobj.Info;
import docnetwork.dataobj.Login;
import fragment.DocsFragment;
import fragment.DownloadFragment;
import fragment.DownloadManagerFragment;
import fragment.MyDocsFragment;
import fragment.MyOfferFragment;
import fragment.OfferFragment;
import network.ThreadPool;
import utils.GeneralUtils;
import utils.Logger;

public class MainActivity extends AppCompatActivity {

    private static final int INFO_RES_CODE = 0;
    private static final int DOC = 0;
    private static final int OFFER = 1;
    private static final int MYDOC = 2;
    private static final int MYOFFER = 3;
    private static final int DOWNLOAD = 4;
    private static final int DOWNLOAD_MANAGER = 5;
    private static final String DOC_TAG = "doc";
    private static final String OFFER_TAG = "offer";
    private static final String MYDOC_TAG = "my_doc";
    private static final String MYOFFER_TAG = "my_offer";
    private static final String DOWNLOAD_TAG = "download";
    private static final String DOWNLOAD_Manager_TAG = "download_manager";

    private int CUR = -1;
    private int FRAGMENT_ID = 0;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private TextView searchText;

    private TextView nameText;
    private CircleImageView headImg;

    private DocsFragment docsFragment;
    private OfferFragment offerFragment;
    private MyDocsFragment myDocsFragment;
    private MyOfferFragment myOfferFragment;
    private DownloadFragment downloadFragment;
    private DownloadManagerFragment downloadManagerFragment;

    private List<Fragment> fragmentList;

    private Login loginMes;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == INFO_RES_CODE){
                HttpData.info = (Info) msg.obj;
                setHeader(HttpData.info);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        initToolBar();
        initData();
        initFragment();
        initView();
        initInfo();
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOrOpenDrawer();
            }
        });
    }

    private void initData(){
        FRAGMENT_ID = R.id.fragment;
        loginMes = (Login) getIntent().getSerializableExtra("login");
    }

    private void initView(){
        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer);
        navigationView = (NavigationView) this.findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()){
                    case R.id.menu_doc:
                        startDoc();
                        break;
                    case R.id.menu_offer:
                        startOffer();
                        break;
                    case R.id.menu_my_doc:
                        startMyDoc();
                        break;
                    case R.id.menu_my_offer:
                        startMyOffer();
                        break;
                    case R.id.menu_download:
                        startDownload();
                        break;
                    case R.id.menu_trans:
                        startDownloadManager();
                        break;
                }
                closeOrOpenDrawer();
                return false;
            }
        });

        toggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);

        searchText = (TextView) this.findViewById(R.id.text_search);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        //init header
        nameText = (TextView) navigationView.findViewById(R.id.text_name);
        headImg = (CircleImageView) navigationView.findViewById(R.id.img_head);

        startDoc();
    }

    private void initInfo(){
        if (loginMes != null && SuccessCheck.ifSuccess(loginMes.getCode())){
            getInfo();
        }
    }

    private void initFragment(){
        docsFragment = new DocsFragment();
        offerFragment = new OfferFragment();
        myDocsFragment = new MyDocsFragment();
        myOfferFragment = new MyOfferFragment();
        downloadFragment = new DownloadFragment();
        downloadManagerFragment = new DownloadManagerFragment();
        fragmentList = new ArrayList<>();
//        fragmentList.add(docsFragment);
//        fragmentList.add(offerFragment);
//        fragmentList.add(myDocsFragment);
//        fragmentList.add(myOfferFragment);
//        fragmentList.add(downloadManagerFragment);

        addFragment(docsFragment);
        addFragment(offerFragment);
        addFragment(myDocsFragment);
        addFragment(myOfferFragment);
        addFragment(downloadFragment);
        addFragment(downloadManagerFragment);
    }

    private void setHeader(Info info){
        nameText.setText(info.getInf().getName());
        GeneralUtils.getInstance().setHeadImg(this, headImg, info.getInf().getHeadImgUrl());
    }

    private void closeOrOpenDrawer(){
        if (drawerLayout.isDrawerOpen(navigationView)){
            drawerLayout.closeDrawer(navigationView);
            return ;
        }
        drawerLayout.openDrawer(navigationView);
    }

    private void startDoc(){
        changeFragment(DOC);
    }

    private void startMyDoc(){
        changeFragment(MYDOC);
    }

    private void startOffer(){
        changeFragment(OFFER);
    }

    private void startMyOffer(){
        changeFragment(MYOFFER);
    }

    private void startDownload() {
        changeFragment(DOWNLOAD);
    }

    private void startDownloadManager(){
        changeFragment(DOWNLOAD_MANAGER);
    }

    private void changeFragment(int willBe){
        if (CUR == willBe){
            return ;
        }

        switch (willBe){
            case DOC:
                changeFragment(FRAGMENT_ID, docsFragment, DOC_TAG);
                break;
            case OFFER:
                changeFragment(FRAGMENT_ID, offerFragment, OFFER_TAG);
                break;
            case MYDOC:
                changeFragment(FRAGMENT_ID, myDocsFragment, MYDOC_TAG);
                break;
            case MYOFFER:
                changeFragment(FRAGMENT_ID, myOfferFragment, MYOFFER_TAG);
                break;
            case DOWNLOAD:
                changeFragment(FRAGMENT_ID, downloadFragment, DOWNLOAD_TAG);
                break;
            case DOWNLOAD_MANAGER:
                changeFragment(FRAGMENT_ID, downloadManagerFragment, DOWNLOAD_Manager_TAG);
                break;
        }
        CUR = willBe;
    }

    private void changeFragment(int id, Fragment fragment, String tag){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for (Fragment f : fragmentList){
            if (f.equals(fragment)){
                fragmentTransaction.show(f);
                continue;
            }
            fragmentTransaction.hide(f);
        }
        fragmentTransaction.commit();
    }

    private void addFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()){
            fragmentList.add(fragment);
            fragmentTransaction.add(FRAGMENT_ID, fragment);
        }
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    private void getInfo(){
        ThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                Info info = DocNetwork.getInstance().info(loginMes.getUserNum());
                handler.sendMessage(handler.obtainMessage(INFO_RES_CODE, info));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)){
            closeOrOpenDrawer();
            return ;
        }
        super.onBackPressed();
    }
}
