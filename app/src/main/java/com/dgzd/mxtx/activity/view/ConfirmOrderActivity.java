package com.dgzd.mxtx.activity.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;

public class ConfirmOrderActivity extends AppCompatActivity {
    private ImageView ivBack;
    private RelativeLayout layoutAddress;
    private TextView tvSubmitOrder;
    private RequestQueue requestQueue;
    private final String requestTag = "ConfirmOrderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        requestQueue = Volley.newRequestQueue(this);
        getConfirmData();
        initView();
        backClick();
        addressClick();
        submitOrderClick();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        layoutAddress = (RelativeLayout) findViewById(R.id.layout_address);
        tvSubmitOrder = (TextView) findViewById(R.id.tv_submit_order);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addressClick() {
        layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmOrderActivity.this, AddressManagementActivity.class));
            }
        });
    }

    private void submitOrderClick() {
        tvSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmOrderActivity.this, PaymentActivity.class));
            }
        });
    }

    private void getConfirmData(){
        Intent intent = getIntent();
        int uid = MxtxApplictaion.getInstance().personalInfo.getUid();
        String selectedCartItemLists = intent.getStringExtra("selectedCartItemLists");
        String strselectedCartItemListsUrl = String.format(GlobalEntity.ShoppingCartConfirmOrder, selectedCartItemLists, uid);
        StringRequest listRequest = new StringRequest(strselectedCartItemListsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
