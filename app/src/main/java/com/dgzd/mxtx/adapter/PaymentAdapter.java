package com.dgzd.mxtx.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.entirety.PaymentInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PaymentAdapter extends BaseAdapter {
    public static interface paymentAdapterListener {
        public void getSelectId(int id);
    }

    private paymentAdapterListener listener;
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private Context context;
    private static Map<Integer, Boolean> isSecleted;
    private List<PaymentInfo> paymentInfos = new ArrayList<PaymentInfo>();
    private int defaultId = 0;

    public PaymentAdapter() {
        super();
    }

    public PaymentAdapter(Context context, List<PaymentInfo> paymentInfos, int defaultId) {
        super();
        this.context = context;
        this.paymentInfos = paymentInfos;
        this.defaultId = defaultId;
//		this.listener = (addressAdapterListener)context;
        this.inflater = LayoutInflater.from(this.context);
        Log.e("count", "here");
        isSecleted = new HashMap<Integer, Boolean>();
        if (paymentInfos != null) {
            for (int i = 0; i < paymentInfos.size(); ++i) {
                if (defaultId == i) {
                    isSecleted.put(i, true);
                } else {
                    isSecleted.put(i, false);
                }
            }
        }
    }

    @Override
    public int getCount() {
        return paymentInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return paymentInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.payment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.layoutRoot = (RelativeLayout) convertView.findViewById(R.id.layout_root);
            viewHolder.ivPaymentSelectStatus = (ImageView) convertView.findViewById(R.id.iv_select_status);
            viewHolder.tvCharge = (TextView) convertView.findViewById(R.id.tv_charge);
            viewHolder.tvPaymentName = (TextView) convertView.findViewById(R.id.tv_payment_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvCharge.setText(paymentInfos.get(position).getCharge());
        viewHolder.tvPaymentName.setText(paymentInfos.get(position).getPaymentName());

        boolean is_selected = isSecleted.get(position);
        if (is_selected) {
            viewHolder.ivPaymentSelectStatus.setBackgroundResource(R.drawable.selected);
            viewHolder.tvCharge.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivPaymentSelectStatus.setBackgroundResource(R.drawable.unselect);
            viewHolder.tvCharge.setVisibility(View.INVISIBLE);
        }
        viewHolder.layoutRoot.setOnClickListener(new OnClickListenerImpl(position));

        return convertView;
    }

    class OnClickListenerImpl implements View.OnClickListener {
        int position;

        public OnClickListenerImpl(int positon) {
            super();
            this.position = positon;
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < isSecleted.size(); ++i) {
                isSecleted.put(i, false);
            }
            isSecleted.put(position, true);
            notifyDataSetChanged();
            defaultId = position;
//			listener.getSelectId(defaultId);
        }
    }

    private final class ViewHolder {
        private RelativeLayout layoutRoot;
        private ImageView ivPaymentSelectStatus;
        private TextView tvPaymentName;
        private TextView tvCharge;
    }
}