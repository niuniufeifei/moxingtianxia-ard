package com.dgzd.mxtx.activity.mainView.homePage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.adapter.CommodityAdapter;
import com.dgzd.mxtx.entirety.ImageFourTextInfo;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.utils.UIHelper;
import com.dgzd.mxtx.widget.XScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0 <配件>
 * @FileName: MotoAccessoriesActivity.java
 * @author: Jessica
 * @date: 2016-01-21 16:47
 */

public class MotoAccessoriesActivity extends AppCompatActivity implements XScrollView.IXScrollViewListener {

    private ImageView ivBack;

    private ListView lvCommodity;
    private Handler mHandler;
    private CommodityAdapter adapter;
    private List<ImageFourTextInfo> commodityList = new ArrayList<ImageFourTextInfo>();
    private int mRefreshIndex = 1;
    private XScrollView mScrollView;
    private RequestQueue requestQueue;
    private final String requestTag = "MotoAccessoriesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moto_accessories);
        requestQueue = Volley.newRequestQueue(this);
        initView();
        backClick();
    }

    private void initView() {
        commodityList.clear();
        mRefreshIndex = 1;

        ivBack = (ImageView) findViewById(R.id.iv_back);
        mHandler = new Handler();

        mScrollView = (XScrollView) findViewById(R.id.scroll_view);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(this);
        mScrollView.setRefreshTime(UIHelper.getTime());

        View content = LayoutInflater.from(this).inflate(R.layout.vw_scroll_view_content, null);
        if (null != content) {
            lvCommodity = (ListView) content.findViewById(R.id.content_list);
            lvCommodity.setFocusable(false);
            lvCommodity.setFocusableInTouchMode(false);
            getCommodityData(GlobalEntity.PULL_UP);
        }

        mScrollView.setView(content);
    }

    private void getCommodityData(final int status) {

        String commodityUrl = String.format(GlobalEntity.commodityListInfo2Url, mRefreshIndex, "", 11);
        StringRequest listRequest = new StringRequest(commodityUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataJson = new JSONObject(response);
                    String strData = dataJson.getString("result");
                    JSONArray data = new JSONArray(strData);
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); ++i) {
                            ImageFourTextInfo info = new ImageFourTextInfo();
                            info.imgUrl = data.getJSONObject(i).getString("ThumbImage").toString();
                            info.strCommodityName = data.getJSONObject(i).getString("ProductName").toString();
                            info.purchaserNum = data.getJSONObject(i).getInt("BuyNumber");
                            info.strPrice = data.getJSONObject(i).getString("ProductPrice");
                            info.strShopName = data.getJSONObject(i).getString("StoreName");
                            commodityList.add(info);
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
                    adapter = new CommodityAdapter(commodityList, getApplicationContext());
                    lvCommodity.setAdapter(adapter);
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
                }

                default:
                    break;
            }
        }
    };

    private void onLoad() {
        mScrollView.stopRefresh();
        mScrollView.stopLoadMore();
        mScrollView.setRefreshTime(UIHelper.getTime());
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshIndex = 1;
                commodityList.clear();
                getCommodityData(GlobalEntity.PULL_UP);
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ++mRefreshIndex;
                getCommodityData(GlobalEntity.PULL_DOWN);
            }
        }, 2500);
    }

    private int measureHeight() {
        // get ListView adapter
        ListAdapter adapter = lvCommodity.getAdapter();
        if (null == adapter) {
            return 0;
        }

        int totalHeight = 0;

        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, lvCommodity);
            if (null == item) continue;
            // measure each item width and height
            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvCommodity.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (lvCommodity.getDividerHeight() * (adapter.getCount() - 1));

        lvCommodity.setLayoutParams(params);

        return params.height;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
