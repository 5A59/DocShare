package zy.com.document;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import docnetwork.dataobj.OfferReword;

/**
 * Created by zy on 15-12-29.
 */
public class DetailOfferActivity extends AppCompatActivity{

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private TextView contentText;

    private OfferReword.OfferMes offerMes;

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
    }

    private void initData(){
        offerMes = (OfferReword.OfferMes) this.getIntent().getSerializableExtra("offer");
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
        collapsingToolbarLayout = (CollapsingToolbarLayout) this.findViewById(R.id.coll);
        collapsingToolbarLayout.setTitle(offerMes.getTitle());

        contentText = (TextView) this.findViewById(R.id.text_content);
        contentText.setText(offerMes.getContent());
    }
}
