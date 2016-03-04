package zy.com.document;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import docnetwork.dataobj.Login;
import utils.MyPreference;

/**
 * Created by zy on 16-2-28.
 */
public class FirstIntroActivity extends AppCompatActivity{
    private static final int PAGE_NUM = 3;
    private static final int SCH_COL_CODE = 0;
    private static final int ROUNG_D = 20;

    private ViewPager viewPager;
    private RoundPageAdapter pageAdapter;
    private LinearLayout roundLayout;
    private Button enterButton;
    private Button roundButton1;
    private Button roundButton2;
    private Button roundButton3;
    private List<ImageView> imgList;
    private List<Button> roundList;

    private Login login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_intro);

        initData();
        initView();
    }

    private void initData(){
        login = (Login) getIntent().getSerializableExtra(MainActivity.LOGIN_MES);

        imgList = new ArrayList<>();
        roundList = new ArrayList<>();
    }

    private void initView(){
        enterButton = (Button) this.findViewById(R.id.button_enter);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstIntroActivity.this, SchoolAndCollegeSelecterActivity.class);
                startActivityForResult(intent, SCH_COL_CODE);
            }
        });
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        roundLayout = (LinearLayout) this.findViewById(R.id.layout_round);

        roundButton1 = (Button) this.findViewById(R.id.button_round_1);
        roundButton2 = (Button) this.findViewById(R.id.button_round_2);
        roundButton3 = (Button) this.findViewById(R.id.button_round_3);
        roundList.add(roundButton1);
        roundList.add(roundButton2);
        roundList.add(roundButton3);

        for (int i = 0 ; i < PAGE_NUM ; i ++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.mipmap.def_headimg);
            imgList.add(imageView);
        }

        pageAdapter = new RoundPageAdapter(imgList);
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0 ; i < roundList.size() ; i ++){
                    Button button = roundList.get(i);
                    if (i == position){
                        button.setBackgroundColor(getResources().getColor(R.color.white));
                    }else {
                        button.setBackgroundColor(getResources().getColor(R.color.splitLine));
                    }
                }
                if (position == PAGE_NUM - 1){
                    enterButton.setVisibility(View.VISIBLE);
                }else {
                    enterButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCH_COL_CODE){
            MyPreference.getInstance().setFirst(this);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.LOGIN_MES, login);
            startActivity(intent);
            this.finish();
        }
    }

    public static class RoundPageAdapter extends PagerAdapter{

        private List<ImageView> imgList;

        public RoundPageAdapter(List<ImageView> imgList) {
            this.imgList = imgList;
        }

        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imgList.get(position));
            return imgList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imgList.get(position));
        }
    }
}
