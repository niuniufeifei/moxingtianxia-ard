package com.dgzd.mxtx.rong;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.login.LoginActivity;
import com.dgzd.mxtx.rong.activity.PhotoActivity;
import com.dgzd.mxtx.rong.activity.RealTimeLocationActivity;
import com.dgzd.mxtx.rong.activity.SOSOLocationActivity;
import com.dgzd.mxtx.rong.activity.UserInfoActivity;
import com.dgzd.mxtx.rong.model.UserInfos;
import com.dgzd.mxtx.rong.provider.AgreedFriendRequestMessage;
import com.dgzd.mxtx.rong.utils.GsonUtils;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.utils.UIHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;
import com.sea_monster.network.ApiCallback;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.DiscussionNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.notification.PushNotificationMessage;


/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。
 * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有：
 * 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。
 * 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。
 * 5、群组信息提供者：GetGroupInfoProvider。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
 * 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */
public final class RongCloudEvent implements RongIMClient.OnReceiveMessageListener, RongIM.OnSendMessageListener,
        RongIM.UserInfoProvider, RongIM.GroupInfoProvider, RongIM.ConversationBehaviorListener,
        RongIMClient.ConnectionStatusListener, RongIM.LocationProvider, RongIMClient.OnReceivePushMessageListener, RongIM.ConversationListBehaviorListener,
        ApiCallback, Handler.Callback, RongIM.GroupUserInfoProvider {

    private static final String TAG = RongCloudEvent.class.getSimpleName();

    private static RongCloudEvent mRongCloudInstance;
    private MxtxApplictaion myApp;
    private Context mContext;
    DbUtils db;
    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        db= DbUtils.create(mContext);
        myApp = MxtxApplictaion.getInstance();
        initDefaultListener();
    }


    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {
        RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
        RongIM.setConversationListBehaviorListener(this);//会话列表界面操作的监听器
        RongIM.getInstance().setSendMessageListener(this);//设置发出消息接收监听器.
        RongIM.setGroupUserInfoProvider(this, true);
//        RongIM.setOnReceivePushMessageListener(this);//自定义 push 通知。
        //消息体内是否有 userinfo 这个属性
//        RongIM.getInstance().setMessageAttachedUserInfo(true);
    }

    /**
     * 连接成功注册。
     * <p/>
     * 在RongIM-connect-onSuccess后调用。
     */
    public void setOtherListener() {

        // RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(this);//设置消息接收监听器。
        //  RongIM.getInstance().getRongIMClient().setConnectionStatusListener(this);//设置连接状态监听器。
        //  TextInputProvider textInputProvider = new TextInputProvider(RongContext.getInstance());
        //       RongIM.setPrimaryInputProvider(textInputProvider);
////        扩展功能自定义
//        InputProvider.ExtendProvider[] provider = {
//                new ImageInputProvider(RongContext.getInstance()),//图片
//                new CameraInputProvider(RongContext.getInstance()),//相机
//                new RealTimeLocationInputProvider(RongContext.getInstance()),//地理位置
//                new VoIPInputProvider(RongContext.getInstance()),// 语音通话
//        };
//
//        InputProvider.ExtendProvider[] provider1 = {
////                new PhotoInputProvider(RongContext.getInstance()),//图片
//                new ImageInputProvider(RongContext.getInstance()),//图片
//                new CameraInputProvider(RongContext.getInstance()),//相机
//                new RealTimeLocationInputProvider(RongContext.getInstance()),//地理位置
//               // new ContactsProvider(RongContext.getInstance()),//通讯录
//        };
//
//        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
//        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.DISCUSSION, provider1);
//        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider1);
//        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CUSTOMER_SERVICE, provider1);
//        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CHATROOM, provider1);
    }

    /**
     * 自定义 push 通知。
     *
     * @param msg
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onReceivePushMessage(PushNotificationMessage msg) {

        Intent intent = new Intent();
        Uri uri;

        intent.setAction(Intent.ACTION_VIEW);

        Conversation.ConversationType conversationType = msg.getConversationType();

        uri = Uri.parse("rong://" + RongContext.getInstance().getPackageName()).buildUpon().appendPath("conversationlist").build();
        intent.setData(uri);
        Log.d(TAG, "onPushMessageArrive-url:" + uri.toString());

        Notification notification = null;

        PendingIntent pendingIntent = PendingIntent.getActivity(RongContext.getInstance(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT < 11) {
            /*notification = new Notification(RongContext.getInstance().getApplicationInfo().icon, "自定义 notification", System.currentTimeMillis());
            notification.setLatestEventInfo(RongContext.getInstance(), "自定义 title", "这是 Content:" + msg.getObjectName(), pendingIntent);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_SOUND;*/
        } else {
            notification = new Notification.Builder(RongContext.getInstance())
                    .setLargeIcon(getAppIcon())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("自定义 notification")
                    .setContentTitle("自定义 title")
                    .setContentText("这是 Content:" + msg.getObjectName())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL).build();
        }
        NotificationManager nm = (NotificationManager) RongContext.getInstance().getSystemService(RongContext.getInstance().NOTIFICATION_SERVICE);
        nm.notify(0, notification);
        return true;
    }

    private Bitmap getAppIcon() {
        BitmapDrawable bitmapDrawable;
        Bitmap appIcon;
        bitmapDrawable = (BitmapDrawable) RongContext.getInstance().getApplicationInfo().loadIcon(RongContext.getInstance().getPackageManager());
        appIcon = bitmapDrawable.getBitmap();
        return appIcon;
    }

    /**
     * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
     *
     * @param message 接收到的消息的实体信息。
     * @param left    剩余未拉取消息数目。
     */
    @Override
    public boolean onReceived(Message message, int left) {

        MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            textMessage.getExtra();
            Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.d(TAG, "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.d(TAG, "onReceived-RichContentMessage:" + richContentMessage.getContent());
        } else if (messageContent instanceof InformationNotificationMessage) {//小灰条消息
            InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
            Log.e(TAG, "onReceived-informationNotificationMessage:" + informationNotificationMessage.getMessage());
            // if (DemoContext.getInstance() != null)
            // getFriendByUserIdHttpRequest = DemoContext.getInstance().getDemoApi().getUserInfoByUserId(message.getSenderUserId(), (ApiCallback<User>) this);
        } else if (messageContent instanceof AgreedFriendRequestMessage) {
            //好友添加成功消息
            AgreedFriendRequestMessage agreedFriendRequestMessage = (AgreedFriendRequestMessage) messageContent;
            Log.d(TAG, "onReceived-deAgreedFriendRequestMessage:" + agreedFriendRequestMessage.getMessage());
          /*  Intent in = new Intent();
            in.setAction(MainActivity.ACTION_DMEO_AGREE_REQUEST);
            in.putExtra("AGREE_REQUEST", true);
            mContext.sendBroadcast(in);*/
        } else if (messageContent instanceof ContactNotificationMessage) {//好友添加消息
            ContactNotificationMessage contactContentMessage = (ContactNotificationMessage) messageContent;
            Log.d(TAG, "onReceived-ContactNotificationMessage:getExtra;" + contactContentMessage.getExtra());
            Log.d(TAG, "onReceived-ContactNotificationMessage:+getmessage:" + contactContentMessage.getMessage().toString());
            /*Intent in = new Intent();
            in.setAction(MainActivity.ACTION_DMEO_RECEIVE_MESSAGE);
            in.putExtra("rongCloud", contactContentMessage);
            in.putExtra("has_message", true);
            mContext.sendBroadcast(in);*/
        } else if (messageContent instanceof DiscussionNotificationMessage) {//讨论组通知消息
            DiscussionNotificationMessage discussionNotificationMessage = (DiscussionNotificationMessage) messageContent;
            Log.d(TAG, "onReceived-discussionNotificationMessage:getExtra;" + discussionNotificationMessage.getOperator());
            setDiscussionName(message.getTargetId());
        } else {
            Log.d(TAG, "onReceived-其他消息，自己来判断处理");
        }

        return false;

    }

    /**
     * 讨论组名称修改后刷新本地缓存
     *
     * @param targetId 讨论组 id
     */
    private void setDiscussionName(String targetId) {

        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            RongIM.getInstance().getRongIMClient().getDiscussion(targetId, new RongIMClient.ResultCallback<Discussion>() {
                @Override
                public void onSuccess(Discussion discussion) {
                    RongIM.getInstance().refreshDiscussionCache(discussion);
                    Log.i(TAG, "------discussion.getName---" + discussion.getName());
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }

    /**
     * 消息发送前监听器处理接口（是否发送成功可以从SentStatus属性获取）。
     *
     * @param message 发送的消息实例。
     * @return 处理后的消息实例。
     */
    @Override
    public Message onSend(Message message) {
        MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.e("qinxiao", "--onSend:" + textMessage.getContent() + ", extra=" + message.getExtra());
        }
        return message;
    }

    /**
     * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
     *
     * @param message 消息。
     */
    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        Log.e("qinxiao", "onSent:" + message.getObjectName() + ", extra=" + message.getExtra());

        if (message.getSentStatus() == Message.SentStatus.FAILED) {

            if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {//不在聊天室

            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {//不在讨论组

            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {//不在群组

            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {//你在他的黑名单中
                // WinToast.toast(mContext, "你在对方的黑名单中");
                UIHelper.ToastMessage(mContext,"你在对方的黑名单中");
            }
        }

        MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.e(TAG, "onSent-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.d(TAG, "onSent-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.d(TAG, "onSent-RichContentMessage:" + richContentMessage.getContent());
        } else {
            Log.d(TAG, "onSent-其他消息，自己来判断处理");
        }
        return false;
    }

    /**
     * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
     *
     * @param userId 用户 Id。
     * @return 用户信息，（注：由开发者提供用户信息）。
     */
    @Override
    public UserInfo getUserInfo(String userId) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        if (userId == null)
            return null;
        com.dgzd.mxtx.rong.model.UserInfo userInfo = null;
        List<com.dgzd.mxtx.rong.model.UserInfo> userList = new ArrayList<com.dgzd.mxtx.rong.model.UserInfo>();
        try {
            if (db.findAll(com.dgzd.mxtx.rong.model.UserInfo.class) != null) {
                userList = db.findAll(com.dgzd.mxtx.rong.model.UserInfo.class);
                for (com.dgzd.mxtx.rong.model.UserInfo user : userList) {
                    if (userId.equals(String.valueOf(user.getUid()))) {
                        userInfo = user;
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (userInfo == null && DemoContext.getInstance() != null) {
            getUserInfothis(userId);
        }
        return DemoContext.getInstance().getUserInfoById(userId);
    }
    /**
     * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
     *
     * @param groupId 群组 Id.
     * @return 群组信息，（注：由开发者提供群组信息）。
     */
    @Override
    public Group getGroupInfo(String groupId) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        if (DemoContext.getInstance().getGroupMap() == null)
            return null;
        return DemoContext.getInstance().getGroupMap().get(groupId);
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
     *
     * @param context          应用当前上下文。
     * @param conversationType 会话类型。
     * @param user             被点击的用户的信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo user) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        if (user != null) {
            if (user.getUserId().equals(String.valueOf(myApp.personalInfo.getUid()))) {
//                Intent in = new Intent(context, MineActivity.class);
//                context.startActivity(in);
            } else {
                Intent in = new Intent(context, UserInfoActivity.class);
                in.putExtra("userid", user.getUserId());
                in.putExtra("type", "head");
                context.startActivity(in);
            }
        }
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        Log.e(TAG, "----onUserPortraitLongClick");

        return true;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
     *
     * @param context 应用当前上下文。
     * @param message 被点击的消息的实体信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onMessageClick(final Context context, final View view, final Message message) {
        Log.e(TAG, "----onMessageClick");
        /**
         * demo 代码 开发者需替换成自己的代码。
         */
        if (message.getContent() instanceof LocationMessage) {
            Intent intent = new Intent(context, SOSOLocationActivity.class);
            intent.putExtra("location", message.getContent());
            context.startActivity(intent);
        } else if (message.getContent() instanceof RichContentMessage) {
            RichContentMessage mRichContentMessage = (RichContentMessage) message.getContent();
            // Log.d("Begavior", "extra:" + mRichContentMessage.getExtra());

        } else if (message.getContent() instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) message.getContent();
            Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
            if (imageMessage.getThumUri() != null)
                intent.putExtra("thumbnail", imageMessage.getThumUri());
            context.startActivity(intent);
        }
        // Log.d("Begavior",message.getObjectName() + ":" +
        // message.getMessageId());
        return false;
    }

    private void startRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {
        RongIMClient.getInstance().startRealTimeLocation(conversationType, targetId);

        Intent intent = new Intent(((FragmentActivity) context), RealTimeLocationActivity.class);
        intent.putExtra("conversationType", conversationType.getValue());
        intent.putExtra("targetId", targetId);
        context.startActivity(intent);
    }

    private void joinRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {
        RongIMClient.getInstance().joinRealTimeLocation(conversationType, targetId);
        Intent intent = new Intent(((FragmentActivity) context), RealTimeLocationActivity.class);
        intent.putExtra("conversationType", conversationType.getValue());
        intent.putExtra("targetId", targetId);
        context.startActivity(intent);
    }

    /**
     * 当点击链接消息时执行。
     *
     * @param context 上下文。
     * @param link    被点击的链接。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLinkClick(Context context, String link) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {

        Log.e(TAG, "----onMessageLongClick");
        return false;
    }

    /**
     * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
     * @param status 网络状态。
     */
    @Override
    public void onChanged(ConnectionStatus status) {
        Log.d(TAG, "onChanged:" + status);
        switch (status) {
            case CONNECTED:// 连接成功

                break;
            case DISCONNECTED:// 断开连接
                myGetToken();
                break;
            case CONNECTING:// 连接中

                break;
            case NETWORK_UNAVAILABLE:// 网络不可用

                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT:// 其他设备登陆
                MxtxApplictaion.getInstance().logout();
                Intent intent = new Intent(mContext,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("otherLogin","yes");
                mContext.startActivity(intent);
			/*
			 * Intent intent=new Intent();
			 * intent.setClass(mContext,SignInActivity.class);
			 * intent.putExtra("dialog", "true");
			 * mContext.startActivity(intent);
			 */
                break;

            default:
                break;
        }
    }


    /**
     * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
     *
     * @param context  上下文
     * @param callback 回调
     */
    @Override
    public void onStartLocation(Context context, LocationCallback callback) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        DemoContext.getInstance().setLastLocationCallback(callback);
        Intent intent = new Intent(context, SOSOLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 点击会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationClick(Context context, View view, UIConversation conversation) {
        MessageContent messageContent = conversation.getMessageContent();
        Log.e(TAG, "--------onConversationClick-------");
        if (messageContent instanceof TextMessage) {//文本消息

            TextMessage textMessage = (TextMessage) messageContent;

            textMessage.getExtra();
        } else if (messageContent instanceof ContactNotificationMessage) {
            Intent intent = new Intent(context, UserInfoActivity.class);
            intent.putExtra("userid", conversation.getConversationSenderId().toString());
            intent.putExtra("type", "add");
            context.startActivity(intent);
            return true;
        }

        return false;
    }

    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    /**
     * 长按会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 长按会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation conversation) {
        return false;
    }


    @Override
    public void onComplete(AbstractHttpRequest abstractHttpRequest, Object obj) {
      /*  if (getUserInfoByUserIdHttpRequest != null && getUserInfoByUserIdHttpRequest.equals(abstractHttpRequest)) {
            if (obj instanceof User) {
                final User user = (User) obj;
                if (user.getCode() == 200) {
                    UserInfos addFriend = new UserInfos();
                    addFriend.setUsername(user.getResult().getUsername());
                    addFriend.setUserid(user.getResult().getId());
                    addFriend.setPortrait(user.getResult().getPortrait());
                    addFriend.setStatus("0");

                    UserInfo userInfo = new UserInfo(user.getResult().getId(), user.getResult().getUsername(), Uri.parse(user.getResult().getPortrait()));
                    RongIM.getInstance().refreshUserInfoCache(userInfo);

                    if (DemoContext.getInstance() != null)
                        DemoContext.getInstance().insertOrReplaceUserInfos(addFriend);
                }
            }
        } else if (getFriendByUserIdHttpRequest != null && getFriendByUserIdHttpRequest.equals(abstractHttpRequest)) {
            if (obj instanceof User) {
                final User user = (User) obj;
                if (user.getCode() == 200) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (DemoContext.getInstance() != null) {

                                if (DemoContext.getInstance().hasUserId(user.getResult().getId())) {
                                    DemoContext.getInstance().updateUserInfos(user.getResult().getId(), "1");
                                } else {
                                    UserInfo info = new UserInfo(user.getResult().getId(), user.getResult().getUsername(), Uri.parse(user.getResult().getPortrait()));
                                    DemoContext.getInstance().insertOrReplaceUserInfo(info, "1");
                                }
                            }
                        }
                    });

                }
            }
        }*/
    }

    @Override
    public void onFailure(AbstractHttpRequest abstractHttpRequest, BaseException e) {

    }

    @Override
    public boolean handleMessage(android.os.Message message) {
        return false;
    }

    /**
     * 可以根据群组 id 修改群成员的群昵称
     * @param groupId
     * @param userId
     * @return
     */
    @Override
    public GroupUserInfo getGroupUserInfo(String groupId, String userId) {

        GroupUserInfo groupUserInfo = new GroupUserInfo("49", "22830", "hehe");
        RongIM.getInstance().refreshGroupUserInfoCache(groupUserInfo);
        return groupUserInfo;
    }
    /**
     * 获取用户信息
     */
    private void getUserInfothis( String userId) {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 0);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userid",userId);
        http.send(HttpRequest.HttpMethod.GET, GlobalEntity.GET_USERINFO_APIS, params, new RequestCallBack<String>() {
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JsonObject result = GsonUtils.fromJson(stringResponseInfo.result, JsonObject.class);
                    String status = GsonUtils.getJsonValue(result.toString(), "status").toString();
                    if (status != null) {
                        if (status.equals("true")) {
                            UserInfos userInfo = GsonUtils.fromJson(result.get("result").toString(), UserInfos.class);
                            com.dgzd.mxtx.rong.model.UserInfo user= new com.dgzd.mxtx.rong.model.UserInfo();
                            user.setId(userInfo.getUid());
                            user.setAvatar(userInfo.getAvatar());
                            user.setUsername(userInfo.getUserName());
                            try {
                                db.save(user);
                            } catch (DbException e) {
                                e.printStackTrace();
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
     * 获取token
     */
    private void myGetToken() {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 0);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", String.valueOf(myApp.personalInfo.getUid()) );
        params.addQueryStringParameter("name", myApp.personalInfo.getUserName());
        params.addQueryStringParameter("portraitUri", myApp.personalInfo.getAvatar());
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
        if (mContext.getApplicationInfo().packageName.equals(MxtxApplictaion.getCurProcessName(mContext.getApplicationContext()))) {

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
                    UIHelper.ToastMessage(mContext,"连接成功！");
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

}
