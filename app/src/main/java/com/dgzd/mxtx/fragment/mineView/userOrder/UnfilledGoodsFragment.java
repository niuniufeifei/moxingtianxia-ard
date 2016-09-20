package com.dgzd.mxtx.fragment.mineView.userOrder;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.adapter.UserOrderUnfilledGoodsAdapter;
import com.dgzd.mxtx.entirety.UserOrderInfo;
import com.dgzd.mxtx.entirety.UserOrderListInfo;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.utils.UIHelper;
import com.dgzd.mxtx.widget.XScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0 <待发货>
 * @FileName: UnfilledGoodsFragment.java
 * @author: Jessica
 */

public class UnfilledGoodsFragment extends Fragment implements XScrollView.IXScrollViewListener {
    private View view;
    private Handler mHandler;
    private ListView lvMyOrder;
    private List<UserOrderInfo> orderList = new ArrayList<>();
    private UserOrderUnfilledGoodsAdapter adapter;
    private int mRefreshIndex = 1;
    private XScrollView mScrollView;
    private RequestQueue requestQueue;
    private final String requestTag = "UnfilledGoodsFragment";
    private MxtxApplictaion myApp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_order_unfilled_goods, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        myApp = MxtxApplictaion.getInstance();
        initView();
        return view;
    }

    private void initView() {
        orderList.clear();
        mRefreshIndex = 1;
        mHandler = new Handler();

        mScrollView = (XScrollView) view.findViewById(R.id.scroll_view);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(this);
        mScrollView.setRefreshTime(UIHelper.getTime());

        View content = LayoutInflater.from(getActivity()).inflate(R.layout.vw_scroll_view_content, null);
        if (null != content) {
            lvMyOrder = (ListView) content.findViewById(R.id.content_list);
            lvMyOrder.setDividerHeight(1);
            lvMyOrder.setFocusable(false);
            lvMyOrder.setFocusableInTouchMode(false);
            getOrderData(GlobalEntity.PULL_UP);
        }

        mScrollView.setView(content);
    }

    private void getOrderData(final int status) {
        int userId = MxtxApplictaion.getInstance().personalInfo.getUid();
        String commodityUrl = String.format(GlobalEntity.UserGetMyOrderUrl, userId, mRefreshIndex, GlobalEntity.GET_USER_ORDER_UNPAID_INDEX);
        StringRequest listRequest = new StringRequest(commodityUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataJson = new JSONObject(response);
                    String strData = dataJson.getString("result");
                    JSONObject orderListJson = new JSONObject(strData);
                    String orderListData = orderListJson.getString("OrderList");
                    JSONArray data = new JSONArray(orderListData);
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); ++i) {
                            UserOrderInfo info = new UserOrderInfo();
                            info.setShopName(data.getJSONObject(i).getString("storename").trim());
                            info.setOrderNumber(data.getJSONObject(i).getInt("osn"));
                            info.setOid(data.getJSONObject(i).getInt("oid"));
                            info.setPayMoney(data.getJSONObject(i).getInt("orderamount"));
                            info.setStoreId(data.getJSONObject(i).getInt("storeid"));

                            JSONArray productListData = data.getJSONObject(i).getJSONArray("OrderProductList");
                            if (productListData.length() > 0) {
                                List<UserOrderListInfo> orderListInfo = new ArrayList<>();
                                for (int j = 0; j < productListData.length(); ++j) {
                                    UserOrderListInfo listInfo = new UserOrderListInfo();
                                    listInfo.setImgUrl(productListData.getJSONObject(j).getString("ShowImg").toString());
                                    listInfo.setCommodityName(productListData.getJSONObject(j).getString("Name").toString());
                                    listInfo.setPurchaserNum(productListData.getJSONObject(j).getInt("BuyCount"));
                                    listInfo.setStrPrice(productListData.getJSONObject(j).getString("ShopPrice"));
                                    orderListInfo.add(listInfo);
                                }
                                info.setOrderListArray(orderListInfo);
                            }
                            orderList.add(info);
                        }

                        Message msg = new Message();
                        msg.what = status;
                        myHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 3;
                        myHandler.sendMessage(msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    myHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Message msg = new Message();
                msg.what = 3;
                myHandler.sendMessage(msg);
            }
        });
        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    adapter = new UserOrderUnfilledGoodsAdapter(orderList, getActivity());
                    lvMyOrder.setAdapter(adapter);
                    measureHeight();
                    onLoad();
                    break;
                }
                case 2: {
                    adapter.notifyDataSetChanged();
                    measureHeight();
                    onLoad();
                    break;
                }
                case 3: {
                    measureHeight();
                    onLoad();
                    break;
                }
                case 4: {
                    UIHelper.ToastMessage(getActivity(), "取消订单成功！");
                    break;
                }
                case 5: {
                    UIHelper.ToastMessage(getActivity(), "取消订单失败！");
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshIndex = 1;
                orderList.clear();
                getOrderData(GlobalEntity.PULL_UP);
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ++mRefreshIndex;
                getOrderData(GlobalEntity.PULL_DOWN);
            }
        }, 2500);
    }

    private void onLoad() {
        mScrollView.stopRefresh();
        mScrollView.stopLoadMore();
        mScrollView.setRefreshTime(UIHelper.getTime());
    }

    private int measureHeight() {
        // get ListView adapter
        ListAdapter adapter = lvMyOrder.getAdapter();
        if (null == adapter) {
            return 0;
        }

        int totalHeight = 0;

        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, lvMyOrder);
            if (null == item) continue;
            // measure each item width and height
            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvMyOrder.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (lvMyOrder.getDividerHeight() * (adapter.getCount() - 1));

        lvMyOrder.setLayoutParams(params);

        return params.height;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
