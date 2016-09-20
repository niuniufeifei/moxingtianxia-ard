package com.dgzd.mxtx.activity.mineView.userEvent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.adapter.CommonAdapter;
import com.dgzd.mxtx.entirety.MotoTripInfo;
import com.dgzd.mxtx.entirety.ViewHolder;
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
 * @version V1.0 <我的活动>
 * @FileName: EventActivity.java
 * @author: Jessica
 * @date: 2015-12-28 14:43
 */

public class EventActivity extends AppCompatActivity implements XScrollView.IXScrollViewListener {
    private ImageView ivBack;
    private ListView lvEvent;
    private List<MotoTripInfo> motoTripList = new ArrayList<>();

    private String strSearch = "";
    private CommonAdapter adapter;
    private Handler mHandler;
    private int mRefreshIndex = 1;
    private XScrollView mScrollView;
    private RequestQueue requestQueue;
    private final String requestTag = "EventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        requestQueue = Volley.newRequestQueue(this);
        initView();
        backClick();
        eventItemClick();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mHandler = new Handler();

        mScrollView = (XScrollView) findViewById(R.id.scroll_view);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(EventActivity.this);
        mScrollView.setRefreshTime(UIHelper.getTime());

        View content = LayoutInflater.from(this).inflate(R.layout.vw_scroll_view_content, null);
        if (null != content) {
            lvEvent = (ListView) content.findViewById(R.id.content_list);
            lvEvent.setFocusable(false);
            lvEvent.setFocusableInTouchMode(false);
            getMotoTripData(GlobalEntity.PULL_UP);
        }

        mScrollView.setView(content);
    }

    private void getMotoTripData(final int status) {
        int uid = MxtxApplictaion.getInstance().personalInfo.getUid();
        String commodityUrl = String.format(GlobalEntity.GetMyActivityListInfoUrl, uid, mRefreshIndex);
        StringRequest listRequest = new StringRequest(commodityUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject dataJson = new JSONObject(response);
                    String strResult = dataJson.getString("result");
                    JSONArray data = new JSONArray(strResult);
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); ++i) {
                            MotoTripInfo info = new MotoTripInfo();
                            info.setImgUrl(data.getJSONObject(i).getString("ImageName"));
                            info.setTitle(data.getJSONObject(i).getString("Title").toString());
                            info.setUploadTime(data.getJSONObject(i).getString("AddTime").toString());
//                            info.setContactPerson(data.getJSONObject(i).getString("ContactPerson"));
//                            info.setContactMobile(data.getJSONObject(i).getString("ContactMobile"));
//                            info.setSummary(data.getJSONObject(i).getString("Summary"));
                            info.setActivityId(data.getJSONObject(i).getInt("ActivityId"));
                            info.setMoney(data.getJSONObject(i).getInt("Money"));
                            motoTripList.add(info);
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

    private void eventItemClick() {
        lvEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contactPerson = motoTripList.get(position).getContactPerson();
                String contactMobile = motoTripList.get(position).getContactMobile();
                float fee = motoTripList.get(position).getMoney();
                int activityId = motoTripList.get(position).getActivityId();
                Intent intent = new Intent();
                intent.putExtra(GlobalEntity.MOTO_TRIP_ACTIVITY_ID, activityId);
                intent.setClass(getApplicationContext(), EventRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    adapter = new CommonAdapter<MotoTripInfo>(EventActivity.this, motoTripList, R.layout.moto_trip_item) {
                        @Override
                        public void convert(ViewHolder helper, MotoTripInfo item, int position) {
                            helper.setSrcImageByUrl(R.id.iv_icon, item.getImgUrl());
                            helper.setText(R.id.tv_title, item.getTitle());
                            helper.setText(R.id.tv_time, item.getUploadTime());
                        }
                    };
                    lvEvent.setAdapter(adapter);

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
                motoTripList.clear();
                getMotoTripData(GlobalEntity.PULL_UP);
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ++mRefreshIndex;
                getMotoTripData(GlobalEntity.PULL_DOWN);
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
        ListAdapter adapter = lvEvent.getAdapter();
        if (null == adapter) {
            return 0;
        }

        int totalHeight = 0;

        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, lvEvent);
            if (null == item) continue;
            // measure each item width and height
            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvEvent.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (lvEvent.getDividerHeight() * (adapter.getCount() - 1));

        lvEvent.setLayoutParams(params);

        return params.height;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
