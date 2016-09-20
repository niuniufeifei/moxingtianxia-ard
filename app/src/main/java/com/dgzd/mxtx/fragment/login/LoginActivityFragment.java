package com.dgzd.mxtx.fragment.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.login.ForgetPasswordActivity;
import com.dgzd.mxtx.activity.login.RegisterActivity;
import com.dgzd.mxtx.custom.ImageEditView;
import com.dgzd.mxtx.entirety.PersonalInfo;
import com.dgzd.mxtx.entirety.TecentLoginInfo;
import com.dgzd.mxtx.rong.RongCloudEvent;
import com.dgzd.mxtx.rong.model.UserInfo;
import com.dgzd.mxtx.rong.utils.GsonUtils;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.tools.NormalPostRequest;
import com.dgzd.mxtx.utils.UIHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {
    private View view;
    private ImageView ivBack;
    private Button btnLogin, btnRegister;
    private ImageView ivQqLogin;
    private TextView tvQQLogin, tvForgetPassword;
    private ImageEditView ieUsername, iePassword;
    private String strUsername, strPassword;
    private int cityId;
    private String strProvince, strCity, strAreaName;
    private RequestQueue requestQueue;
    private String requestTag = "LoginActivityFragment";
    private MxtxApplictaion myApp;
    private String userId, portraitUri, name;
    private UMSocialService mController;
    private String openid;
    private String otherLogin;
    DbUtils db;

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        myApp = MxtxApplictaion.getInstance();
        db = DbUtils.create(getActivity());
        otherLogin = getActivity().getIntent().getStringExtra("otherLogin");
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), "101282743", "95c9bce51cdcfa4917c94db283b626cd");
        qqSsoHandler.addToSocialSDK();
        Log.e("myTTTTag", "login now");

        initView();
        backClick();
        loginClick();
        registerClick();
        qqLoginClick();
        forgetPasswordClick();
        return view;
    }

    private void initView() {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        ivQqLogin = (ImageView) view.findViewById(R.id.iv_qq_login);
        ieUsername = (ImageEditView) view.findViewById(R.id.ie_username);
        iePassword = (ImageEditView) view.findViewById(R.id.ie_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnRegister = (Button) view.findViewById(R.id.btn_register);
        tvQQLogin = (TextView) view.findViewById(R.id.tv_qq_login);
        tvForgetPassword = (TextView) view.findViewById(R.id.tv_forget_password);
        if (otherLogin != null) {
            new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.otherlogin_tip)).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 点击“确认”后的操作

                }
            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void loginClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void registerClick() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });
    }

    private void forgetPasswordClick() {
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
            }
        });
    }

    private void getLoginData() {
        strUsername = ieUsername.getText();
        strPassword = iePassword.getText();
    }

    private void login() {
        getLoginData();

        String loginUrl = String.format(GlobalEntity.LoginUrl, strUsername, strPassword);
        StringRequest listRequest = new StringRequest(loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataJson = new JSONObject(response);
                    boolean status = dataJson.getBoolean("status");
                    if (status) {
                        JSONObject resultJson = dataJson.getJSONObject("result");
                        String userName = resultJson.getString("UserName");
                        String password = resultJson.getString("Password");
                        String cellPhone = resultJson.getString("Mobile");
                        int uid = resultJson.getInt("Uid");
                        userId = String.valueOf(uid);
                        name = userName;
                        portraitUri = resultJson.getString("Avatar");
                        cityId = resultJson.getInt("CityId");
                        int userRid = resultJson.getInt("UserRid");
                        String strNickName = resultJson.getString("NickName");
                        UserInfo user = new UserInfo();
                        user.setId(uid);
                        user.setUid(uid);
                        user.setAvatar(portraitUri);
                        user.setUsername(userName);
                        try {
                            db.save(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        myApp.initPersonalInfo();
                        myApp.personalInfo.setUserName(userName);
                        myApp.personalInfo.setPassword(password);
                        myApp.personalInfo.setUid(uid);
                        myApp.personalInfo.setUserRid(userRid);
                        myApp.personalInfo.setAvatar(portraitUri);
                        myApp.personalInfo.setNickName(strNickName);
                        myApp.personalInfo.setMobile(cellPhone);
                        myApp.personalInfo.setStoreId(resultJson.getInt("StoreId"));
                        myApp.personalInfo.setStoreName(resultJson.getString("StoreName").trim());
                        myApp.personalInfo.setStoreLogo(resultJson.getString("StoreLogo").trim());
                        myApp.personalInfo.setRegionid(resultJson.getInt("Regionid"));
                        int cityId = resultJson.getInt("CityId");
                        getAddressData(cityId);
                        Message msg = new Message();
                        msg.what = 1;
                        myHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 4;
                        myHandler.sendMessage(msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 4;
                    myHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 4;
                myHandler.sendMessage(msg);
            }
        });

        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    private void getAddressData(final int regionId) {
        String loginUrl = String.format(GlobalEntity.getCityUrl, cityId);
        StringRequest listRequest = new StringRequest(loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataJson = new JSONObject(response);
                    boolean status = dataJson.getBoolean("status");
                    if (status) {
                        JSONArray dataArray = dataJson.getJSONArray("result");
                        if (dataArray.length() > 0) {
                            for (int i = 0; i < dataArray.length(); ++i) {
                                int tepRegionId = dataArray.getJSONObject(i).getInt("RegionId");
                                if (tepRegionId == regionId) {
                                    strProvince = dataArray.getJSONObject(i).getString("ProvinceName").trim();
                                    strCity = dataArray.getJSONObject(i).getString("CityName").trim();
                                    strAreaName = dataArray.getJSONObject(i).getString("Name").trim();
                                    String strAddress = strProvince + strCity + strAreaName;
                                    myApp.personalInfo.setAddress(strAddress);

                                    Message msg = new Message();
                                    msg.what = 1;
                                    myHandler.sendMessage(msg);
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 5;
                    myHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                UIHelper.ToastMessage(getActivity(), "获取地址失败");
                Message msg = new Message();
                msg.what = 6;
                myHandler.sendMessage(msg);
            }
        });

        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    private void qqLoginClick() {
        ivQqLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqInit();
            }
        });
    }

    private void qqInit() {
        mController.doOauthVerify(getActivity(), SHARE_MEDIA.QQ, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                Toast.makeText(getActivity(), "授权开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Toast.makeText(getActivity(), "授权错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                Toast.makeText(getActivity(), "授权完成", Toast.LENGTH_SHORT).show();
                openid = value.getString("openid");
                final TecentLoginInfo qqInfo = new TecentLoginInfo();

                String tmpPayToken = value.getString("pay_token");
                String tmpAccessToken = value.getString("access_token");
                String tmpOpenId = value.getString("openid");
                String tmpRedId = value.getString("ret");
                if (tmpRedId != null) {
                    if (tmpRedId.length() > 0) {
                        int redid = Integer.valueOf(tmpRedId);
                        qqInfo.setRetId(redid);
                    }
                }
                qqInfo.setPayToken(tmpPayToken);
                qqInfo.setAccessToken(tmpAccessToken);
                qqInfo.setOpenId(tmpOpenId);

                // 获取相关授权信息
                mController.getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(getActivity(), "获取平台数据开始...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keys = info.keySet();
                            if (keys.size() > 0) {
                                String tmpMsg = info.get("msg").toString();
                                String tmpProfileImageUrl = info.get("profile_image_url").toString();

                                qqInfo.setMsg(tmpMsg);
                                qqInfo.setProfileImageUrl(tmpProfileImageUrl);

                                MxtxApplictaion.getInstance().initPersonalInfo();
                                MxtxApplictaion.getInstance().personalInfo.setTecentLoginInfo(qqInfo);
                            }

                            Message msg = new Message();
                            msg.what = 2;
                            myHandler.sendMessage(msg);
                        } else {
                            Log.d("TestData", "发生错误：" + status);
                            Message msg = new Message();
                            msg.what = 4;
                            myHandler.sendMessage(msg);
                        }
                    }
                });
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(getActivity(), "授权取消", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void postThirdRegister(String strUserId, String strVids, String strMsg, final String StrNickName, int strRetId) {
        Map<String, String> map = new HashMap<String, String>();

        map.put("OpenId", strUserId);
        map.put("Ret", strVids);
        map.put("Msg", strMsg);
        map.put("NickName", StrNickName);

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.QQThridPartRegisterUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (!jsonObject.isNull("result")) {
                    try {
                        JSONObject resultObject = jsonObject.getJSONObject("result");
                        String strUserName = resultObject.getString("UserName");
                        String strImageUrl = resultObject.getString("Avatar");
                        MxtxApplictaion.getInstance().personalInfo.setUserName(strUserName);
                        MxtxApplictaion.getInstance().personalInfo.setAvatar(strImageUrl);

                        Message msg = new Message();
                        msg.what = 3;
                        myHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = 4;
                        myHandler.sendMessage(msg);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 4;
                myHandler.sendMessage(msg);
            }
        }, map);
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    UIHelper.ToastMessage(getActivity(), getString(R.string.longin_success).toString());
                    getActivity().setResult(GlobalEntity.REQUEST_LOGIN);
                    myGetToken();
                    // getActivity().finish();
                    break;
                }
                case 2: {
                    String payToken = myApp.personalInfo.tecentLoginInfo.getPayToken();
                    String accessToken = myApp.personalInfo.tecentLoginInfo.getAccessToken();
                    String openId = myApp.personalInfo.tecentLoginInfo.getOpenId();
                    String strMsg = myApp.personalInfo.tecentLoginInfo.getMsg();
                    int retId = myApp.personalInfo.tecentLoginInfo.getRetId();
                    postThirdRegister(payToken, accessToken, openId, strMsg, retId);
                    break;
                }
                case 3: {
                    getActivity().setResult(GlobalEntity.REQUEST_LOGIN);
                    getActivity().finish();
                    break;
                }
                case 4: {
                    UIHelper.ToastMessage(getActivity(), getString(R.string.longin_failed).toString());
                    getActivity().setResult(GlobalEntity.REQUEST_LOGIN_FAILED);
                    break;
                }
                case 5: {
                    UIHelper.ToastMessage(getActivity(), "获取地址为空");
                    UIHelper.ToastMessage(getActivity(), getString(R.string.longin_success).toString());
                    getActivity().setResult(GlobalEntity.REQUEST_LOGIN);
                    myGetToken();
                    break;
                }
                case 6: {
                    UIHelper.ToastMessage(getActivity(), "获取地址失败");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 获取token
     */
    private void myGetToken() {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 0);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", userId);
        params.addQueryStringParameter("name", name);
        params.addQueryStringParameter("portraitUri", portraitUri);
        http.send(HttpRequest.HttpMethod.GET, GlobalEntity.GET_TOKEN_APIS, params, new RequestCallBack<String>() {
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JsonObject result = GsonUtils.fromJson(stringResponseInfo.result, JsonObject.class);
                    String token = GsonUtils.getJsonValue(result.toString(), "result").toString();
                    connect(token);
                    //Toast.makeText(SignInActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                } catch (JsonParseException e) {
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
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {
        if (getActivity().getApplicationInfo().packageName.equals(MxtxApplictaion.getCurProcessName(getActivity().getApplicationContext()))) {
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
                 * Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid
                 * 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("LoginActivity", "--onSuccess" + userid);
                    RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(RongCloudEvent.getInstance());//设置消息接收监听器。
                    RongIM.getInstance().getRongIMClient().setConnectionStatusListener(RongCloudEvent.getInstance());//设置连接状态监听器。
                    getActivity().finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode
                 * 错误码，可到官网 查看错误码对应的注释
                 * http://www.rongcloud.cn/docs/android.html#常见错误码
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }

    /**
     * 获取好友信息
     */
    private void getFriends() {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 0);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", userId);
        http.send(HttpRequest.HttpMethod.GET, GlobalEntity.GET_FRIENDSLISTS_APIS, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JsonObject result = GsonUtils.fromJson(stringResponseInfo.result, JsonObject.class);
                    List<PersonalInfo> mListData = GsonUtils.fromJson(result.get("Data").toString(), new TypeToken<List<PersonalInfo>>() {
                    }.getType());
                    if (mListData == null || mListData.size() == 0) {
                        // mEmptyView.setText("暂无好友");
                    } else {
                        /*for (int i = 0; i < mListData.size(); i++) {
                            UserInfo userInfo = mListData.get(i);
                            userInfo.setId(Integer.parseInt(mListData.get(i).getMemberId()));
                            try {
                                dbUtils.save(userInfo);
                            } catch (DbException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }*/
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
                //getGroups();
            }
        });
    }

    /**
     * 获取群组信息
     */
   /* private void getGroups() {
        // TODO Auto-generated method stub
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 0);
        RequestParams params = new RequestParams();
        http.send(HttpRequest.HttpMethod.GET, WebUtils.getGroupsUrl(app.getUser().getMemberId(), "", "345654"), params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JsonObject result = GsonUtils.fromJson(stringResponseInfo.result, JsonObject.class);
                    List<GroupMine> mListData = GsonUtils.fromJson(result.get("Data").toString(), new TypeToken<List<GroupMine>>() {
                    }.getType());
                    if (mListData == null || mListData.size() == 0) {
                        // mEmptyView.setText("暂无数据");
                    } else {
                        for (GroupMine i: mListData) {
                            try {
                                dbUtils.saveOrUpdate(i);
                            } catch (DbException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (JsonParseException e) {
                    // AppException.json(e).makeToast(getActivity());
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
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });
    }*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}

