package com.dgzd.mxtx.activity.mineView.shopOrder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dgzd.mxtx.R;

/**
 * @version V1.0 <发布商品>
 * @FileName: PosTradeActivity.java
 * @author: Jessica
 * @date: 2015-12-28 14:43
 */

public class PosTradeActivity extends AppCompatActivity{
    private ImageView ivBack;
    private RelativeLayout layoutProductDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_trade);
        initView();
        backClick();
        productDescriptionClick();
    }

    private void initView(){
        ivBack = (ImageView)findViewById(R.id.iv_back);
        layoutProductDescription = (RelativeLayout)findViewById(R.id.layout_product_description);
    }

    private void backClick(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void productDescriptionClick(){
        layoutProductDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PosTradeActivity.this, ProductDescriptionActivity.class));
            }
        });
    }
}
