package com.dgzd.mxtx.fragment.mainView.bottomTab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.login.LoginActivity;
import com.dgzd.mxtx.activity.mainView.homePage.MotoAccessoriesActivity;
import com.dgzd.mxtx.activity.mainView.homePage.MotoGearActivity;
import com.dgzd.mxtx.activity.mainView.homePage.MotoTripActivity;
import com.dgzd.mxtx.activity.mainView.homePage.MotoTypeActivity;
import com.dgzd.mxtx.activity.mainView.search.CommodityShowActivity;
import com.dgzd.mxtx.custom.HotView;
import com.dgzd.mxtx.custom.NoScrollGridView;
import com.dgzd.mxtx.custom.SlideShowView;
import com.dgzd.mxtx.entirety.HotViewInfo;
import com.dgzd.mxtx.entirety.ImageTextInfo;
import com.dgzd.mxtx.rong.activity.RongHomeActivity;
import com.dgzd.mxtx.tools.AddressSelectDialog;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomePageFragment extends Fragment {
    private View view;
    private RequestQueue requestQueue;
    private String requestTag = "";
    private LinearLayout layoutSearch;
    private GridView gvNavigationShow;
    private ImageView mChat, ivUploadVideo;
    private List<Map<String, Object>> motoData;
    private HotView hvEvents, hvCommodity, hvVideo, hvNewProducts;
    private HotViewInfo showEventList, showCommodityList, showVideoList, showNewProductsList;

    public HomePageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        initView();
        searchClick();
        initNavigationShowView();
        navigationShowViewItemClick();
        initSlide();
        initHotView();
        uploadVideoClick();
        return view;
    }

    private void initView() {
        layoutSearch = (LinearLayout) view.findViewById(R.id.layout_search);
        mChat = (ImageView) view.findViewById(R.id.chat);
        ivUploadVideo = (ImageView) view.findViewById(R.id.iv_upload_video);
        gvNavigationShow = (NoScrollGridView) view.findViewById(R.id.gv_show);
        hvEvents = (HotView) view.findViewById(R.id.hv_events);
        hvCommodity = (HotView) view.findViewById(R.id.hv_commodity);
        hvVideo = (HotView) view.findViewById(R.id.hv_video);
        hvNewProducts = (HotView) view.findViewById(R.id.hv_recent_updates);
        motoData = new ArrayList<>();
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSigned = MxtxApplictaion.getInstance().isSignedIn();
                if (!isSigned) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), RongHomeActivity.class));
                }

            }
        });
    }

    private void uploadVideoClick() {
        ivUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressSelectDialog dialog = new AddressSelectDialog(getActivity(), "请选择点地址", new AddressSelectDialog.OnAddressSelectDialogListener() {
                    @Override
                    public void back(String name) {
                        UIHelper.ToastMessage(getActivity(), name);
                    }
                });
                dialog.show();
            }
        });
    }

    private void searchClick() {
        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIHelper.isFastClick())
                    return;
                startActivity(new Intent(getActivity(), CommodityShowActivity.class));
            }
        });
    }

    private void initNavigationShowView() {
        int[][] showViewInfo = {{R.drawable.moto_type, R.string.moto_type},
                {R.drawable.moto_accessories, R.string.moto_accessories}, {R.drawable.moto_gear, R.string.moto_gear},
                {R.drawable.moto_video, R.string.moto_video}, {R.drawable.moto_trip, R.string.moto_trip}};
        for (int i = 0; i < showViewInfo.length; ++i) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", showViewInfo[i][0]);
            map.put("text", getString(showViewInfo[i][1]).toString());
            motoData.add(map);
        }
        String[] from = {"image", "text"};
        int[] to = {R.id.iv_show, R.id.tv_show};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), motoData, R.layout.show_view_item, from, to);
        gvNavigationShow.setAdapter(simpleAdapter);
    }

    private void navigationShowViewItemClick() {
        gvNavigationShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(getActivity(), MotoTypeActivity.class));
                } else if (position == 1) {
                    startActivity(new Intent(getActivity(), MotoAccessoriesActivity.class));
                } else if (position == 2) {
                    startActivity(new Intent(getActivity(), MotoGearActivity.class));
                } else if (position == 3) {
                    Intent intent = new Intent();
                    intent.putExtra("isCommodityShow", false);
                    intent.setClass(getActivity(), CommodityShowActivity.class);
                    startActivity(intent);
                } else if (position == 4) {
                    startActivity(new Intent(getActivity(), MotoTripActivity.class));
                }
            }
        });
    }

    private void initSlide() {
        SlideShowView slideView = (SlideShowView) view.findViewById(R.id.slideshowView);
        slideView.setUrl(GlobalEntity.HomePageRequestUrl);
    }

    private void initHotView() {
        getEventData();
        getHotProducts();
        getHotVideos();
        getRecentUpdates();
        hvEvents.setOnMoreClick(new HotView.MoreClickListener() {
            @Override
            public void onClickButton() {
                startActivity(new Intent(getActivity(), MotoTripActivity.class));
            }
        });

        hvCommodity.setOnMoreClick(new HotView.MoreClickListener() {
            @Override
            public void onClickButton() {
                startActivity(new Intent(getActivity(), CommodityShowActivity.class));
            }
        });

        hvVideo.setOnMoreClick(new HotView.MoreClickListener() {
            @Override
            public void onClickButton() {
                Intent intent = new Intent();
                intent.putExtra("isCommodityShow", false);
                intent.setClass(getActivity(), CommodityShowActivity.class);
                startActivity(intent);
            }
        });

        hvNewProducts.setOnMoreClick(new HotView.MoreClickListener() {
            @Override
            public void onClickButton() {
                Toast.makeText(getActivity(), "hvNewProducts more is clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    hvEvents.setGridImageList(showEventList);
                    break;
                }
                case 2: {
                    hvCommodity.setGridImageList(showCommodityList);
                    break;
                }
                case 3: {
                    hvVideo.setGridImageList(showVideoList);
                    break;
                }
                case 4: {
                    hvNewProducts.setGridImageList(showNewProductsList);
                }
                default:
                    break;
            }
        }
    };

    private void getEventData() {
        requestTag = "HomePageEvent";
        StringRequest listRequest = new StringRequest(GlobalEntity.HomePageRequestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    showEventList = new HotViewInfo();
                    JSONObject dataJson = new JSONObject(response);
                    String strData = dataJson.getString("result");
                    JSONObject hotProductJson = new JSONObject(strData);
                    JSONArray data = hotProductJson.getJSONArray("HotActivities");
                    List<ImageTextInfo> infoList = new ArrayList<>();
                    for (int i = 0; i < data.length(); ++i) {
                        ImageTextInfo info = new ImageTextInfo();
                        info.imgUrl = data.getJSONObject(i).getString("ThumbImage").toString();
                        info.strText = data.getJSONObject(i).getString("ActivityName").toString();
                        boolean isNull = data.getJSONObject(i).isNull("ProductPrice");
                        if (!isNull) {
                            info.strPrice = data.getJSONObject(i).getString("ProductPrice");
                            info.isShowPrice = true;
                        } else {
                            info.strPrice = "";
                            info.isShowPrice = false;
                        }
                        infoList.add(info);
                    }
                    showEventList.setInfoList(infoList);
                    showEventList.setImgResourceId(R.drawable.event_name);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    private void getHotProducts() {
        requestTag = "HomePageHotProducts";
        StringRequest listRequest = new StringRequest(GlobalEntity.HomePageRequestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    showCommodityList = new HotViewInfo();
                    JSONObject dataJson = new JSONObject(response);
                    String strData = dataJson.getString("result");
                    JSONObject hotProductJson = new JSONObject(strData);
                    JSONArray data = hotProductJson.getJSONArray("HotProducts");
                    List<ImageTextInfo> infoList = new ArrayList<>();
                    for (int i = 0; i < data.length(); ++i) {
                        ImageTextInfo info = new ImageTextInfo();
                        info.imgUrl = data.getJSONObject(i).getString("ThumbImage").toString();
                        info.strText = data.getJSONObject(i).getString("ProductName").toString();
                        boolean isNull = data.getJSONObject(i).isNull("ProductPrice");
                        if (!isNull) {
                            info.strPrice = data.getJSONObject(i).getString("ProductPrice");
                            info.isShowPrice = true;
                        } else {
                            info.strPrice = "";
                            info.isShowPrice = false;
                        }
                        infoList.add(info);
                    }
                    showCommodityList.setInfoList(infoList);
                    showCommodityList.setImgResourceId(R.drawable.commodity_name);
                    Message msg = new Message();
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    private void getHotVideos() {
        requestTag = "HomePageHotVideos";
        StringRequest listRequest = new StringRequest(GlobalEntity.HomePageRequestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    showVideoList = new HotViewInfo();
                    JSONObject dataJson = new JSONObject(response);
                    String strData = dataJson.getString("result");
                    JSONObject hotVideoJson = new JSONObject(strData);
                    JSONArray data = hotVideoJson.getJSONArray("HotVideos");
                    List<ImageTextInfo> infoList = new ArrayList<>();
                    for (int i = 0; i < data.length(); ++i) {
                        ImageTextInfo info = new ImageTextInfo();
                        info.imgUrl = data.getJSONObject(i).getString("ThumbImage").toString();
                        info.strText = data.getJSONObject(i).getString("VideoName").toString();
                        boolean isNull = data.getJSONObject(i).isNull("ProductPrice");
                        if (!isNull) {
                            info.strPrice = data.getJSONObject(i).getString("ProductPrice");
                            info.isShowPrice = true;
                        } else {
                            info.strPrice = "";
                            info.isShowPrice = false;
                        }
                        infoList.add(info);
                    }
                    showVideoList.setInfoList(infoList);
                    showVideoList.setImgResourceId(R.drawable.video_name);
                    Message msg = new Message();
                    msg.what = 3;
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    private void getRecentUpdates() {
        requestTag = "HomePageRecentUpdates";
        StringRequest listRequest = new StringRequest(GlobalEntity.HomePageRequestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    showNewProductsList = new HotViewInfo();
                    JSONObject dataJson = new JSONObject(response);
                    String strData = dataJson.getString("result");
                    JSONObject hotVideoJson = new JSONObject(strData);
                    JSONArray data = hotVideoJson.getJSONArray("NewProducts");
                    List<ImageTextInfo> infoList = new ArrayList<>();
                    for (int i = 0; i < data.length(); ++i) {
                        ImageTextInfo info = new ImageTextInfo();
                        info.imgUrl = data.getJSONObject(i).getString("ThumbImage").toString();
                        info.strText = data.getJSONObject(i).getString("ProductName").toString();
                        boolean isNull = data.getJSONObject(i).isNull("ProductPrice");
                        if (!isNull) {
                            info.strPrice = data.getJSONObject(i).getString("ProductPrice");
                            info.isShowPrice = true;
                        } else {
                            info.strPrice = "";
                            info.isShowPrice = false;
                        }
                        infoList.add(info);
                    }
                    showNewProductsList.setInfoList(infoList);
                    showNewProductsList.setImgResourceId(R.drawable.commodity_name);
                    Message msg = new Message();
                    msg.what = 4;
                    mHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}

