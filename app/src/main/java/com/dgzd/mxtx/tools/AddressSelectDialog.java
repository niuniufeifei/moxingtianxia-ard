package com.dgzd.mxtx.tools;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.entirety.AddressSelectInfo;
import com.dgzd.mxtx.wheelWidget.ArrayWheelAdapter;
import com.dgzd.mxtx.wheelWidget.OnWheelChangedListener;
import com.dgzd.mxtx.wheelWidget.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @version V1.0 <功能>
 * @FileName: AddressSelectDialog.java
 * @author: Jessica
 */

public class AddressSelectDialog extends Dialog {
    public interface OnAddressSelectDialogListener {
        public void back(String name);
    }

    private Context context;
    private String title;
    private OnAddressSelectDialogListener listener;
    private TextView tvTitle;
    private Button btnCancel, btnConfirm;
    private WheelView wheelLeft, wheelMiddle, wheelRight;
    private List<AddressSelectInfo> provinceInfoArray, cityInfoArray, areaInfoArray;
    private RequestQueue queue;
    private String requestTag = "AddressSelectDialog";


    public AddressSelectDialog(Context context, String title, OnAddressSelectDialogListener listener) {
        super(context);
        this.context = context;
        this.title = title;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_select_dialog);
        queue = Volley.newRequestQueue(context);
        initView();
        initWheelView();
        cancelClick();
        confirmClick();
        leftWheelChanging();
        middleWheelChanging();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        tvTitle.setText(title);

        wheelLeft = (WheelView) findViewById(R.id.wheelview_left);
        wheelMiddle = (WheelView) findViewById(R.id.wheelview_middle);
        wheelRight = (WheelView) findViewById(R.id.wheelview_right);
    }
//
//    public void setProvinceData(List<AddressSelectInfo> provinceInfoArray) {
//        this.provinceInfoArray = provinceInfoArray;
//    }
//
//    public void setCityData(List<AddressSelectInfo> cityInfoArray) {
//        this.cityInfoArray = cityInfoArray;
//    }
//
//    public void setAreaData(List<AddressSelectInfo> areaInfoArray) {
//        this.areaInfoArray = areaInfoArray;
//    }

    private void initWheelView() {
        //设置左侧的 WheelView
        wheelLeft.setVisibleItems(5);//设置中侧 WheelView 显示个数
        wheelLeft.setCyclic(false);//设置中侧 WheelView 元素是否循环滚动


        //设置中侧的 WheelView
        wheelMiddle.setVisibleItems(5);
        wheelMiddle.setCyclic(false);

        //设置右侧的 WheelView
        wheelRight.setVisibleItems(5);
        wheelRight.setCyclic(false);
        getProvinceData();
    }

    private void cancelClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void confirmClick() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void leftWheelChanging() {
        //为左侧的 WheelView 设置条目改变监听器
        wheelLeft.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int provinceId = provinceInfoArray.get(newValue).getProvinceId();
                getCityData(provinceId);
            }
        });
    }

    private void middleWheelChanging() {
        wheelMiddle.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (newValue < cityInfoArray.size()) {
                    int cityId = cityInfoArray.get(newValue).getCityId();
                    getAreaData(cityId);
                }
            }
        });
    }

    public void setWheelcityInfo(List<AddressSelectInfo> cityInfoArray) {
        //设置右侧的 WheelView 的适配器
        wheelMiddle.setAdapter(new ArrayWheelAdapter<List<AddressSelectInfo>>(cityInfoArray));
        if (cityInfoArray.size() > 0) {
            int idx = 0;
            if (cityInfoArray.size() > 2) {
                idx = cityInfoArray.size() / 2;
            }
            int count = wheelMiddle.getVisibleItems();
            wheelMiddle.setCurrentItem(idx);
        }
    }

    public void setWheelAreaInfo(List<AddressSelectInfo> areaInfoArray) {

        //设置右侧的 WheelView 的适配器
        wheelRight.setAdapter(new ArrayWheelAdapter<List<AddressSelectInfo>>(areaInfoArray));
        if (areaInfoArray.size() > 0) {
            int idx = 0;
            if (areaInfoArray.size() > 2) {
                idx = areaInfoArray.size() / 2;
            }
            wheelRight.setCurrentItem(idx);
        }
    }

    private void getProvinceData() {
        StringRequest request = new StringRequest(GlobalEntity.getProvinceUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    provinceInfoArray = new ArrayList<>();
                    JSONObject dataJson = new JSONObject(s);
                    String strResult = dataJson.getString("result");
                    JSONArray dataArray = new JSONArray(strResult);
                    for (int i = 0; i < dataArray.length(); ++i) {
                        AddressSelectInfo addressSelectInfo = new AddressSelectInfo();
                        JSONObject resultJson = dataArray.getJSONObject(i);
                        String provinceName = resultJson.getString("Name");
                        int provinceId = resultJson.getInt("RegionId");
                        addressSelectInfo.setName(provinceName);
//                        addressSelectInfo.setProvinceName(provinceName);
                        addressSelectInfo.setProvinceId(provinceId);
                        provinceInfoArray.add(addressSelectInfo);
                    }

                    Message message = new Message();
                    message.what = 1;
                    message.obj = provinceInfoArray;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag(requestTag);
        queue.add(request);
    }

    private void getCityData(int provinceId) {
        final String cityUrl = String.format(GlobalEntity.getCityUrl, provinceId);
        StringRequest request = new StringRequest(cityUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    cityInfoArray = new ArrayList<>();
                    JSONObject dataJson = new JSONObject(s);
                    String strResult = dataJson.getString("result");
                    JSONArray dataArray = new JSONArray(strResult);
                    for (int i = 0; i < dataArray.length(); ++i) {
                        AddressSelectInfo cityInfo = new AddressSelectInfo();
                        JSONObject resultJson = dataArray.getJSONObject(i);
                        String cityName = resultJson.getString("Name");
                        int cityId = resultJson.getInt("RegionId");
                        cityInfo.setName(cityName);
//                        cityInfo.setCityName(cityName);
                        cityInfo.setCityId(cityId);
                        cityInfoArray.add(cityInfo);
                    }

                    Message message = new Message();
                    message.what = 2;
                    message.obj = cityInfoArray;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag(requestTag);
        queue.add(request);
    }

    private void getAreaData(int cityId) {
        String areaUrl = String.format(GlobalEntity.getAreaUrl, cityId);
        StringRequest request = new StringRequest(areaUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    areaInfoArray = new ArrayList<>();
                    JSONObject dataJson = new JSONObject(s);
                    String strResult = dataJson.getString("result");
                    JSONArray dataArray = new JSONArray(strResult);
                    for (int i = 0; i < dataArray.length(); ++i) {
                        AddressSelectInfo cityInfo = new AddressSelectInfo();
                        JSONObject resultJson = dataArray.getJSONObject(i);
                        String areaName = resultJson.getString("Name");
                        int areaId = resultJson.getInt("RegionId");
                        cityInfo.setName(areaName);
//                        cityInfo.setCityName(cityName);
                        cityInfo.setCityId(areaId);
                        areaInfoArray.add(cityInfo);
                    }

                    Message message = new Message();
                    message.what = 3;
                    message.obj = areaInfoArray;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag(requestTag);
        queue.add(request);
    }

    private android.os.Handler handler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    List<AddressSelectInfo> provinceInfoArray = (List<AddressSelectInfo>) msg.obj;
                    wheelLeft.setAdapter(new ArrayWheelAdapter<List<AddressSelectInfo>>(provinceInfoArray));
                    break;
                }
                case 2: {
                    List<AddressSelectInfo> cityInfoArray = (List<AddressSelectInfo>) msg.obj;

                    setWheelcityInfo(cityInfoArray);
                    break;
                }
                case 3: {
                    List<AddressSelectInfo> areaInfoArray = (List<AddressSelectInfo>) msg.obj;
                    setWheelAreaInfo(areaInfoArray);

                    break;
                }
                default:
                    break;
            }
        }
    };
}
