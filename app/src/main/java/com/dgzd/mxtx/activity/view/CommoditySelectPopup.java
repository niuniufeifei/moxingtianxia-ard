package com.dgzd.mxtx.activity.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.shoppingCart.ShoppingCartActivity;

/**
 * @version V1.0 <功能>
 * @FileName: CommoditySelectPopup.java
 * @author: Jessica
 * @date: 2015-11-27 15:17
 */

public class CommoditySelectPopup extends Activity{

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private TextView tvCancel, tvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_commodity_select_popwindow);
        initView();
    }

    public void initView(){
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        //取消按钮
        tvCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                finish();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(CommoditySelectPopup.this, ShoppingCartActivity.class));
                //销毁弹出框
                finish();
            }
        });
    }


}
