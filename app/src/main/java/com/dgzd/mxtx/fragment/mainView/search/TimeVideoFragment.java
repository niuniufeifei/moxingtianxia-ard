package com.dgzd.mxtx.fragment.mainView.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.view.CommodityDetailActivity;
import com.dgzd.mxtx.adapter.CommonAdapter;
import com.dgzd.mxtx.entirety.MyVideoInfo;
import com.dgzd.mxtx.entirety.ViewHolder;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.utils.UIHelper;
import com.dgzd.mxtx.widget.XScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimeVideoFragment extends Fragment implements XScrollView.IXScrollViewListener {

    private View view;
    private ListView lvVideo;
    private Handler mHandler;
    private CommonAdapter adapter;
    private List<MyVideoInfo> videoList = new ArrayList<MyVideoInfo>();
    private String strSearch = "";
    private String strSearchTemp = "";

    private int mRefreshIndex = 1;
    private XScrollView mScrollView;
    private RequestQueue requestQueue;
    private final String requestTag = "TimeVideoFragment";

    public TimeVideoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoList.clear();
        mRefreshIndex = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_entire, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        getBundleContent("clear");
        initView();
        videoClick();
        return view;
    }

    private void getBundleContent(String key) {
        Bundle bundle = getArguments();
        boolean isClear = bundle.getBoolean(key);
        if (isClear) {
            videoList.clear();
            mRefreshIndex = 1;
        }
    }

    private void initView() {
        mHandler = new Handler();

        mScrollView = (XScrollView) view.findViewById(R.id.scroll_view);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(this);
        mScrollView.setRefreshTime(UIHelper.getTime());

        View content = LayoutInflater.from(getActivity()).inflate(R.layout.vw_scroll_view_content, null);
        if (null != content) {
            lvVideo = (ListView) content.findViewById(R.id.content_list);
            lvVideo.setFocusable(false);
            lvVideo.setFocusableInTouchMode(false);
            getVideoData(GlobalEntity.PULL_UP, "");
        }

        mScrollView.setView(content);
    }

    private void videoClick() {
        lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CommodityDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    public void searchVideoData(String strSearch) {
        strSearchTemp = this.strSearch;
        this.strSearch = strSearch;
        mRefreshIndex = 1;
        videoList.clear();
        getVideoData(GlobalEntity.PULL_UP, strSearch);
    }

    public void resetSearchKeyString() {
        strSearch = strSearchTemp;
        mRefreshIndex = 1;
        videoList.clear();
        getVideoData(GlobalEntity.PULL_UP, strSearch);
    }

    private void getVideoData(final int status, String searchKey) {
        try {
            searchKey = URLEncoder.encode(searchKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String commodityUrl = String.format(GlobalEntity.VideoListInfoUrl, searchKey, 1, mRefreshIndex);
        StringRequest listRequest = new StringRequest(commodityUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataJson = new JSONObject(response);
                    JSONObject listJson = dataJson.getJSONObject("result");
                    String strData = listJson.getString("VideoList");
                    JSONArray data = new JSONArray(strData);
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); ++i) {
                            MyVideoInfo info = new MyVideoInfo();
                            info.setImgUrl(data.getJSONObject(i).getString("Thumbnail").toString());
                            info.setTitle(data.getJSONObject(i).getString("description").toString());
                            info.setTitle2(data.getJSONObject(i).getString("NickName"));
                            info.setDate(data.getJSONObject(i).getString("addtime"));
                            videoList.add(info);
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
                    adapter = new CommonAdapter<MyVideoInfo>(getActivity(), videoList, R.layout.my_video_item) {
                        @Override
                        public void convert(ViewHolder helper, MyVideoInfo item, int position) {
                            helper.setImageByUrl(R.id.iv_icon, item.getImageUrl());
                            helper.setText(R.id.tv_title, item.getTitle());
                            helper.setText(R.id.tv_title2, item.getTitle2());
                            helper.setText(R.id.tv_date, item.getDate());
                        }
                    };
                    lvVideo.setAdapter(adapter);
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
                videoList.clear();
                getVideoData(GlobalEntity.PULL_UP, strSearch);
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ++mRefreshIndex;
                getVideoData(GlobalEntity.PULL_DOWN, strSearch);
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
        ListAdapter adapter = lvVideo.getAdapter();
        if (null == adapter) {
            return 0;
        }

        int totalHeight = 0;

        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, lvVideo);
            if (null == item) continue;
            // measure each item width and height
            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvVideo.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (lvVideo.getDividerHeight() * (adapter.getCount() - 1));

        lvVideo.setLayoutParams(params);

        return params.height;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
