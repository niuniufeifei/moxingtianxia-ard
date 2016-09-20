package com.dgzd.mxtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.custom.AddAndSubView;
import com.dgzd.mxtx.entirety.ShoppingCartItemInfo;
import com.dgzd.mxtx.entirety.ShoppingCartItemListItemInfo;
import com.dgzd.mxtx.interfaces.ShoppingCommodityInterface;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ShoppingCommodityItemAdapter extends BaseAdapter {
    private boolean isAllChecked = false;
    public ShoppingCommodityInterface listener;
    //    public List<ShoppingCardItemInfo> list;
    private ShoppingCartItemInfo shoppingCartItemInfo;
    private List<ShoppingCartItemListItemInfo> itemListItemInfoArray;
    private Context context;
    private LayoutInflater inflater;
    private RequestQueue requestQueue;
    private final String requestTag = "ShoppingCommodityItemAdapter";

    public ShoppingCommodityItemAdapter(Context context, ShoppingCartItemInfo shoppingCartItemInfo, ShoppingCommodityInterface listener) {
        super();
        requestQueue = Volley.newRequestQueue(context);
        this.shoppingCartItemInfo = shoppingCartItemInfo;
        this.itemListItemInfoArray = shoppingCartItemInfo.getItemListItemInfoArray();
        this.context = context;
        this.listener = listener;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return itemListItemInfoArray == null ? 0 : itemListItemInfoArray.size();
    }

    @Override
    public Object getItem(int location) {
        return itemListItemInfoArray.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ShoppingCartItemListItemInfo item = itemListItemInfoArray.get(position);
        //Item的位置
        final int listPosition = position;
        //这个记录item的id用于操作isCheckedMap来更新CheckBox的状态
        final int id = item.getOrderProductInfo().getRecordId();
//        final int id = item.getId();

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_commodity_list_detail_item, null);
            holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
            holder.ivCommodityIcon = (ImageView) convertView.findViewById(R.id.iv_commodity_icon);
            holder.tvCommodityName = (TextView) convertView.findViewById(R.id.tv_commodity_name);
            holder.addandsubviewPurchase = (AddAndSubView) convertView.findViewById(R.id.addandsubview_purchase);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShoppingCartItemListItemInfo itemListItemInfo = itemListItemInfoArray.get(position);
//        itemListItemInfo.isSelected();
        boolean isChecked = shoppingCartItemInfo.isCheckedMap.get(id);
        holder.cbSelect.setChecked(isChecked);
        Picasso.with(context).load(itemListItemInfo.getOrderProductInfo().getShowImg()).error(R.drawable.shop_photo_default).into(holder.ivCommodityIcon);
        holder.tvCommodityName.setText(itemListItemInfo.getOrderProductInfo().getName());
        holder.addandsubviewPurchase.setNum(itemListItemInfo.getOrderProductInfo().getBuyCount());
        holder.addandsubviewPurchase.setMaxNum(itemListItemInfo.getOrderProductInfo().getRealCount());
        int price = itemListItemInfo.getOrderProductInfo().getShopPrice();
        holder.tvPrice.setText(String.format("￥%d", price));
//        holder.deleteButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View paramView) {
//                //Log.d(TAG, "deletePosition="+listPosition+"");
//                //删除list中的数据
//                list.remove(listPosition);
//                //删除Map中对应选中状态数据
//                MxtxApplictaion.getInstance().isCheckedMap.remove(id);
//                //通知列表数据修改
//                notifyDataSetChanged();
//            }
//        });
        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!shoppingCartItemInfo.isChangedBySingleCheckbox){
                    if (isChecked) {
                        shoppingCartItemInfo.isCheckedMap.put(id, true);
                    } else {
                        shoppingCartItemInfo.isCheckedMap.put(id,false);
                    }
                    judgeCheckedStatus();
                }
            }
        });
        return convertView;
    }

    public void judgeCheckedStatus() {

        int size = shoppingCartItemInfo.isCheckedMap.size();
        int count = 0;
        if (size > 0) {
            for (boolean v : shoppingCartItemInfo.isCheckedMap.values()) {
                if (v) {
                    ++count;
                }
            }

            if (count == size) {
                isAllChecked = true;
            } else {
                isAllChecked = false;
            }
            listener.isAllChecked(isAllChecked);
        }
    }

    public final class ViewHolder {
        public CheckBox cbSelect;
        public ImageView ivCommodityIcon;
        public TextView tvCommodityName;
        public AddAndSubView addandsubviewPurchase;
        public TextView tvPrice;
    }


}
