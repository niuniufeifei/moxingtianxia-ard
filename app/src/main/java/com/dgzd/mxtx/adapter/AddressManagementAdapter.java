package com.dgzd.mxtx.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.entirety.AddressInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddressManagementAdapter extends BaseAdapter {
	public interface addressAdapterListener {
		public void getSelectId(int id);
		public void getAddressModifyId(int id);
	}

	private addressAdapterListener listener;
	private ViewHolder viewHolder = null;
	private LayoutInflater inflater = null;
	private Context context;
	private static Map<Integer, Boolean> isSecleted;
	private List<AddressInfo> addressInfos = new ArrayList<AddressInfo>();
	private int defaultId = 0;

	public AddressManagementAdapter() {
		super();
	}

	public AddressManagementAdapter(Fragment context, List<AddressInfo> addressInfos, int defaultId) {
		super();
		this.context = context.getActivity();
		this.addressInfos = addressInfos;
		this.defaultId = defaultId;
		this.listener = (addressAdapterListener)context;
        this.inflater = LayoutInflater.from(this.context);
		Log.e("count", "here");
        isSecleted = new HashMap<Integer, Boolean>();
        if (addressInfos != null) {
            for (int i = 0; i < addressInfos.size(); ++i) {
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
		return addressInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return addressInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("test", "go here");
		viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.address_management_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvAddressTitle = (TextView) convertView.findViewById(R.id.tv_address_title);
			viewHolder.tvAddressModify = (TextView) convertView.findViewById(R.id.tv_address_modify);
			viewHolder.tvConsignee = (TextView) convertView.findViewById(R.id.tv_consignee);
			viewHolder.tvContactWay = (TextView) convertView.findViewById(R.id.tv_contact_way);
			viewHolder.tvPostCode = (TextView) convertView.findViewById(R.id.tv_post_code);
			viewHolder.tvShoppingAddress = (TextView) convertView.findViewById(R.id.tv_shopping_address);
			viewHolder.ivAddressSelectStatus = (ImageView) convertView.findViewById(R.id.iv_select_status);
			viewHolder.layoutAddress = (LinearLayout)convertView.findViewById(R.id.layout_default_address);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String strAddress = context.getResources().getString(R.string.address_num);
		String addressContent = String.format(strAddress, position+1);
		viewHolder.tvAddressTitle.setText(addressContent);
//		viewHolder.tvAddressModify;
		viewHolder.tvConsignee.setText(addressInfos.get(position).getConsignee());
		viewHolder.tvContactWay.setText(addressInfos.get(position).getContactWay());
		viewHolder.tvPostCode.setText(addressInfos.get(position).getPostCode());
		viewHolder.tvShoppingAddress.setText(addressInfos.get(position).getAddress());

		boolean is_selected = isSecleted.get(position);
		if (is_selected) {
			viewHolder.ivAddressSelectStatus.setBackgroundResource(R.drawable.selected);
		} else {
			viewHolder.ivAddressSelectStatus.setBackgroundResource(R.drawable.unselect);
		}
		viewHolder.layoutAddress.setOnClickListener(new OnClickListenerImpl(position));
		viewHolder.tvAddressModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO remember to finish it
//				context.startActivity(new Intent(context, ShoppingCartActivity.class));
			}
		});
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
			listener.getSelectId(defaultId);
		}
	}

	private final class ViewHolder {
		private TextView tvAddressTitle;
		private TextView tvAddressModify;
		private TextView tvConsignee;
		private TextView tvContactWay;
		private TextView tvPostCode;
		private TextView tvShoppingAddress;
		private ImageView ivAddressSelectStatus;
		private LinearLayout layoutAddress;
	}
}