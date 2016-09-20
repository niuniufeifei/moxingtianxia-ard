package com.dgzd.mxtx.fragment.mainView.bottomTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.view.ConfirmOrderActivity;
import com.dgzd.mxtx.adapter.ListViewShopCarAdapter;
import com.dgzd.mxtx.entirety.ShopCarItem;
import com.dgzd.mxtx.entirety.ShopCarResult;
import com.dgzd.mxtx.entirety.ShoppingCardItemInfo;
import com.dgzd.mxtx.entirety.ShoppingCartInfo;
import com.dgzd.mxtx.rong.model.UserInfo;
import com.dgzd.mxtx.rong.utils.GsonUtils;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.utils.UIHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nsd on 2016/3/4.
 * Notes：
 */
public class HomePageShopCarFragment extends Fragment{
    private View view;
    private ImageView ivBack;
    private ExpandableListView mShopCarListView;
    private TextView tvProductsInstruction;
    private TextView tvTotalCost, tvToSettlement;
    private List<ShoppingCardItemInfo> itemList;
    private RequestQueue requestQueue;
    private final String requestTag = "HomePageShoppingCartFragment";
    private ShoppingCartInfo shoppingCartInfo;
    ListViewShopCarAdapter mFriendsAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_shopcar,container,false);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        mShopCarListView = (ExpandableListView) view.findViewById(R.id.listview);
        tvProductsInstruction = (TextView) view.findViewById(R.id.tv_products_instruction);
        tvTotalCost = (TextView) view.findViewById(R.id.tv_total_cost);
        tvToSettlement = (TextView) view.findViewById(R.id.tv_to_settlement);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        getShoppingCartData();
    }
    private void initView() {
        mFriendsAdapter = new ListViewShopCarAdapter(getActivity());
        tvToSettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedCartItemLists", "4");
                intent.setClass(getActivity(), ConfirmOrderActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mShopCarListView.setGroupIndicator(null);
        mShopCarListView.setAdapter(mFriendsAdapter);
        mShopCarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        mShopCarListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
               /* Schedule schedule = (Schedule) mFriendsAdapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("childId", schedule.getId());
                intent.putExtra("headImg", headImg);
                intent.putExtra("userName", username);
                intent.putExtra("userSex", userSex);
                intent.putExtra("userBirth", userBirth);
                startActivity(intent);*/
                return false;
            }
        });
    }

    private void backClick() {

    }
    /**
     * 获取token
     */
    private void getShoppingCartData() {
        if (MxtxApplictaion.getInstance().personalInfo == null){
            return;
        }
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 0);
        RequestParams params = new RequestParams();
        int uid = MxtxApplictaion.getInstance().personalInfo.getUid();
        String shopingCartUrl = String.format(GlobalEntity.GetShoppingCartUrl, uid);
        http.send(HttpRequest.HttpMethod.GET, shopingCartUrl, new RequestCallBack<String>() {
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JsonObject result = GsonUtils.fromJson(stringResponseInfo.result, JsonObject.class);
                    String status = GsonUtils.getJsonValue(result.toString(),"status").toString();
                    if (status!=null){
                        if (status.equals("true")){
                            ShopCarResult carResult = GsonUtils.fromJson(result.get("result").toString(), ShopCarResult.class);
                            List<ShopCarItem> carItems = new ArrayList<ShopCarItem>();
                            carItems = carResult.getStoreCartList();
                            if (carItems.size() == 0 || carItems == null) {
                                UIHelper.ToastMessage(getActivity(),"暂无数据！");
                            }else{
                                mFriendsAdapter.setData(carItems);
                                mFriendsAdapter.notifyDataSetChanged();
                                for(int i = 0; i < mFriendsAdapter.getGroupCount(); i++){
                                    mShopCarListView.expandGroup(i);
                                }
                            }
                        }else {
                            String message = GsonUtils.getJsonValue(result.toString(),"message").toString();
                            UIHelper.ToastMessage(getActivity(),message);
                        }
                    }
                } catch (JsonParseException e) {
                }
            }

            public void onStart() {
            }

            public void onFailure(HttpException e, String s) {
                UIHelper.ToastMessage(getActivity(),"网络错误！");
            }

            private void onComplete() {

            }
        });
    }
}
