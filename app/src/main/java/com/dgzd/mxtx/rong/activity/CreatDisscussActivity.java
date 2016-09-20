package com.dgzd.mxtx.rong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.rong.model.UserInfo;
import com.dgzd.mxtx.rong.utils.GsonUtils;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.utils.UIHelper;
import com.dgzd.mxtx.widget.XListView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by nsd on 2016/2/26.
 * Notes：
 */
public class CreatDisscussActivity extends Activity {
    private ImageView mBackView;
    private TextView mTitle, mCreate;
    BitmapUtils bitmapUtils;
    private MxtxApplictaion myApp;
    QuickAdapter<UserInfo> mPleasesAdapter;
    private TextView mEmptyView;
    private XListView mListView;
    DbUtils db;
    ArrayList<String> userIds = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatediscuss);
        bitmapUtils = new BitmapUtils(this);
        myApp = MxtxApplictaion.getInstance();
        db = DbUtils.create(this);
        bitmapUtils = new BitmapUtils(this);
        initView();
        getFriends();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.top_left);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mListView = (XListView) findViewById(R.id.listview);
        mTitle = (TextView) findViewById(R.id.title);
        mCreate = (TextView) findViewById(R.id.create);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 创建讨论组会话并进入会话界面。
                 *
                 * @param context       应用上下文。
                 * @param targetuserids 要与之聊天的讨论组用户 id 列表。
                 * @param title         聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
                 */
                if (userIds.size() != 0) {

                    if (RongIM.getInstance() != null)
                    /**
                     *创建讨论组时，mLists为要添加的讨论组成员，创建者一定不能在 mLists 中
                     */
                        RongIM.getInstance().getRongIMClient().createDiscussion(listToString(names), userIds, new RongIMClient.CreateDiscussionCallback() {
                            @Override
                            public void onSuccess(String s) {
                                Toast.makeText(CreatDisscussActivity.this, s, Toast.LENGTH_SHORT).show();
                                RongIM.getInstance().startDiscussionChat(CreatDisscussActivity.this, s, listToString(names));
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                } else {
                    UIHelper.ToastMessage(CreatDisscussActivity.this, "请选择好友后创建哦！");
                }

            }
        });
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setEmptyView(mEmptyView);
        mListView.setPullLoadEnable(false);
        mPleasesAdapter = new QuickAdapter<UserInfo>(this, R.layout.listitem_friends) {
            protected void convert(BaseAdapterHelper helper, final UserInfo item) {
                helper.setText(R.id.username, item.getUsername());
                ImageView view = helper.getView(R.id.user_head);
                if (item.getAvatar() != null) {
                    bitmapUtils.display(view, item.getAvatar());
                }
                CheckBox checkBox = helper.getView(R.id.select);
                checkBox.setTag(item.getUid());
            }
        };
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getFriends();
            }

            @Override
            public void onLoadMore() {
            }
        });
        mListView.setAdapter(mPleasesAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 1)
                    return; // 点击HeaderView无效
                UserInfo userInfo = mPleasesAdapter.getItem(position - 1);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.select);
                if (checkBox.getTag().toString().equals(String.valueOf(userInfo.getUid()))) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        userIds.remove(String.valueOf(userInfo.getUid()));
                        names.remove(userInfo.getUsername());
                    } else {
                        checkBox.setChecked(true);
                        userIds.add(String.valueOf(userInfo.getUid()));
                        names.add(userInfo.getUsername());
                    }
                }
            }
        });
    }

    /**
     * 获取好友信息
     */
    private void getFriends() {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 0);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", String.valueOf(myApp.personalInfo.getUid()));
        http.send(HttpRequest.HttpMethod.GET, GlobalEntity.GET_FRIENDSLISTS_APIS, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JsonObject result = GsonUtils.fromJson(stringResponseInfo.result, JsonObject.class);
                    List<UserInfo> mListData = GsonUtils.fromJson(result.get("result").toString(), new TypeToken<List<UserInfo>>() {
                    }.getType());
                    if (mListData == null || mListData.size() == 0) {
                        mEmptyView.setText("暂无好友");
                    } else {
                        mPleasesAdapter.replaceAll(mListData);
                    }
                    mEmptyView.setVisibility(View.GONE);
                } catch (JsonParseException e) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mEmptyView.setText("暂无好友");
                    UIHelper.ToastMessage(CreatDisscussActivity.this, "访问出错！");
                }
                onComplete();
            }

            @Override
            public void onStart() {
                mEmptyView.setVisibility(View.VISIBLE);
                mEmptyView.setText("加载中~~");
            }

            @Override
            public void onFailure(HttpException e, String s) {
                UIHelper.ToastMessage(CreatDisscussActivity.this, "获取好友失败！");
                mEmptyView.setVisibility(View.VISIBLE);
                mEmptyView.setText("暂无好友");
                onComplete();
            }

            private void onComplete() {
                mListView.stopRefresh();
                mListView.stopLoadMore();
                mListView.setRefreshTime("刚刚");
            }
        });
    }

    /**
     * 把list转换为一个用逗号分隔的字符串
     */
    public static String listToString(List list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

}
