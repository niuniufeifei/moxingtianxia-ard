package com.dgzd.mxtx.activity.mineView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class EditCellPhoneActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvSave;
    private TextView tvSendMsgTip;
    private EditText etMobile;
    private EditText etVerification;
    private Button btnGetVerification;
    private RequestQueue requestQueue;
    private String requestTag = "EditCellPhoneActivity";
    private MxtxApplictaion myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_cell_phone);
        requestQueue = Volley.newRequestQueue(this);
        myApp = MxtxApplictaion.getInstance();
        initView();
        backClick();
        getVerificationClick();
        saveClick();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvSendMsgTip = (TextView) findViewById(R.id.tv_send_msg_tip);
        tvSave = (TextView) findViewById(R.id.tv_save);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etVerification = (EditText) findViewById(R.id.et_verification);
        btnGetVerification = (Button) findViewById(R.id.btn_get_verification);
        String tip = String.format(getString(R.string.cell_phone_notice), myApp.personalInfo.getMobile());
        tvSendMsgTip.setText(tip);
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

    private void getVerificationClick() {
        btnGetVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postSendMessage();
            }
        });
    }

    private boolean checkPhone() {
        if (etVerification.getText() == null
                || "".equals(etVerification.getText().toString())) {
            UIHelper.ToastMessage(EditCellPhoneActivity.this, getString(R.string.verification_code_tip));
            return false;
        } else if (etMobile.getText() == null
                || "".equals(etMobile.getText().toString())) {
            UIHelper.ToastMessage(EditCellPhoneActivity.this, getString(R.string.cell_phone_number_tip));
            return false;
        }

        if (!etMobile.getText().toString().matches("^[0-9]{11}$")) {
            UIHelper.ToastMessage(EditCellPhoneActivity.this, getString(R.string.cell_phone_number_correct_tip));
            return false;
        }
        return true;
    }

    private void postSendMessage() {
        Map<String, String> map = new HashMap<String, String>();
        String strMobile = myApp.personalInfo.getMobile();
        map.put("mobile", strMobile);
        map.put("SendStatus", "3");

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.SenMessageUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String status = jsonObject.getString("status").toString();
                    if (status.equals("true")) {
                        String result = jsonObject.getString("result").toString();
                        Message msg = new Message();
                        msg.what = 3;
                        msg.obj = result;
                        myHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 4;
                        String strError = jsonObject.getString("message");
                        msg.obj = strError;
                        myHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 5;
                    myHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 5;
                myHandler.sendMessage(msg);
            }
        }, map);

        request.setTag(requestTag);
        requestQueue.add(request);
    }

    private void modifyUserInfo() {

        Map<String, String> map = new HashMap<String, String>();
        String nickName = myApp.personalInfo.getNickName();
        String address = myApp.personalInfo.getAddress();
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
        map.put("nickname", nickName);
        map.put("regionid", strRegionId);
        if((address != null))
            map.put("address", address);
        else
            map.put("address", "");
        map.put("uid", strUserId);

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.ModifyUserInfoUrl, new Response.Listener<JSONObject>() {
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
                    UIHelper.ToastMessage(EditCellPhoneActivity.this, "修改成功");
                    Intent intent = new Intent();
                    intent.putExtra("cellPhone", etMobile.getText().toString().trim());
                    setResult(GlobalEntity.MODIFY_USER_INFO_SUCCESS, intent);
                    finish();
                    break;
                }
                case 2: {
                    UIHelper.ToastMessage(EditCellPhoneActivity.this, "修改失败");
                    setResult(GlobalEntity.MODIFY_USER_INFO_FAILED);
                    finish();
                    break;
                }
                case 3: {
                    UIHelper.ToastMessage(EditCellPhoneActivity.this, "短信已发送");
                    break;
                }
                case 4: {
                    String message = (String) msg.obj;
                    UIHelper.ToastMessage(EditCellPhoneActivity.this, message);
                    break;
                }
                case 5: {
                    UIHelper.ToastMessage(EditCellPhoneActivity.this, getString(R.string.get_verification_faild));
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
