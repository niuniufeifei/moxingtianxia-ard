package com.dgzd.mxtx.activity.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.adapter.CommonAdapter;
import com.dgzd.mxtx.custom.CustomVideoView;
import com.dgzd.mxtx.entirety.MotoTripRegisterInfo;
import com.dgzd.mxtx.entirety.MotoTripReplyInfo;
import com.dgzd.mxtx.entirety.ViewHolder;
import com.dgzd.mxtx.rong.utils.GsonUtils;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.tools.NormalPostRequest;
import com.dgzd.mxtx.utils.UIHelper;
import com.dgzd.mxtx.widget.XScrollView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;


public class VedioActivity extends Activity implements XScrollView.IXScrollViewListener {
    private EditText etPublicCommit;
    private TextView tvPublicCommit;
    private CommonAdapter adapter;
    private Handler mHandler;
    private int mRefreshIndex = 1;
    private XScrollView mScrollView;
    private ListView lvVedioComments;
    private List<MotoTripRegisterInfo> tripRegisterList = new ArrayList<>();
    private RequestQueue requestQueue;
    private String requestTag = "VedioActivity";
    private MxtxApplictaion myApp;

    private int vid;
    private String videoName = "http://115.231.144.58/7/w/c/z/e/wczekrqjbpadrrpmbcwvvhlowbgkyw/hc.yinyuetai.com/09150146EAB702F03AC13BD20D0E6174.flv?sc=2b046d3c3022cffa&br=776&vid=2083657&aid=27434&area=KR&vst=0&ptp=mv&rd=yinyuetai.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.activity_vedio);
        requestQueue = Volley.newRequestQueue(this);
        myApp = MxtxApplictaion.getInstance();
        getBundleData();
        initView();
        publicCommitClick();
    }

    private void getBundleData() {
        Intent intent = getIntent();
        videoName = intent.getStringExtra("vedioUrl");
        vid = intent.getIntExtra("vedioId", 0);
    }

    private void initView() {
        tvPublicCommit = (TextView) findViewById(R.id.tv_public_commit);
        etPublicCommit = (EditText) findViewById(R.id.et_public_commit);

        CustomVideoView videoView = (CustomVideoView) findViewById(R.id.video1);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Uri.parse(videoName));
        videoView.requestFocus();
        videoView.start();

        mHandler = new Handler();

        mScrollView = (XScrollView) findViewById(R.id.scroll_event_comments);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(VedioActivity.this);
        mScrollView.setRefreshTime(UIHelper.getTime());

        View content = LayoutInflater.from(this).inflate(R.layout.vw_scroll_view_content, null);
        if (null != content) {
            lvVedioComments = (ListView) content.findViewById(R.id.content_list);
            lvVedioComments.setSelector(R.drawable.trans);
            lvVedioComments.setDividerHeight(1);
            lvVedioComments.setFocusable(false);
            lvVedioComments.setFocusableInTouchMode(false);
            getCommentData(GlobalEntity.PULL_UP);
        }

        mScrollView.setView(content);
    }

    private void publicCommitClick() {
        tvPublicCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCommitData();
            }
        });
    }

    private void postCommitData() {
        if (myApp.personalInfo == null) {

            UIHelper.ToastMessage(VedioActivity.this, "未报名不能提交评论！");
            return;
        } else {
            if (myApp.personalInfo.getUserName() == null) {
                UIHelper.ToastMessage(VedioActivity.this, "未报名不能提交评论！");
                return;
            }
        }
        Map<String, String> map = new HashMap<>();
        String strVid = String.valueOf(vid);
        String strUserId = String.valueOf(myApp.personalInfo.getUid());
        String strPage = String.valueOf(mRefreshIndex);
        map.put("VId", strVid);
        map.put("Description", etPublicCommit.getText().toString().trim());
        map.put("UserId", strUserId);
        map.put("page", strPage);
        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.AddVedioCommentUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Message msg = new Message();
                msg.what = 8;
                myHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 9;
                myHandler.sendMessage(msg);
            }
        }, map);
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    private void getCommentData(final int status) {
        String commodityUrl = String.format(GlobalEntity.GetVedioDetailUrl, vid, mRefreshIndex);

        StringRequest listRequest = new StringRequest(commodityUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataJson = new JSONObject(response);
                    boolean isOk = dataJson.getBoolean("status");
                    if (isOk) {
                        JSONObject activityListObject = dataJson.getJSONObject("result");
                        JSONArray videoCommentArray = activityListObject.getJSONArray("VideoCommentReviewList");
                        if (videoCommentArray.length() > 0) {
                            for (int i = 0; i < videoCommentArray.length(); ++i) {
                                MotoTripRegisterInfo info = new MotoTripRegisterInfo();
                                info.setUserName(videoCommentArray.getJSONObject(i).getString("UserName"));
                                info.setUserComment(videoCommentArray.getJSONObject(i).getString("Description"));
                                info.setPhotoUrl(videoCommentArray.getJSONObject(i).getString("Avatar"));
                                info.setUserId(videoCommentArray.getJSONObject(i).getInt("UserId"));
                                boolean isReplyNull = videoCommentArray.getJSONObject(i).isNull("Reply");
                                if (!isReplyNull) {
                                    JSONArray replyData = videoCommentArray.getJSONObject(i).getJSONArray("Reply");
                                    int repleyCount = replyData.length();
                                    if (repleyCount > 0) {
                                        List<MotoTripReplyInfo> replyList = new ArrayList<>();
                                        for (int j = 0; j < repleyCount; ++j) {
                                            String tmpOfficialReply = replyData.getJSONObject(j).getString("Description");
                                            if (tmpOfficialReply.length() > 0) {
                                                MotoTripReplyInfo replyInfo = new MotoTripReplyInfo();
                                                replyInfo.setOfficialReply(tmpOfficialReply);
                                                replyInfo.setOfficialPhotoUrl(replyData.getJSONObject(j).getString("ReplyUserAvatar"));
                                                replyList.add(replyInfo);
                                            }
                                        }
                                        info.setOfficialReplyList(replyList);
                                        tripRegisterList.add(info);
                                    } else {
                                        tripRegisterList.add(info);
                                    }
                                } else {
                                    tripRegisterList.add(info);
                                }
                            }
                            Message msg = new Message();
                            msg.what = status;
                            myHandler.sendMessage(msg);

                        } else {
                            Message msg = new Message();
                            msg.what = 3;
                            myHandler.sendMessage(msg);
                        }
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
                    showReplyInfo();
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
//                    showPersolInfo(msg);
                    break;
                }
                case 5: {
//                    int RegisterStatus = (int) msg.obj;
//                    if (RegisterStatus == 0) {
//                        handleByApplyStatus(false);
//                    } else if (RegisterStatus == 1) {
//                        handleByApplyStatus(true);
//                    }
//                    break;
                }
                case 7: {
//                    showSummaryImage();
                    break;
                }
                case 8: {
                    UIHelper.ToastMessage(VedioActivity.this, "提交评论成功！");
                    mRefreshIndex = 1;
                    tripRegisterList.clear();
                    getCommentData(GlobalEntity.PULL_UP);
                    break;
                }
                case 9: {
                    UIHelper.ToastMessage(VedioActivity.this, "提交评论失败！");
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void showReplyInfo() {
        adapter = new CommonAdapter<MotoTripRegisterInfo>(VedioActivity.this,
                tripRegisterList, R.layout.moto_trip_register_item) {
            @Override
            public void convert(ViewHolder helper, final MotoTripRegisterInfo item, int position) {
                helper.setSrcImageByUrl(R.id.iv_user_photo, item.getPhotoUrl());
                helper.setText(R.id.tv_user_comment, item.getUserComment());
                helper.setText(R.id.tv_user_name, item.getUserName());
                List<MotoTripReplyInfo> tmpReplyInfoList = item.getOfficialReplyList();
                CommonAdapter replyAdapter = new CommonAdapter<MotoTripReplyInfo>(VedioActivity.this,
                        tmpReplyInfoList, R.layout.moto_trip_reply_item) {
                    @Override
                    public void convert(ViewHolder replyHelpr, MotoTripReplyInfo replyItem, int replyPosition) {
                        String replyContent = replyItem.getOfficialReply();
                        if (replyContent == null) {
                            replyHelpr.getView(R.id.layout_root).setVisibility(View.GONE);
                        } else {
                            replyHelpr.getView(R.id.layout_root).setVisibility(View.VISIBLE);
                            replyHelpr.setSrcImageByUrl(R.id.iv_official_photo, replyItem.getOfficialPhotoUrl());
                            replyHelpr.setText(R.id.tv_official_reply, replyItem.getOfficialReply());
                        }
                    }
                };
                helper.setAdapter(R.id.lv_reply, replyAdapter);
            }
        };
        lvVedioComments.setAdapter(adapter);
        lvVedioComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MotoTripRegisterInfo info = (MotoTripRegisterInfo) adapter.getItem(position);
                if (info.getUserId() != myApp.personalInfo.getUid()) {
                    addFriends(String.valueOf(info.getUserId()), info.getUserName());
                } else {
                    UIHelper.ToastMessage(VedioActivity.this, "不能添加自己为好友哦！");
                }
            }
        });
    }

    /**
     * 添加好友
     */
    private void addFriends(final String TargetUserId, final String username) {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 0);
        RequestParams params = new RequestParams();
        params.addBodyParameter("SourceUserId", String.valueOf(myApp.personalInfo.getUid()));
        params.addBodyParameter("SourceNickName", myApp.personalInfo.getNickName());
        params.addBodyParameter("TargetUserId", TargetUserId);
        http.send(HttpRequest.HttpMethod.POST, GlobalEntity.GET_ADDFRIEND_APIS, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JsonObject result = GsonUtils.fromJson(stringResponseInfo.result, JsonObject.class);
                    String status = GsonUtils.getJsonValue(result.toString(), "status").toString();
                    if (status != null) {
                        if (status.equals("true")) {
                            String message = GsonUtils.getJsonValue(result.toString(), "message").toString();
                            UIHelper.ToastMessage(VedioActivity.this, message);
                            RongIM.getInstance().startConversation(VedioActivity.this, Conversation.ConversationType.PRIVATE, TargetUserId, username);
                        } else {
                            String message = GsonUtils.getJsonValue(result.toString(), "message").toString();
                            UIHelper.ToastMessage(VedioActivity.this, message);
                        }
                    }

                } catch (JsonParseException e) {
                }
                onComplete();
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(HttpException e, String s) {
                onComplete();
            }

            private void onComplete() {
            }
        });
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshIndex = 1;
                tripRegisterList.clear();
                getCommentData(GlobalEntity.PULL_UP);
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ++mRefreshIndex;
                getCommentData(GlobalEntity.PULL_DOWN);
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
        ListAdapter adapter = lvVedioComments.getAdapter();
        if (null == adapter) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, lvVedioComments);
            if (null == item) continue;
            // measure each item width and height
            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvVedioComments.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (lvVedioComments.getDividerHeight() * (adapter.getCount() - 1));
        lvVedioComments.setLayoutParams(params);
        return params.height;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
