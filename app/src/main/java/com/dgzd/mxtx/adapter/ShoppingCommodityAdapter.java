package com.dgzd.mxtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.entirety.ShoppingCartInfo;
import com.dgzd.mxtx.entirety.ShoppingCartItemInfo;
import com.dgzd.mxtx.interfaces.ShoppingCommodityInterface;
import com.dgzd.mxtx.tools.MxtxApplictaion;

import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class ShoppingCommodityAdapter extends BaseAdapter {
    private ShoppingCommodityItemAdapter adapter;
    private Context context;
    private MxtxApplictaion myApp;
    private LayoutInflater inflater;
    private ViewHolder holder = null;
    private ShoppingCartInfo shoppingCartInfo;
    private List<ShoppingCartItemInfo> itemInfoList;

    public ShoppingCommodityAdapter(Context context, ShoppingCartInfo shoppingCartInfo) {
        super();
        myApp = MxtxApplictaion.getInstance();
        this.shoppingCartInfo = shoppingCartInfo;
        this.itemInfoList = shoppingCartInfo.getShoppingCartItemInfoArray();
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return itemInfoList == null ? 0 : itemInfoList.size();
    }

    @Override
    public Object getItem(int location) {
        return itemInfoList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_commodity_item, null);
            holder.allCheckBox = (CheckBox) convertView.findViewById(R.id.is_check_box);
            holder.tvShopName = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.lvShopCommodoty = (ListView) convertView.findViewById(R.id.lv_shop_commodity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int itemListCount = itemInfoList.get(position).getItemListItemInfoArray().size();
        int selectedCount = itemInfoList.get(position).getSelectedOrderProductArray().size();
//        if (itemListCount > 0) {
//            if (itemListCount == selectedCount)
//                holder.allCheckBox.setChecked(true);
//            else
//                holder.allCheckBox.setChecked(false);
//        }

        holder.tvShopName.setText(itemInfoList.get(position).getStoreInfo().getName());
        adapter = new ShoppingCommodityItemAdapter(context, itemInfoList.get(position), new ShoppingCommodityInterface() {
            @Override
            public void isAllChecked(boolean isOk) {
                itemInfoList.get(position).isChangedBySingleCheckbox = true;
                boolean isChecked = holder.allCheckBox.isChecked();
                if (isOk) {
                    if (!isChecked) {
                        holder.allCheckBox.setChecked(true);
                    }
                } else {
                    if (isChecked) {
                        holder.allCheckBox.setChecked(false);
                    }
                }
                itemInfoList.get(position).isChangedBySingleCheckbox = false;
            }
        });
        holder.lvShopCommodoty.setAdapter(adapter);
        holder.allCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (itemInfoList.get(position).isChangedBySingleCheckbox) {
                    itemInfoList.get(position).isChangedBySingleCheckbox = false;
                } else {
                    Set<Integer> set = itemInfoList.get(position).isCheckedMap.keySet();
                    Iterator<Integer> iterator = set.iterator();
                    if (isChecked) {
                        while (iterator.hasNext()) {
                            Integer keyId = iterator.next();
                            itemInfoList.get(position).isCheckedMap.put(keyId, true);
                        }
                    } else {
                        while (iterator.hasNext()) {
                            Integer keyId = iterator.next();
                            itemInfoList.get(position).isCheckedMap.put(keyId, false);
                        }
                    }
                    itemInfoList.get(position).isChangedBySingleCheckbox = false;

                    adapter.notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        public CheckBox allCheckBox;
        public TextView tvShopName;
        public ListView lvShopCommodoty;
    }

}
