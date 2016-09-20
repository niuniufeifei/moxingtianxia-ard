package com.dgzd.mxtx.activity.mineView.shopOrder;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.fragment.mineView.shopOrder.ShopNotReceivingFragment;
import com.dgzd.mxtx.fragment.mineView.shopOrder.ShopToEvaluateFragment;
import com.dgzd.mxtx.fragment.mineView.shopOrder.ShopUnfilledGoodsFragment;
import com.dgzd.mxtx.fragment.mineView.shopOrder.ShopUnpaidFragment;

/**
 * @version V1.0 <订单管理>
 * @FileName: OrderManagementActivity.java
 * @author: Jessica
 * @date: 2015-12-28 14:43
 */

public class OrderManagementActivity extends AppCompatActivity{
    private static final int[][] USER_ORDER_INFO = new int[][]{ {R.string.unpaid, R.id.layout_my_event}, {R.string.unfilled_goods, R.id.layout_my_event},
            {R.string.not_receiving, R.id.layout_my_event}, {R.string.to_evaluate, R.id.layout_my_event}};

    private Class fragmentArray[] = { ShopUnpaidFragment.class, ShopUnfilledGoodsFragment.class, ShopNotReceivingFragment.class, ShopToEvaluateFragment.class};

    private ImageView ivBack;
    private FragmentTabHost tabHost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        backClick();
        initFragments();
    }

    private void initView(){
        ivBack = (ImageView)findViewById(R.id.iv_back);
    }

    private void backClick(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initFragments(){
        tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (int i=0; i<USER_ORDER_INFO.length; ++i){
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(getString(USER_ORDER_INFO[i][0])).setIndicator(getString(USER_ORDER_INFO[i][0]));
            tabHost.addTab(tabSpec, fragmentArray[i], null);
        }

    }
}
