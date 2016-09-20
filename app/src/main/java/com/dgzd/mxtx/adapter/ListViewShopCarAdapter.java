package com.dgzd.mxtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.custom.AddAndSubView;
import com.dgzd.mxtx.entirety.CartItem;
import com.dgzd.mxtx.entirety.SelectedOrderProduct;
import com.dgzd.mxtx.entirety.ShopCarItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nsd on 16/03/04.
 */
public class ListViewShopCarAdapter extends BaseExpandableListAdapter {
    private Context context;
    ViewHolder childholder;
    int tag = 0;
    List<CheckBox> groupcheckBoxes = new ArrayList<>();
    List<CheckBox> childcheckBoxes = new ArrayList<>();
    private List<ShopCarItem> group = Collections.emptyList(); // 数据集合;

    public ListViewShopCarAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return group.get(groupPosition).getCartItemList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return group.get(groupPosition).getCartItemList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<ShopCarItem> data) {
        this.group = data;
    }

    /**
     * 显示：group
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder groupholder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listitem_shopcargroup, null);
            groupholder = new ViewHolder();
            groupholder.allCheckBox = (CheckBox) convertView.findViewById(R.id.is_check_box);
            groupholder.tvShopName = (TextView) convertView.findViewById(R.id.tv_shop_name);
            convertView.setTag(groupholder);
        } else {
            groupholder = (ViewHolder) convertView.getTag();
        }
        ShopCarItem schedule = group.get(groupPosition);
        groupholder.tvShopName.setText(schedule.getStoreInfo().getName());
        /////checkbox
        groupholder.allCheckBox.setTag(R.id.tag_first, schedule.getStoreInfo().getName());
        groupholder.allCheckBox.setTag(R.id.tag_second, String.valueOf(schedule.getCartItemList().size()));
        groupholder.allCheckBox.setTag(R.id.tag_third,0);
        groupcheckBoxes.add(groupholder.allCheckBox);
        groupholder.allCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果被选中，遍历所有子checkbox ，当第一个tag相等时，则为一组，都选中；
                    for (CheckBox box : childcheckBoxes) {
                        if (groupholder.allCheckBox.getTag(R.id.tag_first).toString().equals(box.getTag(R.id.tag_first).toString())) {
                            box.setChecked(true);
                        }
                    }
                } else {
                    if (buttonView.getTag(R.id.tag_third).toString().equals("0")) {
                        for (CheckBox box : childcheckBoxes) {//同上 ，全部取消选中
                            if (groupholder.allCheckBox.getTag(R.id.tag_first).toString().equals(box.getTag(R.id.tag_first).toString())) {
                                box.setChecked(false);
                            }
                        }
                    } else {
                        buttonView.setTag(R.id.tag_third,0);

                    }

                }
            }
        });
        return convertView;
    }
    /**
     * 显示：child
     */
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.shopping_commodity_list_detail_item, null);
            childholder = new ViewHolder();
            childholder.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
            childholder.ivCommodityIcon = (ImageView) convertView.findViewById(R.id.iv_commodity_icon);
            childholder.tvCommodityName = (TextView) convertView.findViewById(R.id.tv_commodity_name);
            childholder.addandsubviewPurchase = (AddAndSubView) convertView.findViewById(R.id.addandsubview_purchase);
            childholder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(childholder);
        } else {
            childholder = (ViewHolder) convertView.getTag();
        }
        final ShopCarItem schedule = group.get(groupPosition);
        CartItem child = schedule.getCartItemList().get(childPosition);
        SelectedOrderProduct orderProductInfo = child.getItem().getOrderProductInfo();
        Picasso.with(context).load(orderProductInfo.getShowImg()).error(R.drawable.shop_photo_default).into(childholder.ivCommodityIcon);
        childholder.tvCommodityName.setText(orderProductInfo.getName());
        childholder.addandsubviewPurchase.setNum(orderProductInfo.getBuyCount());
        childholder.addandsubviewPurchase.setMaxNum(orderProductInfo.getRealCount());
        childholder.cbSelect.setTag(R.id.tag_first, schedule.getStoreInfo().getName());
        childholder.cbSelect.setTag(R.id.tag_second,orderProductInfo.getPid());
        childcheckBoxes.add(childholder.cbSelect);
        childholder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    //用来记录，当前组子checkbox被选中的个数
                    int size = 0;
                    for (CheckBox checkBox : childcheckBoxes) {
                        //遍历所有子checkbox，如果第一个tag跟当前tag相等，之后判断是否选中，如果选中了，那么size则加
                        if (checkBox.getTag(R.id.tag_first).toString().equals(buttonView.getTag(R.id.tag_first).toString())) {
                            if (checkBox.isChecked()){
                                  size += 1;
                            }
                        }
                    }
                    for (CheckBox box : groupcheckBoxes) {
                        if (box.getTag(R.id.tag_first).toString().equals(buttonView.getTag(R.id.tag_first).toString())) {
                            if (box.getTag(R.id.tag_second).toString().equals(String.valueOf(size))){
                                box.setTag(R.id.tag_third,0);
                                box.setChecked(true);
                            }
                        }
                    }
                } else {
                    for (CheckBox box : groupcheckBoxes) {
                        if (box.getTag(R.id.tag_first).toString().equals(buttonView.getTag(R.id.tag_first).toString())) {
                            box.setTag(R.id.tag_third,1);
                            box.setChecked(false);
                        }
                    }
                }
            }
        });
        // double price = orderProductInfo.getShopPrice();
        // holder.tvPrice.setText(String.format("￥%d", price));
        return convertView;
    }

    class ViewHolder {
        public CheckBox allCheckBox;
        public TextView tvShopName;
        public CheckBox cbSelect;
        public ImageView ivCommodityIcon;
        public TextView tvCommodityName;
        public AddAndSubView addandsubviewPurchase;
        public TextView tvPrice;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
