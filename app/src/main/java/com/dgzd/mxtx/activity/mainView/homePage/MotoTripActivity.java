package com.dgzd.mxtx.activity.mainView.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.mineView.userEvent.EventRegistrationActivity;
import com.dgzd.mxtx.adapter.CommonAdapter;
import com.dgzd.mxtx.entirety.MotoTripInfo;
import com.dgzd.mxtx.entirety.ViewHolder;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.utils.UIHelper;
import com.dgzd.mxtx.widget.XScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0 <功能>
 * @FileName: MotoTripActivity.java
 * @author: Jessica
 * @date: 2016-01-21 16:47
 */

public class MotoTripActivity extends AppCompatActivity implements XScrollView.IXScrollViewListener {
    private ImageView ivBack;
    private LinearLayout layoutSearch, layoutSearchEdit;
    private EditText etSearch;
    private ListView lvMotoTrip;
    private Button btnSearch;
    private List<MotoTripInfo> motoTripList = new ArrayList<>();

    private String strSearch = "";
    private CommonAdapter adapter;
    private Handler mHandler;
    private int mRefreshIndex = 1;
    private XScrollView mScrollView;
    private RequestQueue requestQueue;
    private String requestTag = "MotoTripActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moto_trip);
        requestQueue = Volley.newRequestQueue(this);
        initView();
        backClick();
        eventItemClick();
        searchClick();
        searchEditClick();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        layoutSearch = (LinearLayout) findViewById(R.id.layout_search);
        layoutSearchEdit = (LinearLayout) findViewById(R.id.layout_search2);
        etSearch = (EditText)findViewById(R.id.et_search);
        btnSearch = (Button)findViewById(R.id.btn_search);

        mHandler = new Handler();
        mScrollView = (XScrollView) findViewById(R.id.scroll_view);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(MotoTripActivity.this);
        mScrollView.setRefreshTime(UIHelper.getTime());

        View content = LayoutInflater.from(this).inflate(R.layout.vw_scroll_view_content, null);
        if (null != content) {
            lvMotoTrip = (ListView) content.findViewById(R.id.content_list);
            lvMotoTrip.setFocusable(false);
            lvMotoTrip.setFocusableInTouchMode(false);
            getMotoTripData(GlobalEntity.PULL_UP, "");
        }

        mScrollView.setView(content);
    }

    private void searchClick() {
        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSearchShow(true);
            }
        });
    }

    private void changeSearchShow(boolean isEdit) {
        if (isEdit) {
            layoutSearch.setVisibility(View.GONE);
            layoutSearchEdit.setVisibility(View.VISIBLE);
            etSearch.findFocus();
        } else {
            layoutSearch.setVisibility(View.VISIBLE);
            layoutSearchEdit.setVisibility(View.GONE);
        }
    }

    private void searchEditClick() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strSearch = etSearch.getText().toString().trim();
                if (strSearch != null) {
                    if (!"".equals(strSearch)) {
                        mRefreshIndex = 1;
                        motoTripList.clear();
                        getMotoTripData(GlobalEntity.PULL_UP, strSearch);
                    }
                }
            }
        });
    }


    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getMotoTripData(final int status, String searchKey) {

        try {
            searchKey = URLEncoder.encode(searchKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String commodityUrl = String.format(GlobalEntity.ActivityListInfoUrl, searchKey, mRefreshIndex);
        StringRequest listRequest = new StringRequest(commodityUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject dataJson = new JSONObject(response);
                    String strResult = dataJson.getString("result");
                    JSONObject activityListObject = new JSONObject(strResult);
                    String strData = activityListObject.getString("ActivityList");
                    JSONArray data = new JSONArray(strData);
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); ++i) {
                            MotoTripInfo info = new MotoTripInfo();
                            JSONObject imageEntityObject = data.getJSONObject(i).getJSONObject("imageEntity");
                            info.setImgUrl(imageEntityObject.getString("ImageName"));
                            info.setTitle(data.getJSONObject(i).getString("Title").toString());
                            info.setUploadTime(data.getJSONObject(i).getString("addtime").toString());
                            info.setContactPerson(data.getJSONObject(i).getString("ContactPerson"));
                            info.setContactMobile(data.getJSONObject(i).getString("ContactMobile"));
                            info.setSummary(data.getJSONObject(i).getString("Summary"));
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
        lvMotoTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    adapter = new CommonAdapter<MotoTripInfo>(MotoTripActivity.this, motoTripList, R.layout.moto_trip_item) {
                        @Override
                        public void convert(ViewHolder helper, MotoTripInfo item, int position) {
                            helper.setSrcImageByUrl(R.id.iv_icon, item.getImgUrl());
                            helper.setText(R.id.tv_title, item.getTitle());
                            helper.setText(R.id.tv_time, item.getUploadTime());
                        }
                    };
                    lvMotoTrip.setAdapter(adapter);

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

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshIndex = 1;
                motoTripList.clear();
                getMotoTripData(GlobalEntity.PULL_UP, strSearch);
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ++mRefreshIndex;
                getMotoTripData(GlobalEntity.PULL_DOWN, strSearch);
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
        ListAdapter adapter = lvMotoTrip.getAdapter();
        if (null == adapter) {
            return 0;
        }

        int totalHeight = 0;

        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, lvMotoTrip);
            if (null == item) continue;
            // measure each item width and height
            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvMotoTrip.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (lvMotoTrip.getDividerHeight() * (adapter.getCount() - 1));

        lvMotoTrip.setLayoutParams(params);

        return params.height;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
