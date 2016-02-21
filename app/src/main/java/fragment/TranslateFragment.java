package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import utils.Logger;
import zy.com.document.R;

/**
 * Created by zy on 16-2-20.
 */
public class TranslateFragment extends Fragment {

    private View rootView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyFragmentPagerAdapter fragmentPagerAdapter;

    private DownloadManagerFragment downloadManagerFragment;
    private UploadManagerFragment uploadManagerFragment;
    private List<Fragment> fragments;
    private List<String> title;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_translate, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView(){
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        title = new ArrayList<>();
        title.add(getResources().getString(R.string.download));
        title.add(getResources().getString(R.string.upload));

        downloadManagerFragment = new DownloadManagerFragment();
        uploadManagerFragment = new UploadManagerFragment();
        fragments = new ArrayList<>();
        fragments.add(downloadManagerFragment);
        fragments.add(uploadManagerFragment);
        fragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager(), fragments, title);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    public static class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> title;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> title) {
            super(fm);
            this.fragments = fragments;
            this.title = title;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }
}
