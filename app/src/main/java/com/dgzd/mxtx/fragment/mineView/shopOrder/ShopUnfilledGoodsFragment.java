package com.dgzd.mxtx.fragment.mineView.shopOrder;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.mineView.shopOrder.LogisticsActivity;

/**
 * @version V1.0 <功能>
 * @FileName: ShopUnfilledGoodsFragment.java
 * @author: Jessica
 * @date: 2015-12-29 17:33
 */

public class ShopUnfilledGoodsFragment extends Fragment {
    private View view;
    private TextView tvSendOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop_order_unfilled_goods, container, false);
        initView();
        sendOutClick();
        return view;
    }

    private void initView(){
        tvSendOut = (TextView)view.findViewById(R.id.tv_send_out);
    }

    private void sendOutClick(){
        tvSendOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LogisticsActivity.class ));
            }
        });
    }
}
