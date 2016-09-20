package com.dgzd.mxtx.activity.mineView.userVideo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.view.VedioActivity;
import com.dgzd.mxtx.adapter.CommonAdapter;
import com.dgzd.mxtx.entirety.MyVideoInfo;
import com.dgzd.mxtx.entirety.ViewHolder;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.tools.NormalPostRequest;
import com.dgzd.mxtx.utils.UIHelper;
import com.dgzd.mxtx.widget.XScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0 <功能>
 * @FileName: UserVideoActivity.java
 * @author: Jessica
 * @date: 2015-12-23 14:24
 */

public class UserVideoActivity extends AppCompatActivity implements XScrollView.IXScrollViewListener {

    private ImageView ivBack;
    private ListView lvVideo;
    private LinearLayout layoutDelete, layoutUpload;

    private Handler mHandler;
    private int mRefreshIndex = 1;
    private XScrollView mScrollView;
    private CommonAdapter adapter;
    private List<MyVideoInfo> videoList = new ArrayList<MyVideoInfo>();
    private List<Integer> selectedVideoArray;
    private RequestQueue requestQueue;
    private final String requestTag = "UserVideoActivity";
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_video);
        requestQueue = Volley.newRequestQueue(this);
        initView();
        backClick();
        deleteClick();
        uploadClick();
        listItemClick();
    }

    private void initView() {
        userId = MxtxApplictaion.getInstance().personalInfo.getUid();
        selectedVideoArray = new ArrayList<Integer>();

        ivBack = (ImageView) findViewById(R.id.iv_back);
        layoutDelete = (LinearLayout) findViewById(R.id.layout_delete);
        layoutUpload = (LinearLayout) findViewById(R.id.layout_upload);

        mHandler = new Handler();

        mScrollView = (XScrollView) findViewById(R.id.scroll_user_video);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(this);
        mScrollView.setRefreshTime(UIHelper.getTime());

        View content = LayoutInflater.from(this).inflate(R.layout.vw_scroll_view_content, null);
        if (null != content) {
            lvVideo = (ListView) content.findViewById(R.id.content_list);
            lvVideo.setFocusable(false);
            lvVideo.setFocusableInTouchMode(false);
            getVideoData(GlobalEntity.PULL_UP);
        }

        mScrollView.setView(content);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void uploadClick() {
        layoutUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(UserVideoActivity.this, SelectPicPopupWindow.class), GlobalEntity.POPWINDOW_PICK_PHOTO);
            }
        });
    }

    private void deleteClick() {
        layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strVids = "";
                int selectCount = selectedVideoArray.size();
                if (selectCount > 0) {
                    for (int i = 0; i < selectCount; ++i) {
                        int videoId = videoList.get(selectedVideoArray.get(i)).getVideoId();
                        String strTemp = String.valueOf(videoId);
                        if (i < selectCount - 1) {
                            strTemp = String.valueOf(videoId) + ",";
                        }
                        strVids += strTemp;
                    }
                }
                String strUserId = String.valueOf(userId);
                postDeleteVideo(strUserId, strVids);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == GlobalEntity.POPWINDOW_TAKE_PHOTO) {
            UIHelper.ToastMessage(UserVideoActivity.this, "take");
            Intent intent = new Intent(UserVideoActivity.this, StartTackMovieActivity.class);
            startActivityForResult(intent, 200);

        } else if (resultCode == GlobalEntity.POPWINDOW_PICK_PHOTO) {
            UIHelper.ToastMessage(UserVideoActivity.this, "pick");

        } else if (resultCode == GlobalEntity.POPWINDOW_CANCEL) {

        }
    }

    private void postDeleteVideo(String strUserId, String strVids) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", strUserId);
        map.put("videoids", strVids);

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.VedioDeleteUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Message msg = new Message();
                msg.what = 4;
                myHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, map);

        request.setTag(requestTag);
        requestQueue.add(request);
    }

    private void getVideoData(final int status) {
        String videoUrl = String.format(GlobalEntity.VedioUrl, userId, mRefreshIndex);
        StringRequest request = new StringRequest(videoUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject dataJson = new JSONObject(s);
                    JSONObject listJson = dataJson.getJSONObject("result");
                    String strData = listJson.getString("VideoReviewList");
                    JSONArray data = new JSONArray(strData);
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); ++i) {
                            MyVideoInfo info = new MyVideoInfo();
                            info.setVideoId(data.getJSONObject(i).getInt("VideoId"));
                            info.setUserId(data.getJSONObject(i).getInt("UserId"));
                            info.setImgUrl(data.getJSONObject(i).getString("VideoThumPath").toString());
                            info.setTitle(data.getJSONObject(i).getString("Description").toString());
                            info.setTitle2(data.getJSONObject(i).getString("UserName"));
                            info.setVideoUrl(data.getJSONObject(i).getString("VideoPath"));
                            info.setDate(data.getJSONObject(i).getString("AddTime"));
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
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 3;
                myHandler.sendMessage(msg);
            }
        });
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {

                    adapter = new CommonAdapter<MyVideoInfo>(UserVideoActivity.this, videoList, R.layout.my_video_item) {
                        @Override
                        public void convert(final ViewHolder helper, MyVideoInfo item, final int position) {
                            helper.setImageByUrl(R.id.iv_icon, item.getImageUrl());
                            helper.setText(R.id.tv_title, item.getTitle());
                            helper.setText(R.id.tv_title2, item.getTitle2());
                            helper.setText(R.id.tv_date, item.getDate());
                            helper.setCheckBoxVisibility(R.id.cb_select, true);
                            helper.setSelectStatus(R.id.cb_select, item.getSelectStatus());
                            CheckBox cb = (CheckBox) helper.getView(R.id.cb_select);
                            cb.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl(item, position));
                        }

                        class OnCheckedChangeListenerImpl implements CompoundButton.OnCheckedChangeListener {
                            MyVideoInfo holder;
                            int position;

                            public OnCheckedChangeListenerImpl(MyVideoInfo holder, int position) {
                                super();
                                this.holder = holder;
                                this.position = position;
                            }

                            public void removeSelectedPosition() {
                                for (int i = 0; i < selectedVideoArray.size(); ++i) {
                                    int content = selectedVideoArray.get(i);
                                    if (position == content)
                                        selectedVideoArray.remove(i);
                                }
                            }

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    videoList.get(position).setSelectStatus(true);
                                    videoList.get(position).getUserId();
                                    selectedVideoArray.add(position);
                                } else {
                                    removeSelectedPosition();
                                    videoList.get(position).setSelectStatus(false);
                                }
//                                isSelected.put(position, isChecked);
                            }
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
                    break;
                }
                case 4: {
//                    int count = selectedVideoArray.size();
//                    for (int i=0; i<count; ++i){
//                        int idx = selectedVideoArray.get(i);
//                        videoList.remove(idx);
//                    }
//                    for(int j=0; j<videoList.size(); ++j){
//                        videoList.get(j).setSelectStatus(false);
//                    }
                    mRefreshIndex = 1;
                    videoList.clear();
                    selectedVideoArray.clear();
                    getVideoData(GlobalEntity.PULL_UP);
//                  adapter.notifyDataSetChanged();
                    break;
                }

                default:
                    break;
            }
        }
    };

    private void listItemClick() {
        lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String videoUrl = videoList.get(position).getVideoUrl();
                int vid = videoList.get(position).getVideoId();
                Intent intent = new Intent();
                intent.putExtra("vedioUrl", videoUrl);
                intent.putExtra("vedioId", vid);
                intent.setClass(UserVideoActivity.this, VedioActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshIndex = 1;
                videoList.clear();
                getVideoData(GlobalEntity.PULL_UP);
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ++mRefreshIndex;
                getVideoData(GlobalEntity.PULL_DOWN);
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
