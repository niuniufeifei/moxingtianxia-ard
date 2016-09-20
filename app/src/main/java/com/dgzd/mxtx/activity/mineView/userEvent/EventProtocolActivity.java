package com.dgzd.mxtx.activity.mineView.userEvent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @version V1.0 <功能>
 * @FileName: EventProtocolActivity.java
 * @author: Jessica
 */

public class EventProtocolActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvTitle, tvNext;
    private WebView wvDetail;
    private CheckBox cbAgreeProtocol;
    private RequestQueue requestQueue;
    private String requestTag = "EventProtocolActivity";
    private int activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_protocol);
        requestQueue = Volley.newRequestQueue(this);
        getBundleData();
        initView();
        backClick();
        nextClick();
        getProtocolData();
    }

    private void getBundleData() {
        Intent intent = getIntent();
        activityId = intent.getIntExtra(GlobalEntity.MOTO_TRIP_ACTIVITY_ID, 2);
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_protocol_title);
        wvDetail = (WebView) findViewById(R.id.wv_protocol);
        cbAgreeProtocol = (CheckBox) findViewById(R.id.cb_agree);
        tvNext = (TextView) findViewById(R.id.tv_next);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void nextClick() {
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAgreeProtocol.isChecked()) {
                    Intent intent = new Intent();
                    intent.putExtra(GlobalEntity.MOTO_TRIP_ACTIVITY_ID, activityId);
                    intent.setClass(EventProtocolActivity.this, EventParticipantInfoActivity.class);
                    startActivity(intent);
                } else {
                    UIHelper.ToastMessage(EventProtocolActivity.this, getString(R.string.agree_protocol_tip));
                }
            }
        });
    }

    private void getProtocolData() {
        String protocolUrl = String.format(GlobalEntity.ActivityProtocolUrl);
        StringRequest listRequest = new StringRequest(protocolUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject dataJson = new JSONObject(response);
                    String strResult = dataJson.getString("result");
                    JSONObject activityListObject = new JSONObject(strResult);
                    //获取活动协议
                    String strProtocolInfo = activityListObject.getString("ProtocolInfo");
                    if (strProtocolInfo.length() > 0) {
                        JSONObject activityInfoJson = new JSONObject(strProtocolInfo);
                        if (activityInfoJson.length() > 0) {
                            Bundle bundle = new Bundle();
                            String tmpTitle = activityInfoJson.getString("Title");
                            String tmpProtocol = activityInfoJson.getString("description");
                            bundle.putString("title", tmpTitle);
                            bundle.putString("protocol", tmpProtocol);

                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = bundle;
                            myHandler.sendMessage(msg);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
                    Bundle bundle = (Bundle) msg.obj;
                    String tmpTitle = bundle.getString("title");
                    String tmpProtocol = bundle.getString("protocol");
                    showProtocol(tmpTitle, tmpProtocol);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };

    private void showProtocol(String title, String protocol) {
        tvTitle.setText(title);
        wvDetail.loadDataWithBaseURL(null, protocol, "text/html", "utf-8", null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(requestTag);
    }
}
