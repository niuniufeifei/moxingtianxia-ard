package com.dgzd.mxtx.fragment.mineView.shopOrder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgzd.mxtx.R;

/**
 * @version V1.0 <功能>
 * @FileName: ShopUnpaidFragment.java
 * @author: Jessica
 * @date: 2015-12-29 17:33
 */

public class ShopUnpaidFragment extends Fragment {
    private View view;

    public ShopUnpaidFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop_order_unpaid, container, false);
        return view;
    }

}
