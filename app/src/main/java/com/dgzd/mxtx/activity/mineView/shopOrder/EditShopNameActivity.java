package com.dgzd.mxtx.activity.mineView.shopOrder;

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
 * @FileName: EditShopNameActivity.java
 * @author: Jessica
 * @date: 2015-12-28 14:43
 */

public class EditShopNameActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvSave;
    private EditText etContent;
    private RequestQueue requestQueue;
    private String requestTag = "EditShopNameActivity";
    private MxtxApplictaion myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_name);
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
        etContent.setText(MxtxApplictaion.getInstance().personalInfo.getStoreName());
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
                modifyStoreName();
            }
        });
    }

    private void modifyStoreName() {
        Map<String, String> map = new HashMap<String, String>();
        String shopName = etContent.getText().toString().trim();
        int userId = myApp.personalInfo.getUid();
        int storeId = myApp.personalInfo.getStoreId();
        String strUserId = String.valueOf(userId);
        String strStoreId = String.valueOf(storeId);
        try {
            if (shopName != null && shopName.length() > 0) {
                shopName = URLEncoder.encode(shopName, "UTF-8");
            }
            if (strUserId != null && strUserId.length() > 0) {
                strUserId = URLEncoder.encode(strUserId, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("name", shopName);
        map.put("storeid", strStoreId);
        map.put("uid", strUserId);

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.ModifyStoreNameUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String result = "";
                try {
                    result = jsonObject.getString("result");
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
                    String storeName = etContent.getText().toString().trim();
                    MxtxApplictaion.getInstance().personalInfo.setStoreName(storeName);
                    UIHelper.ToastMessage(EditShopNameActivity.this, "修改成功");
                    Intent intent = new Intent();
                    intent.putExtra("shopName", etContent.getText().toString().trim());
                    setResult(GlobalEntity.MODIFY_USER_INFO_SUCCESS, intent);
                    finish();
                    break;
                }
                case 2: {
                    UIHelper.ToastMessage(EditShopNameActivity.this, "修改失败");
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
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
