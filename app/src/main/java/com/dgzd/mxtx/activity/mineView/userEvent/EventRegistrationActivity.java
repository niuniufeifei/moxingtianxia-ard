package com.dgzd.mxtx.activity.mineView.userEvent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.login.LoginActivity;
import com.dgzd.mxtx.adapter.CommonAdapter;
import com.dgzd.mxtx.entirety.MotoTripRegisterInfo;
import com.dgzd.mxtx.entirety.MotoTripReplyInfo;
import com.dgzd.mxtx.entirety.MotoTripSummaryImgInfo;
import com.dgzd.mxtx.entirety.ViewHolder;
import com.dgzd.mxtx.rong.utils.GsonUtils;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.tools.NormalPostRequest;
import com.dgzd.mxtx.utils.ImageUtils;
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

/**
 * @version V1.0 <活动报名>
 * @FileName: EventActivity.java
 * @author: Jessica
 * @date: 2015-12-28 14:43
 */

public class EventRegistrationActivity extends AppCompatActivity implements XScrollView.IXScrollViewListener {
    private ImageView ivBack;
    private TextView tvEventDetails;
    private TextView tvContact, tvEntryFee, tvContactNumber;
    private LinearLayout layoutApplyed;
    private Button btnWannaApply, btnRefund;
    private String contactPerson, contactMobile, summary, eventDetail;
    private TextView tvSummary;
    private GridView gvSummary;
    private EditText etPublicCommit;
    private TextView tvPublicCommit;

    private int activityId;
    private int money;

    private CommonAdapter adapter;
    private Handler mHandler;
    private int mRefreshIndex = 1;
    private XScrollView mScrollView;
    private ListView lvMotoTripComments;
    private List<MotoTripSummaryImgInfo> summaryImgList = new ArrayList<>();
    private List<MotoTripRegisterInfo> tripRegisterList = new ArrayList<>();
    private RequestQueue requestQueue;
    private String requestTag = "EventRegistrationActivity";
    private MxtxApplictaion myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration);
        requestQueue = Volley.newRequestQueue(this);
        myApp = MxtxApplictaion.getInstance();
        getBundleData();
        initView();
        handleByApplyStatus(false);
        backClick();
        eventDetailsClick();
        wannaApplyClick();
        refundClick();
        publicCommitClick();
    }

    private void getBundleData() {
        Intent intent = getIntent();
        activityId = intent.getIntExtra(GlobalEntity.MOTO_TRIP_ACTIVITY_ID, 2);
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvEventDetails = (TextView) findViewById(R.id.tv_event_details);
        tvContact = (TextView) findViewById(R.id.tv_contact);
        tvEntryFee = (TextView) findViewById(R.id.tv_entry_fee);
        tvContactNumber = (TextView) findViewById(R.id.tv_contact_number);
        layoutApplyed = (LinearLayout) findViewById(R.id.layout_applyed);
        btnWannaApply = (Button) findViewById(R.id.btn_wanna_apply);
        btnRefund = (Button) findViewById(R.id.btn_refund);
        tvSummary = (TextView) findViewById(R.id.tv_summary);
        gvSummary = (GridView) findViewById(R.id.gv_summary);
        tvPublicCommit = (TextView) findViewById(R.id.tv_public_commit);
        etPublicCommit = (EditText) findViewById(R.id.et_public_commit);
        etPublicCommit.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {

                if (view.getId() == R.id.et_public_commit) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        mHandler = new Handler();
        getEventSummaryData();

        mScrollView = (XScrollView) findViewById(R.id.scroll_event_comments);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(EventRegistrationActivity.this);
        mScrollView.setRefreshTime(UIHelper.getTime());

        View content = LayoutInflater.from(this).inflate(R.layout.vw_scroll_view_content, null);
        if (null != content) {
            lvMotoTripComments = (ListView) content.findViewById(R.id.content_list);
            lvMotoTripComments.setSelector(R.drawable.trans);
            lvMotoTripComments.setDividerHeight(1);
            lvMotoTripComments.setFocusable(false);
            lvMotoTripComments.setFocusableInTouchMode(false);
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

            UIHelper.ToastMessage(EventRegistrationActivity.this, "未报名不能提交评论！");
            return;
        } else {
            if (myApp.personalInfo.getUserName() == null) {
                UIHelper.ToastMessage(EventRegistrationActivity.this, "未报名不能提交评论！");
                return;
            }
        }
        Map<String, String> map = new HashMap<>();
        String strActivityId = String.valueOf(activityId);
        String strUserId = String.valueOf(myApp.personalInfo.getUid());
        map.put("ActivityId", strActivityId);
        map.put("Description", etPublicCommit.getText().toString().trim());
        map.put("UserId", strUserId);
        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.EventCommitUrl, new Response.Listener<JSONObject>() {
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

    private void handleByApplyStatus(boolean status) {
        if (status) {
            layoutApplyed.setVisibility(View.VISIBLE);
            btnWannaApply.setVisibility(View.GONE);
        } else {
            layoutApplyed.setVisibility(View.GONE);
            btnWannaApply.setVisibility(View.VISIBLE);
        }
    }

    private void wannaApplyClick() {
        btnWannaApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSigned = MxtxApplictaion.getInstance().isSignedIn();
                if (!isSigned) {
                    startActivityForResult(new Intent(EventRegistrationActivity.this, LoginActivity.class), GlobalEntity.REQUEST_LOGIN);
                } else {
//                    if (contactPerson != null) {
//                        if (contactPerson.length() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra(GlobalEntity.MOTO_TRIP_ACTIVITY_ID, activityId);
                    intent.setClass(EventRegistrationActivity.this, EventProtocolActivity.class);
                    startActivity(intent);
//                        }
//                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalEntity.REQUEST_LOGIN) {
            getEventSummaryData();
            getCommentData(GlobalEntity.PULL_UP);
        }
    }

    private void refundClick() {
        btnRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMsgToRefund();
            }
        });
    }

    private void postMsgToRefund() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("userid", "2");

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.EventRefundUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, map);
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void eventDetailsClick() {
        tvEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(GlobalEntity.MOTO_TRIP_ACTIVITY_DETAIL, eventDetail);
                intent.setClass(EventRegistrationActivity.this, EventDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getEventSummaryData() {
        if (myApp.personalInfo == null) {
            return;
        }
        int uid = myApp.personalInfo.getUid();
        String commodityUrl = String.format(GlobalEntity.ActivityRegisterInfoUrl, activityId, 1, uid);
        StringRequest listRequest = new StringRequest(commodityUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataJson = new JSONObject(response);
                    String strResult = dataJson.getString("result");
                    JSONObject activityListObject = new JSONObject(strResult);
                    //获取活动总结
                    String strActivityInfo = activityListObject.getString("ActivityInfo");
                    if (strActivityInfo.length() > 0) {
                        JSONObject activityInfoJson = new JSONObject(strActivityInfo);
                        if (activityInfoJson.length() > 0) {
                            Bundle bundle = new Bundle();

                            String tmpSummary = activityInfoJson.getString("Summary");
                            String tmpContactPerson = activityInfoJson.getString("ContactPerson");
                            String tmpContactMobile = activityInfoJson.getString("ContactMobile");
                            int tmpMoney = activityInfoJson.getInt("Money");
                            String tmpEventDetail = activityInfoJson.getString("detail");
                            bundle.putString("summary", tmpSummary);
                            bundle.putString("person", tmpContactPerson);
                            bundle.putString("mobile", tmpContactMobile);
                            bundle.putInt("money", tmpMoney);
                            bundle.putString("detail", tmpEventDetail);

                            Message msg = new Message();
                            msg.what = 4;
                            msg.obj = bundle;
                            myHandler.sendMessage(msg);
                        }
                    }

                    //获取活动总结图片
                    String strSummaryImgListInfo = activityListObject.getString("SummaryImgList");
                    JSONArray summaryImageArray = new JSONArray(strSummaryImgListInfo);
                    if (summaryImageArray.length() > 0) {
                        summaryImgList.clear();
                        for (int i = 0; i < summaryImageArray.length(); ++i) {
                            MotoTripSummaryImgInfo summaryImgInfo = new MotoTripSummaryImgInfo();
                            String imageUrl = summaryImageArray.getJSONObject(i).getString("ImageName");
                            summaryImgInfo.setPhotoUrl(imageUrl);
                            summaryImgInfo.setVideoFlag(false);
                            summaryImgList.add(summaryImgInfo);
                        }
                        Message msg3 = new Message();
                        msg3.what = 7;
                        myHandler.sendMessage(msg3);
                    }

                    int registerStatus = activityListObject.getInt("RegisterStatus");

                    boolean isSigned = MxtxApplictaion.getInstance().isSignedIn();
                    if (!isSigned) {
                        registerStatus = 0;
                    }
                    Message msg = new Message();
                    msg.what = 5;
                    msg.obj = registerStatus;
                    myHandler.sendMessage(msg);
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

    private void getCommentData(final int status) {
        int uid = myApp.personalInfo.getUid();
        String commodityUrl = String.format(GlobalEntity.ActivityRegisterInfoUrl, activityId, mRefreshIndex, uid);

        StringRequest listRequest = new StringRequest(commodityUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataJson = new JSONObject(response);
                    String strResult = dataJson.getString("result");
                    JSONObject activityListObject = new JSONObject(strResult);
                    String strReview = activityListObject.getString("ActivityCommentReviewInfo");
                    if (strReview.length() > 0) {
                        JSONArray reviewData = new JSONArray(strReview);
                        if (reviewData.length() > 0) {
                            for (int i = 0; i < reviewData.length(); ++i) {
                                MotoTripRegisterInfo info = new MotoTripRegisterInfo();
                                info.setUserName(reviewData.getJSONObject(i).getString("UserName"));
                                info.setUserComment(reviewData.getJSONObject(i).getString("Description"));
                                info.setPhotoUrl(reviewData.getJSONObject(i).getString("Avatar"));
                                info.setUserId(reviewData.getJSONObject(i).getInt("UserId"));
                                boolean isReplyNull = reviewData.getJSONObject(i).isNull("Reply");
                                if (!isReplyNull) {
                                    JSONArray replyData = reviewData.getJSONObject(i).getJSONArray("Reply");
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
                    showPersolInfo(msg);
                    break;
                }
                case 5: {
                    int RegisterStatus = (int) msg.obj;
//                    if (RegisterStatus == 0) {
//                        handleByApplyStatus(false);
//                    } else if (RegisterStatus == 1) {
//                        handleByApplyStatus(true);
//                    }
                    break;
                }
                case 7: {
                    showSummaryImage();
                    break;
                }
                case 8: {
                    UIHelper.ToastMessage(EventRegistrationActivity.this, "提交评论成功！");
                    mRefreshIndex = 1;
                    tripRegisterList.clear();
                    getCommentData(GlobalEntity.PULL_UP);
                    break;
                }
                case 9: {
                    UIHelper.ToastMessage(EventRegistrationActivity.this, "提交评论失败！");
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void showReplyInfo() {
        adapter = new CommonAdapter<MotoTripRegisterInfo>(EventRegistrationActivity.this,
                tripRegisterList, R.layout.moto_trip_register_item) {
            @Override
            public void convert(ViewHolder helper, final MotoTripRegisterInfo item, int position) {
                helper.setSrcImageByUrl(R.id.iv_user_photo, item.getPhotoUrl());
                helper.setText(R.id.tv_user_comment, item.getUserComment());
                helper.setText(R.id.tv_user_name, item.getUserName());

                List<MotoTripReplyInfo> tmpReplyInfoList = item.getOfficialReplyList();
                CommonAdapter replyAdapter = new CommonAdapter<MotoTripReplyInfo>(EventRegistrationActivity.this,
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
        lvMotoTripComments.setAdapter(adapter);
        lvMotoTripComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MotoTripRegisterInfo info = (MotoTripRegisterInfo) adapter.getItem(position);
                if (info.getUserId() != myApp.personalInfo.getUid()) {
                    addFriends(String.valueOf(info.getUserId()),info.getUserName());
                } else {
                    UIHelper.ToastMessage(EventRegistrationActivity.this, "不能添加自己为好友哦！");
                }
            }
        });
    }

    private void showPersolInfo(Message msg) {
        Bundle myBundle = (Bundle) msg.obj;
        money = myBundle.getInt("money");
        contactPerson = myBundle.getString("person");
        contactMobile = myBundle.getString("mobile");
        summary = myBundle.getString("summary");
        eventDetail = myBundle.getString("detail");

        String strEntryFee = String.format("%d", money);
        tvEntryFee.setText(strEntryFee);
        tvContact.setText(contactPerson);
        tvContactNumber.setText(contactMobile);
        if (null == summary || "".equals(summary) || "null".equals(summary)) {
            summary = getString(R.string.empty_tip);
        }
        tvSummary.setText(Html.fromHtml(summary));
    }

    private void showSummaryImage() {
        CommonAdapter summaryImgAdapter = new CommonAdapter<MotoTripSummaryImgInfo>(EventRegistrationActivity.this,
                summaryImgList, R.layout.moto_trip_summary_img_item) {
            @Override
            public void convert(ViewHolder helper, MotoTripSummaryImgInfo item, int position) {

                if (item.isVideo()) {
                    Bitmap bitmap = ImageUtils.getVideoThumbnail(item.getPhotoUrl());
                    helper.setImageBitmap(R.id.iv_image, bitmap);
                    helper.getView(R.id.iv_play).setVisibility(View.VISIBLE);
                } else {
                    helper.setSrcImageByUrl(R.id.iv_image, item.getPhotoUrl());
                    helper.getView(R.id.iv_play).setVisibility(View.GONE);
                }
            }
        };
        gvSummary.setAdapter(summaryImgAdapter);
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
        ListAdapter adapter = lvMotoTripComments.getAdapter();
        if (null == adapter) {
            return 0;
        }

        int totalHeight = 0;

        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, lvMotoTripComments);
            if (null == item) continue;
            // measure each item width and height
            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvMotoTripComments.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (lvMotoTripComments.getDividerHeight() * (adapter.getCount() - 1));
        lvMotoTripComments.setLayoutParams(params);
        return params.height;
    }

    /**
     * 添加好友
     */
    private void addFriends(final String TargetUserId,final String username) {
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
                    String status = GsonUtils.getJsonValue(result.toString(),"status").toString();
                    if (status!=null){
                        if (status.equals("true")){
                            String message = GsonUtils.getJsonValue(result.toString(),"message").toString();
                            UIHelper.ToastMessage(EventRegistrationActivity.this,message);
                            RongIM.getInstance().startConversation(EventRegistrationActivity.this, Conversation.ConversationType.PRIVATE,TargetUserId,username);
                        }else{
                            String message = GsonUtils.getJsonValue(result.toString(),"message").toString();
                            UIHelper.ToastMessage(EventRegistrationActivity.this,message);
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
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
