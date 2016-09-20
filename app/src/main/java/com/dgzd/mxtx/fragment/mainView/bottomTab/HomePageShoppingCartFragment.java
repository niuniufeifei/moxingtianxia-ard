package com.dgzd.mxtx.fragment.mainView.bottomTab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.view.ConfirmOrderActivity;
import com.dgzd.mxtx.adapter.ShoppingCommodityAdapter;
import com.dgzd.mxtx.entirety.ShoppingCardItemInfo;
import com.dgzd.mxtx.entirety.ShoppingCartInfo;
import com.dgzd.mxtx.entirety.ShoppingCartItemInfo;
import com.dgzd.mxtx.entirety.ShoppingCartItemListItemInfo;
import com.dgzd.mxtx.entirety.ShoppingCartOrderProductInfo;
import com.dgzd.mxtx.entirety.ShoppingCartStoreInfo;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomePageShoppingCartFragment extends Fragment {
    private View view;
    private ImageView ivBack;
    private ListView lvCommodity;
    private TextView tvProductsInstruction;
    private TextView tvTotalCost, tvToSettlement;
    private List<ShoppingCardItemInfo> itemList;
    private RequestQueue requestQueue;
    private final String requestTag = "HomePageShoppingCartFragment";
    private ShoppingCartInfo shoppingCartInfo;

    public HomePageShoppingCartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page_shopping_cart, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        initView();
        backClick();
        toSettlementClick();
        getShoppingCartData();
        return view;
    }

    private void initView() {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        lvCommodity = (ListView) view.findViewById(R.id.lv_commodity);
        tvProductsInstruction = (TextView) view.findViewById(R.id.tv_products_instruction);
        tvTotalCost = (TextView) view.findViewById(R.id.tv_total_cost);
        tvToSettlement = (TextView) view.findViewById(R.id.tv_to_settlement);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void toSettlementClick() {
        tvToSettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedCartItemLists", "4");
                intent.setClass(getActivity(), ConfirmOrderActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getShoppingCartData() {
        if (MxtxApplictaion.getInstance().personalInfo == null){
            return;
        }
        int uid = MxtxApplictaion.getInstance().personalInfo.getUid();
        String shopingCartUrl = String.format(GlobalEntity.GetShoppingCartUrl, uid);
        StringRequest listRequest = new StringRequest(shopingCartUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    shoppingCartInfo = new ShoppingCartInfo();
                    JSONObject jsonData = new JSONObject(s);
                    boolean isStautsNull = jsonData.isNull("status");
                    if (!isStautsNull) {
                        JSONObject resultJson = jsonData.getJSONObject("result");
                        int totalCount = resultJson.getInt("TotalCount");
                        int productAmount = resultJson.getInt("ProductAmount");
                        int fullCut = resultJson.getInt("FullCut");
                        int orderAmount = resultJson.getInt("OrderAmount");
                        shoppingCartInfo.setTotalCount(totalCount);
                        shoppingCartInfo.setProductAmount(productAmount);
                        shoppingCartInfo.setFullCut(fullCut);
                        shoppingCartInfo.setOrderAmount(orderAmount);
                        //get the data of StoreCartList
                        List<ShoppingCartItemInfo> shoppingCartItemInfoArray = new ArrayList<>();
                        JSONArray storeCratArray = resultJson.getJSONArray("StoreCartList");
                        for (int i = 0; i < storeCratArray.length(); ++i) {
                            ShoppingCartItemInfo shoppingCartItemInfo = new ShoppingCartItemInfo();
                            ShoppingCartStoreInfo storeInfo = new ShoppingCartStoreInfo();
                            JSONObject storeCartJson = storeCratArray.getJSONObject(i);
                            JSONObject storeInfoJson = storeCartJson.getJSONObject("StoreInfo");
                            storeInfo.setName(storeInfoJson.getString("Name"));
                            shoppingCartItemInfo.setStoreInfo(storeInfo);

                            List<ShoppingCartItemListItemInfo> itemListItemInfoArray = new ArrayList<>();
                            JSONArray cartItemListJson = storeCartJson.getJSONArray("CartItemList");
                            for (int j = 0; j < cartItemListJson.length(); ++j) {
                                ShoppingCartItemListItemInfo itemListItemInfo = new ShoppingCartItemListItemInfo();
                                JSONObject itemJson = cartItemListJson.getJSONObject(j).getJSONObject("Item");
                                itemListItemInfo.setSelected(itemJson.getBoolean("Selected"));

                                JSONObject orderProductInfoJson = itemJson.getJSONObject("OrderProductInfo");
                                ShoppingCartOrderProductInfo orderProductInfo = new ShoppingCartOrderProductInfo();
                                orderProductInfo.setName(orderProductInfoJson.getString("Name"));
                                orderProductInfo.setShowImg(orderProductInfoJson.getString("ShowImg"));
                                orderProductInfo.setShopPrice(orderProductInfoJson.getInt("ShopPrice"));
                                orderProductInfo.setAddTime(orderProductInfoJson.getString("AddTime"));
                                orderProductInfo.setBuyCount(orderProductInfoJson.getInt("BuyCount"));
                                orderProductInfo.setRealCount(orderProductInfoJson.getInt("RealCount"));
                                int recordId = orderProductInfoJson.getInt("RecordId");
                                orderProductInfo.setRecordId(recordId);
                                shoppingCartItemInfo.isCheckedMap.put(recordId, false);
                                itemListItemInfo.setOrderProductInfo(orderProductInfo);
                                itemListItemInfoArray.add(itemListItemInfo);
                            }
                            shoppingCartItemInfo.setItemListItemInfoArray(itemListItemInfoArray);

                            List<ShoppingCartOrderProductInfo> selectedOrderProductArray = new ArrayList<>();
                            JSONArray selectedOrderProductListInfo = storeCartJson.getJSONArray("SelectedOrderProductList");
                            for (int m = 0; m < selectedOrderProductListInfo.length(); ++m) {
                                ShoppingCartOrderProductInfo selectedOrderProductInfo = new ShoppingCartOrderProductInfo();
                                JSONObject orderProductInfoJson = selectedOrderProductListInfo.getJSONObject(m);
                                selectedOrderProductInfo.setName(orderProductInfoJson.getString("Name"));
                                selectedOrderProductInfo.setShowImg(orderProductInfoJson.getString("ShowImg"));
                                selectedOrderProductInfo.setShopPrice(orderProductInfoJson.getInt("ShopPrice"));
                                selectedOrderProductInfo.setAddTime(orderProductInfoJson.getString("AddTime"));
                                selectedOrderProductArray.add(selectedOrderProductInfo);
                            }
                            shoppingCartItemInfo.setSelectedOrderProductArray(selectedOrderProductArray);
                            shoppingCartItemInfoArray.add(shoppingCartItemInfo);
                        }
                        shoppingCartInfo.setShoppingCartItemInfoArray(shoppingCartItemInfoArray);

                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = shoppingCartInfo;
                        myHandler.sendMessage(msg);

                    } else {
                        Message msg = new Message();
                        msg.what = 2;
                        myHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 2;
                    myHandler.sendMessage(msg);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 2;
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
                    ShoppingCartInfo listInfo = (ShoppingCartInfo) msg.obj;
                    if (listInfo != null) {
                        int cost = listInfo.getProductAmount();
                        tvTotalCost.setText(String.format("￥%d", cost));
                        setListData(listInfo);
                    }
                    break;
                }
                case 2: {
                    UIHelper.ToastMessage(getActivity(), "获取购物车信息失败！");
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void setListData(ShoppingCartInfo listData) {
        ShoppingCommodityAdapter adapter = new ShoppingCommodityAdapter(getContext(), listData);
        lvCommodity.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
