package com.dgzd.mxtx.activity.mineView.userEvent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.mineView.userVideo.SelectPicPopupWindow;
import com.dgzd.mxtx.activity.view.ImagePreView;
import com.dgzd.mxtx.entirety.AddressSelectInfo;
import com.dgzd.mxtx.tools.AddressSelectDialog;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.dgzd.mxtx.tools.NormalPostRequest;
import com.dgzd.mxtx.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.utils.FileUtils;

/**
 * @version V1.0 <个人信息>
 * @FileName: EventParticipantInfoActivity.java
 * @author: Jessica
 */

public class EventParticipantInfoActivity extends AppCompatActivity {
    private ImageView ivBack;
    private ImageView ivFrontSide, ivReverseSide;
    private ImageView ivFrontShow, ivReverseShow;
    private Button btnCancel, btnNext;
    private TextView tvAddress;
    private EditText etUserName, etCellPhone, etIdentityNumber;
    private String strUserName, strCellPhone, strAddress, strIdentityNumber;

    private int maxNum = 1;
    private ArrayList<String> selectPathArray;
    private String frontImagePath = "", reverseImagePath = "";
    private final int REQUEST_CAMERA = 100;
    private File mTmpFile = null;

    private RequestQueue queue;
    private MxtxApplictaion myApp;
    private int activityId;
    private String requestTag = "EventParticipantInfoActivity";
    private List<AddressSelectInfo> leftAddressSelectInfoArray, middleAddressSelectInfoArray, rightAddressSelectInfoArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_info);
        myApp = MxtxApplictaion.getInstance();
        queue = Volley.newRequestQueue(this);

        getBundleData();
        initView();
        backClick();
        setFrontSideClick();
        setReverseSideClick();
        frontImageClick();
        reverseImageClick();
        cancelClick();
        nextClick();
        addressClick();
    }

    private void getBundleData() {
        Intent intent = getIntent();
        activityId = intent.getIntExtra(GlobalEntity.MOTO_TRIP_ACTIVITY_ID, 2);
    }

    private void initView() {

        ivBack = (ImageView) findViewById(R.id.iv_back);
        etUserName = (EditText) findViewById(R.id.et_user_name);
        etCellPhone = (EditText) findViewById(R.id.et_cell_phone_num);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        etIdentityNumber = (EditText) findViewById(R.id.et_identity);
        ivFrontSide = (ImageView) findViewById(R.id.iv_front_side);
        ivFrontShow = (ImageView) findViewById(R.id.iv_front_show);
        ivReverseSide = (ImageView) findViewById(R.id.iv_reverse_side);
        ivReverseShow = (ImageView) findViewById(R.id.iv_reverse_show);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnNext = (Button) findViewById(R.id.btn_next);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cancelClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void nextClick() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    getWechatInfo();
                }
            }
        });
    }

    private void getUserInput() {
        strUserName = etUserName.getText().toString();
        strCellPhone = etCellPhone.getText().toString();
        strAddress = tvAddress.getText().toString();
        strIdentityNumber = etIdentityNumber.getText().toString();
    }

    private boolean check() {
        if (etUserName.getText() == null
                || "".equals(etUserName.getText().toString())) {
            UIHelper.ToastMessage(EventParticipantInfoActivity.this, getString(R.string.your_name_tip));
            return false;
        } else if (etCellPhone.getText() == null
                || "".equals(etCellPhone.getText().toString())) {
            UIHelper.ToastMessage(EventParticipantInfoActivity.this, getString(R.string.cell_phone_number_tip));
            return false;
        } else if (etIdentityNumber.getText() == null
                || "".equals(etIdentityNumber.getText().toString())) {
            UIHelper.ToastMessage(EventParticipantInfoActivity.this, getString(R.string.identity_card_number_tip));
            return false;
        }

        if (!etCellPhone.getText().toString().matches("^[0-9]{11}$")) {
            UIHelper.ToastMessage(EventParticipantInfoActivity.this, getString(R.string.cell_phone_number_correct_tip));
            return false;
        } else if (!etIdentityNumber.getText().toString()
                .matches("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])")) {
            UIHelper.ToastMessage(EventParticipantInfoActivity.this, getString(R.string.identity_card_number_correct_tip));
            return false;
        }

        return true;
    }

    private void addressClick() {
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressSelectDialog dialog = new AddressSelectDialog(EventParticipantInfoActivity.this, "请选择点地址", new AddressSelectDialog.OnAddressSelectDialogListener() {
                    @Override
                    public void back(String name) {
                        UIHelper.ToastMessage(EventParticipantInfoActivity.this, name);
                    }
                });
                dialog.show();
            }
        });
    }

    private void postMsgToRegistration() {
        getUserInput();

        int userId = myApp.personalInfo.getUid();
        String strUserId = String.valueOf(userId);
        String strActivityId = String.valueOf(activityId);

        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", strUserId);
        map.put("activityid", strActivityId);
        map.put("realName", strUserName);
        map.put("idCard", strIdentityNumber);
        map.put("address", strUserId);
        map.put("regionid", strUserId);
        map.put("file", strUserId);

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.EventRegisterUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, map);
        request.setTag(requestTag);
        queue.add(request);
    }

    private void getWechatInfo() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("oid", "23");
        map.put("Body", "123");
        map.put("Subject", "456");
        map.put("userid", "2");

        Request<JSONObject> listRequest = new NormalPostRequest(GlobalEntity.WeixinPayUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("true")) {
                        myApp.setWechatInfo(jsonObject.getJSONObject("result"));
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("response ->" + error.toString());
            }
        }, map);
        listRequest.setTag(requestTag);
        queue.add(listRequest);

    }

    private android.os.Handler handler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    myApp.weixinPay();
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(EventParticipantInfoActivity.this.getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            try {
                mTmpFile = FileUtils.createTmpFile(EventParticipantInfoActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mTmpFile != null && mTmpFile.exists()) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            } else {
                Toast.makeText(EventParticipantInfoActivity.this, "图片错误", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(EventParticipantInfoActivity.this, me.nereo.multi_image_selector.R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocalPicture(boolean isFront) {
        int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        boolean showCamera = false;
        Intent intent = new Intent();
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
        if (selectPathArray != null && selectPathArray.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, selectPathArray);
        }

        if (isFront) {
            intent.putExtra(MultiImageSelectorActivity.IMAGE_SIDE, true);
        } else {
            intent.putExtra(MultiImageSelectorActivity.IMAGE_SIDE, false);
        }
        intent.setClass(EventParticipantInfoActivity.this, MultiImageSelectorActivity.class);
        startActivityForResult(intent, GlobalEntity.IDENTY_REQUESET_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    mTmpFile.getAbsolutePath();
                }
            } else {
                while (mTmpFile != null && mTmpFile.exists()) {
                    boolean success = mTmpFile.delete();
                    if (success) {
                        mTmpFile = null;
                    }
                }
            }
        } else if (requestCode == GlobalEntity.IDENTY_REQUESET_CODE) {
            if (resultCode == RESULT_OK) {
                selectPathArray = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for (String p : selectPathArray) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bm = BitmapFactory.decodeFile(p.toString(), options);

                    boolean isFront = data.getBooleanExtra(MultiImageSelectorActivity.IMAGE_SIDE, true);
                    if (isFront) {
                        ivFrontSide.setVisibility(View.GONE);
                        ivFrontShow.setVisibility(View.VISIBLE);
                        ivFrontShow.setImageBitmap(bm);
                        frontImagePath = p.toString();
                    } else {
                        ivReverseSide.setVisibility(View.GONE);
                        ivReverseShow.setVisibility(View.VISIBLE);
                        ivReverseShow.setImageBitmap(bm);
                        reverseImagePath = p.toString();
                    }
                }

            }
        } else if (requestCode == GlobalEntity.PREVIEW_DELETE) {
            if (data != null) {
                boolean result = data.getBooleanExtra(ImagePreView.IMAGE_SIDE, true);
                deletedImage(result);
            }
        } else {

            if (resultCode == GlobalEntity.POPWINDOW_TAKE_PHOTO) {
                UIHelper.ToastMessage(EventParticipantInfoActivity.this, "alipay");
                showCameraAction();
            } else if (resultCode == GlobalEntity.POPWINDOW_PICK_PHOTO) {
                UIHelper.ToastMessage(EventParticipantInfoActivity.this, "wechatpay");
                boolean isFront = data.getBooleanExtra(SelectPicPopupWindow.IMAGE_SIDE, true);
                getLocalPicture(isFront);
            } else if (resultCode == GlobalEntity.POPWINDOW_CANCEL) {

            }
        }
    }

    private void getImageBitmap() {
//        selectPathArray = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//        for (String p : selectPathArray) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;
//            Bitmap bm = BitmapFactory.decodeFile(p.toString(), options);
//
//            boolean isFront = data.getBooleanExtra(MultiImageSelectorActivity.IMAGE_SIDE, true);
//            if (isFront) {
//                ivFrontSide.setVisibility(View.GONE);
//                ivFrontShow.setVisibility(View.VISIBLE);
//                ivFrontShow.setImageBitmap(bm);
//                frontImagePath = p.toString();
//            } else {
//                ivReverseSide.setVisibility(View.GONE);
//                ivReverseShow.setVisibility(View.VISIBLE);
//                ivReverseShow.setImageBitmap(bm);
//                reverseImagePath = p.toString();
//            }
//        }
    }


    private void deletedImage(boolean isFront) {
        if (isFront) {
            ivFrontSide.setVisibility(View.VISIBLE);
            ivFrontShow.setVisibility(View.GONE);
            frontImagePath = "";
        } else {
            ivReverseSide.setVisibility(View.VISIBLE);
            ivReverseShow.setVisibility(View.GONE);
            reverseImagePath = "";
        }
    }

    private void setFrontSideClick() {
        ivFrontSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(SelectPicPopupWindow.IMAGE_SIDE, true);
                intent.setClass(EventParticipantInfoActivity.this, SelectPicPopupWindow.class);
                startActivityForResult(intent, GlobalEntity.POPWINDOW_PICK_PHOTO);
            }
        });
    }

    private void setReverseSideClick() {
        ivReverseSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(SelectPicPopupWindow.IMAGE_SIDE, false);
                intent.setClass(EventParticipantInfoActivity.this, SelectPicPopupWindow.class);
                startActivityForResult(intent, GlobalEntity.POPWINDOW_PICK_PHOTO);
            }
        });
    }

    private void frontImageClick() {
        ivFrontShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EventParticipantInfoActivity.this, ImagePreView.class);
                intent.putExtra(ImagePreView.IMAGE_PATH, frontImagePath);
                intent.putExtra(ImagePreView.IMAGE_SIDE, true);
                startActivityForResult(intent, GlobalEntity.PREVIEW_DELETE);
            }
        });
    }

    private void reverseImageClick() {
        ivReverseShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EventParticipantInfoActivity.this, ImagePreView.class);
                intent.putExtra(ImagePreView.IMAGE_PATH, reverseImagePath);
                intent.putExtra(ImagePreView.IMAGE_SIDE, false);
                startActivityForResult(intent, GlobalEntity.PREVIEW_DELETE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        queue.cancelAll(requestTag);
    }
}
