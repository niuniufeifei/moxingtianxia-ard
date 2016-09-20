package com.dgzd.mxtx.activity.mineView.userEvent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.tools.GlobalEntity;

/**
 * @version V1.0 <功能>
 * @FileName: EventActivity.java
 * @author: Jessica
 * @date: 2015-12-28 14:43
 */

public class EventDetailsActivity extends AppCompatActivity{
    private ImageView ivBack;
    private WebView wvDetail;
    private String detailContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getBundleData();
        initView();
        backClick();
    }

    private void getBundleData(){
        Intent intent = getIntent();
        detailContent = intent.getStringExtra(GlobalEntity.MOTO_TRIP_ACTIVITY_DETAIL);
    }

    private void initView(){
        ivBack = (ImageView)findViewById(R.id.iv_back);
        wvDetail = (WebView)findViewById(R.id.wv_detail);
        wvDetail.loadDataWithBaseURL(null, detailContent, "text/html", "utf-8", null);
    }

    private void backClick(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
