package com.dgzd.mxtx.fragment.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dgzd.mxtx.custom.ImageEditView;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.NormalPostRequest;
import com.dgzd.mxtx.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterActivityFragment extends Fragment {
    private View view;
    private RequestQueue requestQueue;
    private final String requestTag = "RegisterActivityFragment";
    private String strMobile;
    private String strVerification;

    private ImageView ivBack;
    private ImageEditView iePhone, ieUserName, iePassword, ieConfirmPassword;
    private EditText etVerification;
    private Button btnRegister;
    private TextView tvServiceAgree, tvGetVerification;

    public RegisterActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());
        initView();
        backClick();
        verificationClick();
        registerClick();
        return view;
    }

    private void initView() {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        iePhone = (ImageEditView) view.findViewById(R.id.ie_phone);
        ieUserName = (ImageEditView) view.findViewById(R.id.ie_user_name);
        iePassword = (ImageEditView) view.findViewById(R.id.ie_password);
        ieConfirmPassword = (ImageEditView) view.findViewById(R.id.ie_confirm_password);
        etVerification = (EditText) view.findViewById(R.id.et_verification);
        tvGetVerification = (TextView) view.findViewById(R.id.tv_get_verification);
        btnRegister = (Button) view.findViewById(R.id.btn_register);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void verificationClick() {
        tvGetVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPhone()) {
                    postSendMessage();
                }
            }
        });
    }


    private boolean checkPhone() {
        if (iePhone.getText() == null
                || "".equals(iePhone.getText().toString())) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.cell_phone_number_tip));
            return false;
        }

        if (!iePhone.getText().toString().matches("^[0-9]{11}$")) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.cell_phone_number_correct_tip));
            return false;
        }
        return true;
    }

    private boolean check() {
        if (ieUserName.getText() == null
                || "".equals(iePhone.getText().toString())) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.your_name_tip));
            return false;
        } else if (etVerification.getText() == null
                || "".equals(etVerification.getText().toString())) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.verification_code_tip));
            return false;
        } else if (ieUserName.getText() == null
                || "".equals(ieUserName.getText().toString())) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.username_tip));
            return false;
        } else if (iePassword.getText() == null
                || "".equals(iePassword.getText().toString())) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.input_password_tip));
            return false;
        } else if (iePassword.getText() == null
                || "".equals(iePassword.getText().toString())) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.input_confirm_password_tip));
            return false;
        }

        if (!iePhone.getText().toString().matches("^[0-9]{11}$")) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.cell_phone_number_correct_tip));
            return false;
        }
        return true;
    }

    private void postSendMessage() {
        strMobile = iePhone.getText().toString();
        Map<String, String> map = new HashMap<String, String>();

        map.put("mobile", strMobile);
        map.put("SendStatus", "1");

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.SenMessageUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String status = jsonObject.getString("status").toString();
                    if (status.equals("true")) {
                        String result = jsonObject.getString("result").toString();
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        myHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 2;
                        String strError = jsonObject.getString("message");
                        msg.obj = strError;
                        myHandler.sendMessage(msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    myHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 3;
                myHandler.sendMessage(msg);
            }
        }, map);

        request.setTag(requestTag);
        requestQueue.add(request);
    }

    private void registerClick() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    postRegister();
                }
            }
        });
    }

    private void postRegister() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("UserName", ieUserName.getText().toString());
        map.put("Password", iePassword.getText().toString());
        map.put("ConfirmPwd", ieConfirmPassword.getText().toString());
        map.put("Mobile", iePhone.getText().toString());
        map.put("MobileCode", etVerification.getText().toString());

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.RegisterUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Message msg = new Message();
                msg.what = 4;
                myHandler.sendMessage(msg);
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

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    UIHelper.ToastMessage(getActivity(), "短信已发送");
//                    String result = (String) msg.obj;
//                    etVerification.setText(result);
                    break;
                }
                case 2: {
                    String message = (String) msg.obj;
                    UIHelper.ToastMessage(getActivity(), message);

                    break;
                }
                case 3: {
                    UIHelper.ToastMessage(getActivity(), getString(R.string.get_verification_faild));
                    break;
                }
                case 4: {
                    UIHelper.ToastMessage(getActivity(), getString(R.string.register_success));
                    getActivity().finish();
                    break;
                }case 5: {
                    UIHelper.ToastMessage(getActivity(), getString(R.string.register_failed));
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

