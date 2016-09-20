package com.dgzd.mxtx.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.entirety.AddressSelectInfo;
import com.dgzd.mxtx.entirety.PersonalInfo;
import com.dgzd.mxtx.rong.DemoContext;
import com.dgzd.mxtx.rong.RongCloudEvent;
import com.dgzd.mxtx.rong.provider.AgreedFriendRequestMessage;
import com.dgzd.mxtx.rong.provider.ContactNotificationMessageProvider;
import com.dgzd.mxtx.rong.provider.NewDiscussionConversationProvider;
import com.dgzd.mxtx.rong.provider.RealTimeLocationMessageProvider;
import com.dgzd.mxtx.wheelWidget.ArrayWheelAdapter;
import com.dgzd.mxtx.wheelWidget.OnWheelChangedListener;
import com.dgzd.mxtx.wheelWidget.WheelView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.ipc.RongExceptionHandler;

/**
 * @version V1.0 <全局类>
 * @FileName: MxtxApplictaion.java
 * @author: Jessica
 * @date: 2015-12-04 13:54
 */

public class MxtxApplictaion extends Application {
    public static PersonalInfo personalInfo = null;
    private static Stack<Activity> activityStack;
    private IWXAPI api;
    private JSONObject wechatInfo;

    private static final MxtxApplictaion instance = new MxtxApplictaion();

    public static MxtxApplictaion getInstance() {
//        if (instance == null) {
//            instance = new MxtxApplictaion();
//        }
        return instance;
    }

    public static RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 注意：
         *
         * IMKit SDK调用第一步 初始化
         *
         * context上下文
         *
         * 只有两个进程需要初始化，主进程和 push 进程
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "com.dgzd.mxtx".equals(getCurProcessName(getApplicationContext()))) {

            RongIM.init(this);

            /**
             * 融云SDK事件监听处理
             *
             * 注册相关代码，只需要在主进程里做。
             */
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

                RongContext.init(this);
                DemoContext.init(this);
                RongCloudEvent.getInstance().init(this);
                //RongCloudEvent.getInstance().setOtherListener();
                Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

                try {
                    RongIM.registerMessageType(AgreedFriendRequestMessage.class);
                    RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
                    RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
                    //@ 消息模板展示
                    RongContext.getInstance().registerConversationTemplate(new NewDiscussionConversationProvider());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        initialize();
    }

    private void initialize() {
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp("wx657be80de120b830");
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        initPersonalInfo();
    }

    public void initPersonalInfo() {
        if (personalInfo == null) {
            personalInfo = new PersonalInfo();
        }
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * 是否已登录
     *
     * @return
     */
    public boolean isSignedIn() {
        if (personalInfo == null) {
            return false;
        }
        boolean isEmpty = true;
        String name = personalInfo.getUserName();
        if (name != null) {
            isEmpty = "".equals(name);
        }
        return !isEmpty;
    }

    /**
     * 退出登录
     */
    public void logout() {
        personalInfo = null;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                finishActivity(activityStack.get(i));

            }
        }
        activityStack.clear();
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void AppExit(Context context) {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public void setWechatInfo(JSONObject wechatInfo) {
        this.wechatInfo = wechatInfo;
    }

    public void weixinPay() {
        if (null != wechatInfo) {
            PayReq req = new PayReq();
            Log.e("------------------", wechatInfo.toString());
            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
            try {
                req.appId = wechatInfo.getString("appid");
                req.partnerId = wechatInfo.getString("partnerid");
                req.prepayId = wechatInfo.getString("prepay_id");
                req.nonceStr = wechatInfo.getString("noncestr");
                req.timeStamp = wechatInfo.getString("timestamp");
                req.packageValue = wechatInfo.getString("package");
                req.sign = wechatInfo.getString("sign");
            } catch (JSONException e) {
                e.printStackTrace();
            }


//        req.appId = "wx657be80de120b830";
//        req.partnerId = "1311262401";
//        req.prepayId = "wx201602051348413a173cb60a0976323240";
//        req.nonceStr = "3d83fe6962ea4101a4dcd166173cc186";
//        req.timeStamp = "1454651316";
//        req.packageValue = "Sign=WXPay";
//        req.sign = "E6FB952F222EE929C25A23CC21107023";


//					req.extData			= "app data"; // optional
            Toast.makeText(getApplicationContext(), "正常调起支付", Toast.LENGTH_SHORT).show();
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信


//        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//        signParams.add(new BasicNameValuePair("appid", req.appId));

//        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//        signParams.add(new BasicNameValuePair("appid", req.appId));
//        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//        signParams.add(new BasicNameValuePair("package", req.packageValue));
//        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//        req.sign = genAppSign(signParams);
//        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//        signParams.add(new BasicNameValuePair("package", req.packageValue));
//        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//        req.sign = genAppSign(signParams);

            api.sendReq(req);
        } else {
//	        	Toast.makeText(PayActivity.this, "返回错误"+wechatInfo.getString("retmsg"), Toast.LENGTH_SHORT).show();
        }

    }

}
