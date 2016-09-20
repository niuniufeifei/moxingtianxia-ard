package com.dgzd.mxtx.rong.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.rong.model.UserInfos;
import com.dgzd.mxtx.rong.utils.GsonUtils;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.utils.UIHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import io.rong.imkit.RongIM;

/**
 * Created by nsd on 2016/2/26
 * Notes：
 */
public class UserInfoActivity extends AppCompatActivity {
    private ImageView mBackView,mUserHead;
    private TextView mTitle,mUserName,mBeginChat,mAgreeFriends,mAdd;
    private String username,userhead,userid,type;
    BitmapUtils bitmapUtils;
    private MxtxApplictaion myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        bitmapUtils = new BitmapUtils(this);
        username = getIntent().getStringExtra("username");
        userhead = getIntent().getStringExtra("userhead");
        userid = getIntent().getStringExtra("userid");
        type = getIntent().getStringExtra("type");
        myApp = MxtxApplictaion.getInstance();
        initView();
    }
    private void initView(){
        mBackView = (ImageView) findViewById(R.id.top_left);
        mUserHead = (ImageView) findViewById(R.id.user_head);
        mTitle = (TextView) findViewById(R.id.title);
        mUserName = (TextView) findViewById(R.id.username);
        mBeginChat = (TextView) findViewById(R.id.beginChat);
        mAgreeFriends = (TextView) findViewById(R.id.agreefriend);
        mAdd = (TextView) findViewById(R.id.add_friend);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加好友接口
                addFriends(userid);
            }
        });
        mAgreeFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agreeFriends(userid);
            }
        });
        if (userhead!=null){
            bitmapUtils.display(mUserHead,userhead);
        }
        mUserName.setText(username);
        if (type!=null){
            switch (type){
                //从好友列表跳转过来
                case "list":
                    mAgreeFriends.setVisibility(View.GONE);
                    mAdd.setVisibility(View.GONE);
                    break;
                //从会话列表跳转
                case "add":
                    mBeginChat.setVisibility(View.GONE);
                    getUserInfo();
                    break;
                case "head":
                    mBeginChat.setVisibility(View.GONE);
                    getUserInfo();
                    break;
            }
        }
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBeginChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(UserInfoActivity.this, userid, username);
                }
            }
        });
    }
    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 0);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid", userid);
        params.addQueryStringParameter("otheruserid", String.valueOf(myApp.personalInfo.getUid()));
        http.send(HttpRequest.HttpMethod.GET, GlobalEntity.GET_USERINFO_APIS, params, new RequestCallBack<String>() {
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JsonObject result = GsonUtils.fromJson(stringResponseInfo.result, JsonObject.class);
                    String status = GsonUtils.getJsonValue(result.toString(), "status").toString();
                    if (status != null) {
                        if (status.equals("true")) {
                            UserInfos userInfo = GsonUtils.fromJson(result.get("result").toString(), UserInfos.class);
                            if (userInfo.getAvatar() != null) {
                                bitmapUtils.display(mUserHead, userInfo.getAvatar());
                            }
                            mUserName.setText(userInfo.getUserName());
                                if (type != null) {
                                    switch (type) {
                                        //从会话列表跳转
                                        case "add":
                                            ///控制显示聊天还是添加好友
                                           if (userInfo.getFriendStatus()==0) {
                                                mBeginChat.setVisibility(View.GONE);
                                                mAgreeFriends.setVisibility(View.VISIBLE);
                                            } else if (userInfo.getFriendStatus()==1) {
                                                mBeginChat.setVisibility(View.VISIBLE);
                                                mAgreeFriends.setVisibility(View.GONE);
                                                mAdd.setVisibility(View.GONE);
                                            } else {
                                                mBeginChat.setVisibility(View.GONE);
                                                mAgreeFriends.setVisibility(View.GONE);
                                               mAdd.setVisibility(View.GONE);
                                            }
                                            break;
                                        //会话页面头像
                                        case "head":
                                            ///控制显示聊天还是添加好友
                                            if (userInfo.getFriendStatus()==0) {
                                                mBeginChat.setVisibility(View.GONE);
                                                mAgreeFriends.setVisibility(View.GONE);
                                                mAdd.setVisibility(View.VISIBLE);
                                            } else if (userInfo.getFriendStatus()==1) {
                                                mBeginChat.setVisibility(View.VISIBLE);
                                                mAgreeFriends.setVisibility(View.GONE);
                                                mAdd.setVisibility(View.GONE);
                                            } else {
                                                mBeginChat.setVisibility(View.GONE);
                                                mAgreeFriends.setVisibility(View.GONE);
                                                mAdd.setVisibility(View.GONE);
                                            }
                                            break;
                                    }
                                }
                        } else {
                            String message = GsonUtils.getJsonValue(result.toString(), "message").toString();

                        }
                    }
                } catch (JsonParseException e) {
                    // AppException.json(e).makeToast(getActivity());
                }
            }

            public void onStart() {
            }

            public void onFailure(HttpException e, String s) {
            }

            private void onComplete() {
            }
        });
    }
    /**
     * 添加好友
     */
    private void addFriends(String TargetUserId) {
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
                            UIHelper.ToastMessage(UserInfoActivity.this, message);
                        }else{
                            String message = GsonUtils.getJsonValue(result.toString(),"message").toString();
                            UIHelper.ToastMessage(UserInfoActivity.this,message);
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
    /**
     * 同意添加好友
     */
    private void agreeFriends(String TargetUserId) {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 0);
        RequestParams params = new RequestParams();
        params.addBodyParameter("SourceUserId", String.valueOf(myApp.personalInfo.getUid()));
        params.addBodyParameter("SourceNickName", myApp.personalInfo.getNickName());
        params.addBodyParameter("TargetUserId", TargetUserId);
        http.send(HttpRequest.HttpMethod.POST, GlobalEntity.GET_AGREEAPPLY_APIS, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JsonObject result = GsonUtils.fromJson(stringResponseInfo.result, JsonObject.class);
                    String status = GsonUtils.getJsonValue(result.toString(),"status").toString();
                    if (status!=null){
                        if (status.equals("true")){
                            String message = GsonUtils.getJsonValue(result.toString(),"message").toString();
                            UIHelper.ToastMessage(UserInfoActivity.this, message);
                            if (message.equals("添加好友成功")){
                                mBeginChat.setVisibility(View.VISIBLE);
                                mAgreeFriends.setVisibility(View.GONE);
                                mAdd.setVisibility(View.GONE);
                            }
                        }else{
                            String message = GsonUtils.getJsonValue(result.toString(),"message").toString();
                            UIHelper.ToastMessage(UserInfoActivity.this,message);
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
}
