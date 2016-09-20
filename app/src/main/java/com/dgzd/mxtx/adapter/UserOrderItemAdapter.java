package com.dgzd.mxtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.entirety.UserOrderListInfo;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


public class UserOrderItemAdapter extends BaseAdapter {
    private List<UserOrderListInfo> commodityViewArray = Collections.emptyList();
    private Context context = null;
    private LayoutInflater mInflater;


    public UserOrderItemAdapter(List<UserOrderListInfo> commodityViewArray, Context context) {
        super();
        this.context = context;
        this.mInflater = LayoutInflater.from(this.context);
        this.commodityViewArray = commodityViewArray;
    }

    @Override
    public int getCount() {
        return commodityViewArray.size();
    }

    @Override
    public Object getItem(int position) {
        return commodityViewArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.custom_user_order_list_item, null);
            holder = new ViewHolder();
            holder.ivCommodity = (ImageView) convertView.findViewById(R.id.iv_commodity);
            holder.tvCommodityName = (TextView) convertView.findViewById(R.id.tv_commodity_name);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvPurchaserNumber = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String photoUrl = commodityViewArray.get(position).getImgUrl();
        Picasso.with(context).load(photoUrl).error(R.mipmap.ic_launcher).into(holder.ivCommodity);

        //holder.ivCommodity.setBackgroundResource(R.drawable.commodity);
        holder.tvCommodityName.setText(commodityViewArray.get(position).getCommodityName());
        holder.tvPrice.setText(commodityViewArray.get(position).getStrPrice());
        int purchaserNum = commodityViewArray.get(position).getPurchaserNum();
        if (purchaserNum > 0) {
            holder.tvPurchaserNumber.setVisibility(View.VISIBLE);
            String strFormat = context.getString(R.string.commodity_count);
            String strShow = String.format(strFormat, purchaserNum);
            holder.tvPurchaserNumber.setText(strShow);
        } else {
            holder.tvPurchaserNumber.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private final class ViewHolder {
        private ImageView ivCommodity;
        private TextView tvCommodityName;
        private TextView tvPrice;
        private TextView tvPurchaserNumber;
    }
}
