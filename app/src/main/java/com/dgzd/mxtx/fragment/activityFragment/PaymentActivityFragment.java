package com.dgzd.mxtx.fragment.activityFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.adapter.PaymentAdapter;
import com.dgzd.mxtx.entirety.PaymentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PaymentActivityFragment extends Fragment {

    private View view;
    private ImageView ivBack;
    private ListView lvPayment;
    private List<PaymentInfo> paymentInfos;
    private TextView tvConfirmPayment;

    public PaymentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_payment_activity, container, false);

        initView();
        backClick();
        confirmPaymentClick();
        return view;
    }

    private void initView() {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        lvPayment = (ListView) view.findViewById(R.id.lv_payment);
        tvConfirmPayment = (TextView) view.findViewById(R.id.tv_confirm_payment);
        getPaymentData();
        PaymentAdapter adapter = new PaymentAdapter(getActivity(), paymentInfos, 0);
        lvPayment.setAdapter(adapter);
    }

    private void getPaymentData() {
        paymentInfos = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            PaymentInfo info = new PaymentInfo();
            info.setPaymentName(getString(R.string.alipay_pay));
            info.setCharge("100");
            paymentInfos.add(info);
        }
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void confirmPaymentClick() {
        tvConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 支付吧！！！
            }
        });
    }
}
