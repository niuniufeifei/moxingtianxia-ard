package com.dgzd.mxtx.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.custom.TxtTxtBtn;
import com.dgzd.mxtx.entirety.UserOrderInfo;
import com.dgzd.mxtx.entirety.UserOrderListInfo;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.tools.NormalPostRequest;
import com.dgzd.mxtx.utils.UIHelper;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
 * 待发货
 ****/


public class UserOrderUnfilledGoodsAdapter extends BaseAdapter {
    private List<UserOrderInfo> commodityViewArray = Collections.emptyList();
    private UserOrderItemAdapter adapter;
    private Context context = null;
    private LayoutInflater mInflater;
    private RequestQueue requestQueue;
    private final String requestTag = "UserOrderAdapter";

    public UserOrderUnfilledGoodsAdapter(List<UserOrderInfo> commodityViewArray, Context context) {
        super();
        this.context = context;
        requestQueue = Volley.newRequestQueue(this.context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.custom_user_order_unfiiled_goods_item, null);
            holder = new ViewHolder();
            holder.lvUserOrder = (ListView) convertView.findViewById(R.id.lv_user_order);
            holder.tvShoppingName = (TextView) convertView.findViewById(R.id.tv_shopping_name);
            holder.txttxtOrderNumber = (TxtTxtBtn) convertView.findViewById(R.id.txttxtbtn_order_number);
            holder.tvCancelOrder = (TextView) convertView.findViewById(R.id.tv_cancel_order);
            holder.tvRefund = (TextView) convertView.findViewById(R.id.tv_refund_application);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<UserOrderListInfo> orderList = commodityViewArray.get(position).getOrderListArray();
        if (orderList != null) {
            adapter = new UserOrderItemAdapter(orderList, context);
            holder.lvUserOrder.setAdapter(adapter);
        }

        String strOrderNumber = String.valueOf(commodityViewArray.get(position).getOrderNumber());
        holder.txttxtOrderNumber.setContentText(strOrderNumber);
        holder.tvShoppingName.setText(commodityViewArray.get(position).getShopName());
        holder.ivIcon.setBackgroundResource(R.drawable.red_chat);
        holder.tvCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCancleOrder(commodityViewArray.get(position).getOid(), commodityViewArray.get(position).getStoreId());
            }
        });

        holder.tvRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }


    private void postCancleOrder(int oid, int storeId) {
        Map<String, String> map = new HashMap<String, String>();
        int userId = MxtxApplictaion.getInstance().personalInfo.getUid();
        String strUserId = String.format("%d", userId);
        String strOid = String.format("%d", oid);
        String strStoreId = String.format("%d", storeId);
        map.put("oid", strOid);
        map.put("Userid", strUserId);
        map.put("Storeid", strStoreId);

        Request<JSONObject> listRequest = new NormalPostRequest(GlobalEntity.UserCancleOrderUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Message msg = new Message();
                msg.what = 1;
                myHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 2;
                myHandler.sendMessage(msg);
            }
        }, map);

        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    private void postSubmitOrder(int oid, int storeId) {
        Map<String, String> map = new HashMap<String, String>();

        int userId = MxtxApplictaion.getInstance().personalInfo.getUid();
        String strUserId = String.format("%d", userId);
        String strOid = String.format("%d", oid);
        map.put("oid", strOid);
        map.put("Userid", strUserId);


        Request<JSONObject> listRequest = new NormalPostRequest(GlobalEntity.UsersubmitOrderUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Message msg = new Message();
                msg.what = 3;
                myHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 4;
                myHandler.sendMessage(msg);
            }
        }, map);

        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    UIHelper.ToastMessage(context, "取消订单成功！");
                    break;
                }
                case 2: {
                    UIHelper.ToastMessage(context, "取消订单失败！");
                    break;
                }
                case 3: {
                    UIHelper.ToastMessage(context, "支付订单成功！");
                    break;
                }
                case 4: {
                    UIHelper.ToastMessage(context, "支付订单失败！");
                    break;
                }
                default:
                    break;
            }
        }
    };

    private final class ViewHolder {
        private ListView lvUserOrder;
        private ImageView ivIcon;
        private TextView tvShoppingName;
        private TxtTxtBtn txttxtOrderNumber;
        private TextView tvCancelOrder;
        private TextView tvRefund;
    }
}
