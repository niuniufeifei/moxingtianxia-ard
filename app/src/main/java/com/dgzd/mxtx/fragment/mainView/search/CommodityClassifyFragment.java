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
import com.dgzd.mxtx.adapter.CommodityAdapter;
import com.dgzd.mxtx.adapter.CommonAdapter;
import com.dgzd.mxtx.custom.NoScrollGridView;
import com.dgzd.mxtx.entirety.ImageFourTextInfo;
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
public class CommodityClassifyFragment extends Fragment implements XScrollView.IXScrollViewListener {
    private View view;
    private NoScrollGridView gvClassify;
    private int gvContentId[] = {R.string.highway, R.string.cross_country, R.string.pull,
            R.string.cruise, R.string.streetcar, R.string.pedal, R.string.empty, R.string.empty};
    private int classifyIndex = 0;
    private int filterItemStartIndex = 6;

    private ListView lvCommodity;
    private Handler mHandler;
    private CommodityAdapter adapter;
    private CommonAdapter selectedAdapter;
    private List<ImageFourTextInfo> commodityList = new ArrayList<ImageFourTextInfo>();
    private String strSearch = "";
    private String strSearchTemp = "";

    private int mRefreshIndex = 1;
    private XScrollView mScrollView;
    private RequestQueue requestQueue;
    private final String requestTag = "CommodityClassifyFragment";

    private boolean siftTypeStatus = true;

    public CommodityClassifyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commodityList.clear();
        mRefreshIndex = 1;
        classifyIndex = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_classify, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        getBundleContent("clear");
        initView();
        gvClassifyData();
        commodityClick();
        classifyItemClick();
        return view;
    }

    private void getBundleContent(String key) {
        Bundle bundle = getArguments();
        boolean isClear = bundle.getBoolean(key);
        if (isClear) {
            commodityList.clear();
            mRefreshIndex = 1;
            classifyIndex = 0;
        }
    }

    private void initView() {
        gvClassify = (NoScrollGridView) view.findViewById(R.id.gv_classify);
        strSearch = getSearchString(0);
        mHandler = new Handler();

        mScrollView = (XScrollView) view.findViewById(R.id.scroll_view);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(this);
        mScrollView.setRefreshTime(UIHelper.getTime());

        View content = LayoutInflater.from(getActivity()).inflate(R.layout.vw_scroll_view_content, null);
        if (null != content) {
            lvCommodity = (ListView) content.findViewById(R.id.content_list);
            lvCommodity.setFocusable(false);
            lvCommodity.setFocusableInTouchMode(false);
            getCommodityData(GlobalEntity.PULL_UP, strSearch);
        }

        mScrollView.setView(content);
    }

    private void gvClassifyData() {
        List<String> classifyList = new ArrayList<String>();
        for (int i = 0; i < gvContentId.length; ++i) {
            String str = getResources().getString(gvContentId[i]).toString();
            classifyList.add(str);
        }

        selectedAdapter = new CommonAdapter<String>(view.getContext(), classifyList, R.layout.line_gv_item) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {

                helper.setText(R.id.tv_content, item);

                View myView = helper.getConverView().findViewById(R.id.layout_background);
                if (classifyIndex == position) {
                    myView.setBackgroundResource(R.drawable.trans_dark_salmon);
                } else {
                    myView.setBackgroundResource(R.drawable.trans_skin);
                }
            }
        };
        gvClassify.setAdapter(selectedAdapter);
    }

    private String getSearchString(int id) {
        String str = getResources().getString(gvContentId[classifyIndex]).toString();
        return str;
    }

    private void classifyItemClick() {
        gvClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= filterItemStartIndex)
                    return;

                classifyIndex = position;
                selectedAdapter.notifyDataSetChanged();//更新选择类型色块
                strSearch = getSearchString(classifyIndex);

                mRefreshIndex = 1;
                commodityList.clear();
                getCommodityData(GlobalEntity.PULL_UP, strSearch);

            }
        });
    }

    public boolean getClassifyShowFlag() {
        return siftTypeStatus;
    }

    public void setClassifyShowFlag(boolean isShow) {
        siftTypeStatus = isShow;
    }

    public void setClassifyVisibility() {
        siftTypeStatus = !siftTypeStatus;
        if (siftTypeStatus) {
            gvClassify.setVisibility(View.VISIBLE);
        } else {
            gvClassify.setVisibility(View.GONE);
        }
    }

    private void commodityClick() {
        lvCommodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CommodityDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    public void searchCommodityData(String strSearch) {
        strSearchTemp = this.strSearch;
        this.strSearch = strSearch;
        mRefreshIndex = 1;
        commodityList.clear();
        getCommodityData(GlobalEntity.PULL_UP, strSearch);
    }

    public void resetSearchKeyString() {
        strSearch = strSearchTemp;
        mRefreshIndex = 1;
        classifyIndex = 0;
        commodityList.clear();
        getCommodityData(GlobalEntity.PULL_UP, strSearch);
    }

    private void getCommodityData(final int status, String searchKey) {
        try {
            searchKey = URLEncoder.encode(searchKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String commodityUrl = String.format(GlobalEntity.commodityListInfoUrl, mRefreshIndex, searchKey);
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
                    adapter = new CommodityAdapter(commodityList, getActivity());
                    lvCommodity.setAdapter(adapter);
                    measureHeight();
                    onLoad();
                    break;
                }
                case 2: {
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                    measureHeight();
                    onLoad();
                    break;
                }
                case 3: {
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
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
                commodityList.clear();
                getCommodityData(GlobalEntity.PULL_UP, strSearch);
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ++mRefreshIndex;
                getCommodityData(GlobalEntity.PULL_DOWN, strSearch);
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
