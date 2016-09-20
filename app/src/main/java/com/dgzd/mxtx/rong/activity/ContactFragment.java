package com.dgzd.mxtx.rong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;
/**
 * Created by nsd on 16/2/22.
 * note:联系人
 */
public class ContactFragment extends Fragment {
    public static ContactFragment intance = null;
    public static ContactFragment getIntance(){
        if (intance==null){
            intance = new ContactFragment();
        }
        return  intance;
    }
    private MxtxApplictaion myApp;
    QuickAdapter<UserInfo> mPleasesAdapter;
    private TextView mEmptyView;
    private XListView mListView;
    DbUtils db;
    BitmapUtils bitmapUtil;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_contact,container,false);
        mEmptyView = (TextView) view.findViewById(R.id.empty_view);
        mListView = (XListView) view.findViewById(R.id.listview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myApp = MxtxApplictaion.getInstance();
        db = DbUtils.create(getActivity());
        bitmapUtil=new BitmapUtils(getActivity());
        initView();
        getFriends();
    }
    private void initView() {
        mListView.setEmptyView(mEmptyView);
        mListView.setPullLoadEnable(false);
        mPleasesAdapter = new QuickAdapter<UserInfo>(getActivity(), R.layout.listitem_friend) {
            protected void convert(BaseAdapterHelper helper, final UserInfo item) {
                helper.setText(R.id.username,item.getUsername());
                ImageView view = helper.getView(R.id.user_head);
                if (item.getAvatar()!=null) {
                    bitmapUtil.display(view, item.getAvatar());
                }
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
                UserInfo personalInfo = mPleasesAdapter.getItem(position - 1);
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("username", personalInfo.getUsername());
                intent.putExtra("userhead", personalInfo.getAvatar());
                intent.putExtra("userid", String.valueOf(personalInfo.getUid()));
                intent.putExtra("type", "list");
                startActivity(intent);
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
        params.addQueryStringParameter("userId",String.valueOf(myApp.personalInfo.getUid()));
        http.send(HttpRequest.HttpMethod.GET, GlobalEntity.GET_FRIENDSLISTS_APIS,params, new RequestCallBack<String>() {
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
                        for (UserInfo user :mListData){
                            user.setId(user.getUid());
                            try {
                                db.save(user);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mEmptyView.setVisibility(View.GONE);
                } catch (JsonParseException e) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mEmptyView.setText("暂无好友");
                    UIHelper.ToastMessage(getActivity(),"访问出错！");
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
                UIHelper.ToastMessage(getActivity(),"获取好友失败！");
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
}
