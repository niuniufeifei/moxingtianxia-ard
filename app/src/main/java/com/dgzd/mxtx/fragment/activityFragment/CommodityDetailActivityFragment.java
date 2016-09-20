package com.dgzd.mxtx.fragment.activityFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.view.CommodityInstructionActivity;
import com.dgzd.mxtx.activity.view.CommoditySelectPopup;
import com.dgzd.mxtx.adapter.CommonAdapter;
import com.dgzd.mxtx.custom.CommodityDetailSlideShowView;
import com.dgzd.mxtx.entirety.CommodityCommentInfo;
import com.dgzd.mxtx.entirety.CommodityDetailInfo;
import com.dgzd.mxtx.entirety.MotoTripRegisterInfo;
import com.dgzd.mxtx.entirety.ViewHolder;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.utils.UIHelper;
import com.dgzd.mxtx.widget.XScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommodityDetailActivityFragment extends Fragment implements XScrollView.IXScrollViewListener {

    private View view;
    private CommodityDetailSlideShowView slideShowView;
    private ImageView ivBack, right_btn;
    private TextView tvTitle, tvShopName;
    private TextView tvPurchaseNum, tvAssessmentNum, tvProductsName;
    private TextView tvProductsInstruction;
    private TextView tvAddShoppingCart, tvBuyNow;
    private RatingBar ratingbarStar;
    private CommoditySelectPopup selectPopupWindow;
    private CommodityDetailInfo commodityDetailInfo = new CommodityDetailInfo();
    private String requestTag = "CommodityDetailActivityFragment";
    private RequestQueue requestQueue;
    private int pid;

    private CommonAdapter adapter;
    private Handler mHandler;
    private int mRefreshIndex = 1;
    private XScrollView mScrollView;
    private ListView lvCommodityComments;

    private List<CommodityCommentInfo> commentList = new ArrayList<>();

    public CommodityDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity_detail_activity, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        getBundle();
        initView();
        getCommodityDetailData();
        backClick();
        productsInstructionClick();
        addShoppingCartClick();
        buyNowClick();
        return view;
    }

    private void getBundle() {
        Intent intent = getActivity().getIntent();
        pid = intent.getIntExtra("pid", 0);
    }

    private void initView() {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        right_btn = (ImageView) view.findViewById(R.id.right_btn);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvPurchaseNum = (TextView) view.findViewById(R.id.tv_purchaser_num);
        tvAssessmentNum = (TextView) view.findViewById(R.id.tv_assessment_num);
        tvProductsName = (TextView) view.findViewById(R.id.tv_products_name);
        ratingbarStar = (RatingBar) view.findViewById(R.id.ratingbar_star);
        tvShopName = (TextView) view.findViewById(R.id.tv_shop_name);
        tvProductsInstruction = (TextView) view.findViewById(R.id.tv_products_instruction);
        tvAddShoppingCart = (TextView) view.findViewById(R.id.tv_add_shopping_cart);
        tvBuyNow = (TextView) view.findViewById(R.id.tv_buy_now);
        slideShowView = (CommodityDetailSlideShowView) view.findViewById(R.id.slide_view);
        String url = String.format(GlobalEntity.commodityDetailUrl, pid);
        slideShowView.setUrl(url);

        mHandler = new Handler();

        mScrollView = (XScrollView) view.findViewById(R.id.scroll_commodity_comments);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(this);
        mScrollView.setRefreshTime(UIHelper.getTime());

        View content = LayoutInflater.from(getActivity()).inflate(R.layout.vw_scroll_view_content, null);
        if (null != content) {
            lvCommodityComments = (ListView) content.findViewById(R.id.content_list);
            lvCommodityComments.setSelector(R.drawable.trans);
            lvCommodityComments.setDividerHeight(1);
            lvCommodityComments.setFocusable(false);
            lvCommodityComments.setFocusableInTouchMode(false);
            getCommentData(GlobalEntity.PULL_UP);
        }

        mScrollView.setView(content);
        /**
         * 开启会话
         * 用户id","用户姓名"
         */
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.PRIVATE, "1", "mxtx1");
            }
        });
    }

    private void getCommentData(final int status) {
        String commentUrl = String.format(GlobalEntity.commodityCommentUrl, 5);
        StringRequest listRequest = new StringRequest(commentUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataJson = new JSONObject(response);
                    String strResult = dataJson.getString("result");
                    JSONArray commentArray = new JSONArray(strResult);
                    for (int i = 0; i < commentArray.length(); ++i) {
                        CommodityCommentInfo info = new CommodityCommentInfo();
                        info.setStrUserComment(commentArray.getJSONObject(i).getString("Comments"));
                        info.setStrPersonName(commentArray.getJSONObject(i).getString("PersonName"));
                        info.setStrPhotoUrl(commentArray.getJSONObject(i).getString("Avatar"));
                        info.setUserId(commentArray.getJSONObject(i).getInt("Userid"));
                        commentList.add(info);
                    }
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
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Message msg = new Message();
                msg.what = 4;
                myHandler.sendMessage(msg);
            }
        });
        listRequest.setTag(requestTag);
        requestQueue.add(listRequest);
    }

    private void getCommodityDetailData() {
        String url = String.format(GlobalEntity.commodityDetailUrl, pid);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject dataJson = new JSONObject(s);
                    JSONObject resultJson = dataJson.getJSONObject("result");
                    commodityDetailInfo.setShopName(resultJson.getString("ShopName"));
                    commodityDetailInfo.setProductName(resultJson.getString("ProductName"));
                    commodityDetailInfo.setSaleNumber(resultJson.getInt("SaleNumber"));
                    commodityDetailInfo.setCommentNum(resultJson.getInt("CommentNum"));
                    commodityDetailInfo.setCommentRate(resultJson.getInt("CommentRate"));

                    Message msg = new Message();
                    msg.what = 1;
                    myHandler.sendMessage(msg);
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
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    String strTitle = commodityDetailInfo.getProductName();
                    tvTitle.setText(strTitle);
                    String strBuyNum = getString(R.string.purchaser_number);
                    tvPurchaseNum.setText(String.format(strBuyNum, commodityDetailInfo.getBuyNumber()));
                    String strCommentNum = String.valueOf(commodityDetailInfo.getCommentNum());
                    tvAssessmentNum.setText(strCommentNum);
                    tvProductsName.setText(strTitle);
                    tvShopName.setText(commodityDetailInfo.getShopName());
                    int starCount = commodityDetailInfo.getCommentRate();
                    float rating = (float) (starCount / 2.0);
                    ratingbarStar.setRating(rating);
                    break;
                }
                case 2: {

                    break;
                }
                case 3: {
                    adapter = new CommonAdapter<CommodityCommentInfo>(getActivity(),
                            commentList, R.layout.moto_trip_register_item) {
                        @Override
                        public void convert(ViewHolder helper, final CommodityCommentInfo item, int position) {
                            helper.setSrcImageByUrl(R.id.iv_user_photo, item.getStrPhotoUrl());
                            helper.setText(R.id.tv_user_comment, item.getStrUserComment());
                            helper.setText(R.id.tv_user_name, item.getStrPersonName());
                        }
                    };
                    lvCommodityComments.setAdapter(adapter);
                    lvCommodityComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MotoTripRegisterInfo info = (MotoTripRegisterInfo) adapter.getItem(position);
                            if (info.getUserId() != MxtxApplictaion.getInstance().personalInfo.getUid()) {
//                                addFriends(String.valueOf(info.getUserId()), info.getUserName());
                            } else {
                                UIHelper.ToastMessage(getActivity(), "不能添加自己为好友哦！");
                            }
                        }
                    });
                    break;
                }
                case 4: {

                    break;
                }
                default:
                    break;
            }
        }
    };

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void productsInstructionClick() {
        tvProductsInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CommodityInstructionActivity.class));
            }
        });
    }

    private void addShoppingCartClick() {
        tvAddShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                startActivity(new Intent(getActivity(), CommoditySelectPopup.class));
            }
        });
    }

    private void buyNowClick() {
        tvBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                startActivity(new Intent(getActivity(), CommoditySelectPopup.class));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshIndex = 1;
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
        ListAdapter adapter = lvCommodityComments.getAdapter();
        if (null == adapter) {
            return 0;
        }

        int totalHeight = 0;

        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, lvCommodityComments);
            if (null == item) continue;
            // measure each item width and height
            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lvCommodityComments.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (lvCommodityComments.getDividerHeight() * (adapter.getCount() - 1));
        lvCommodityComments.setLayoutParams(params);
        return params.height;
    }
}
