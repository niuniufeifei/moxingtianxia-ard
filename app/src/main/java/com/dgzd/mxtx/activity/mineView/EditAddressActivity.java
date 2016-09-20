package com.dgzd.mxtx.activity.mineView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.tools.NormalPostRequest;
import com.dgzd.mxtx.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0 <功能>
 * @FileName: EditNicknameActivity.java
 * @author: Jessica
 * @date: 2015-12-22 14:43
 */

public class EditAddressActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvSave;
    private EditText etContent;
    private RequestQueue requestQueue;
    private String requestTag = "EditNicknameActivity";
    private MxtxApplictaion myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_address);
        requestQueue = Volley.newRequestQueue(this);
        myApp = MxtxApplictaion.getInstance();
        initView();
        backClick();
        saveClick();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvSave = (TextView) findViewById(R.id.tv_save);
        etContent = (EditText) findViewById(R.id.et_content);

        String nickName = MxtxApplictaion.getInstance().personalInfo.getNickName();
        etContent.setText(nickName);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveClick() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyUserInfo();
            }
        });
    }

    private void modifyUserInfo() {

        Map<String, String> map = new HashMap<String, String>();
        String nickName = myApp.personalInfo.getNickName();
        String address = etContent.getText().toString().trim();
        int userId = myApp.personalInfo.getUid();
        int regionId = myApp.personalInfo.getRegionid();
        String strUserId = String.valueOf(userId);
        String strRegionId = String.valueOf(regionId);
        try {
            if (nickName != null && nickName.length() > 0) {
                nickName = URLEncoder.encode(nickName, "UTF-8");
            }

            if (address != null && address.length() > 0) {
                address = URLEncoder.encode(address, "UTF-8");
            }

            if (strUserId != null && strUserId.length() > 0) {
                strUserId = URLEncoder.encode(strUserId, "UTF-8");
            }
            if (strRegionId != null && strRegionId.length() > 0) {
                strRegionId = URLEncoder.encode(strRegionId, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        map.put("nickname", nickName);
//        map.put("regionid", strRegionId);
//        map.put("address", address);
//        map.put("uid", strUserId);

        map.put("nickname", nickName);
        map.put("regionid", "4");
        map.put("address", "3");
        map.put("uid", strUserId);

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.ModifyUserInfoUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String result = "";
                try {
                    result = jsonObject.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 2;
                    myHandler.sendMessage(msg);
                }
                Message msg = new Message();
                msg.what = 1;
                myHandler.sendMessage(msg);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 2;
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
                    UIHelper.ToastMessage(EditAddressActivity.this, "修改成功");
                    Intent intent = new Intent();
                    intent.putExtra("address", etContent.getText().toString().trim());
                    setResult(GlobalEntity.MODIFY_USER_INFO_SUCCESS, intent);
                    finish();
                    break;
                }
                case 2: {
                    UIHelper.ToastMessage(EditAddressActivity.this, "修改失败");
                    setResult(GlobalEntity.MODIFY_USER_INFO_FAILED);
                    finish();
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
