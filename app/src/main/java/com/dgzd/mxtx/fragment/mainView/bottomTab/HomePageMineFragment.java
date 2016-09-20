package com.dgzd.mxtx.fragment.mainView.bottomTab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.login.LoginActivity;
import com.dgzd.mxtx.activity.mineView.MinePersonalActivity;
import com.dgzd.mxtx.activity.mineView.shopOrder.ShopResellActivity;
import com.dgzd.mxtx.activity.mineView.userEvent.EventActivity;
import com.dgzd.mxtx.activity.mineView.userOrder.UserOrderActivity;
import com.dgzd.mxtx.activity.mineView.userVideo.UserVideoActivity;
import com.dgzd.mxtx.custom.CircleImageView;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomePageMineFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tvCompleteModify;
    private CircleImageView ivPhoto;
    private TextView tvUserName;
    private RelativeLayout layoutMyOrder;
    private RelativeLayout layoutMyVideo;
    private RelativeLayout layoutMyEvent;
    private RelativeLayout layoutMyResell;
    private RelativeLayout layoutLogout;

    private MxtxApplictaion myApp;
//    private RequestQueue requestQueue;
//    private final String requestTag = "HomePageMineFragment";

    public HomePageMineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page_mine, container, false);
        myApp = MxtxApplictaion.getInstance();
//        requestQueue = Volley.newRequestQueue(getActivity());
        initView();
        completeCModifyClick();
        return view;
    }

    private void initView() {
        ivPhoto = (CircleImageView) view.findViewById(R.id.iv_photo);
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        tvCompleteModify = (TextView) view.findViewById(R.id.tv_complete_modify);
        layoutMyOrder = (RelativeLayout) view.findViewById(R.id.layout_my_order);
        layoutMyVideo = (RelativeLayout) view.findViewById(R.id.layout_my_video);
        layoutMyEvent = (RelativeLayout) view.findViewById(R.id.layout_my_event);
        layoutMyResell = (RelativeLayout) view.findViewById(R.id.layout_my_resell);
        layoutLogout = (RelativeLayout) view.findViewById(R.id.layout_logout);

        layoutMyOrder.setOnClickListener(this);
        layoutMyVideo.setOnClickListener(this);
        layoutMyEvent.setOnClickListener(this);
        layoutMyResell.setOnClickListener(this);
        layoutLogout.setOnClickListener(this);

        setPersonalInfo();
    }

    private void setPersonalInfo() {
        if (myApp.personalInfo != null) {
            String userName = myApp.personalInfo.getUserName();
            if (userName != null) {
                if (userName.length() > 0) {
                    tvUserName.setText(userName);
                }
            }
            String photoUrl = myApp.personalInfo.getAvatar();
            if (photoUrl != null) {
                if (photoUrl.length() > 0) {
                    Picasso.with(getContext()).load(photoUrl).into(ivPhoto);
                }
            }
        }
    }

    private void completeCModifyClick() {
        tvCompleteModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MinePersonalActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_my_order: {
                startActivity(new Intent(getActivity(), UserOrderActivity.class));
                break;
            }
            case R.id.layout_my_video: {
                startActivity(new Intent(getActivity(), UserVideoActivity.class));
                break;
            }
            case R.id.layout_my_event: {
                startActivity(new Intent(getActivity(), EventActivity.class));
                break;
            }
            case R.id.layout_my_resell: {
                startActivity(new Intent(getActivity(), ShopResellActivity.class));
                break;
            }
            case R.id.layout_logout: {

                logoutConfirm();
                break;
            }
            default: {
                break;
            }

        }
    }

    private void logoutConfirm() {
        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.logout_tip)).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击“确认”后的操作
                MxtxApplictaion.getInstance().logout();
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), GlobalEntity.REQUEST_LOGIN);
            }
        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GlobalEntity.REQUEST_LOGIN: {
                if (resultCode != -1)
                    setPersonalInfo();
                break;
            }
            case GlobalEntity.REQUEST_LOGIN_FAILED: {
                MxtxApplictaion.getInstance().logout();
                break;
            }
            default:
                break;
        }
    }

    private void logout() {
//        Map<String, String> map = new HashMap<String, String>();
//
//        map.put("UserName", strUserId);
//        map.put("Password", strVids);
//        map.put("ConfirmPwd", strVids);
//        map.put("Mobile", strVids);
//
//        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.VedioDeleteUrl, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                Message msg = new Message();
//                msg.what = 4;
//                myHandler.sendMessage(msg);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        }, map);
//
//        request.setTag(requestTag);
//        requestQueue.add(request);
    }

//    private Handler myHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1: {
//                    break;
//                }
//                default:
//                    break;
//            }
//        }
//    };

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        requestQueue.cancelAll(requestTag);
//    }
}
