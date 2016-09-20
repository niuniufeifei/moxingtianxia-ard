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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
public class ForgetPasswordActivityFragment extends Fragment {
    private View view;
    private ImageView ivBack;
    private ImageEditView iePhone, iePassword, ieConfirmPassword;
    private EditText etVerification;
    private Button btnVerification, btnOk;
    private RequestQueue requestQueue;
    private final String requestTag = "ForgetPasswordActivityFragment";

    public ForgetPasswordActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        initView();
        backClick();
        verificationClick();
        okClick();
        return view;
    }

    private void initView() {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        iePhone = (ImageEditView) view.findViewById(R.id.ie_phone);
        iePassword = (ImageEditView) view.findViewById(R.id.ie_password);
        ieConfirmPassword = (ImageEditView) view.findViewById(R.id.ie_confirm_password);
        btnVerification = (Button) view.findViewById(R.id.btn_verification);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private boolean check() {
        if (iePhone.getText() == null
                || "".equals(iePhone.getText().toString())) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.phone_tip));
            return false;
        } else if (etVerification.getText() == null
                || "".equals(etVerification.getText().toString())) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.verification_code_tip));
            return false;
        } else if (iePassword.getText() == null
                || "".equals(iePassword.getText().toString())) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.new_password_tip));
            return false;
        } else if (ieConfirmPassword.getText() == null
                || "".equals(ieConfirmPassword.getText().toString())) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.new_password_confirm_tip));
            return false;
        }

        if (!iePhone.getText().toString().matches("^[0-9]{11}$")) {
            UIHelper.ToastMessage(getActivity(), getString(R.string.cell_phone_number_correct_tip));
            return false;
        }

        return true;
    }

    private void verificationClick() {
        btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    postSendMessage();
                }
            }
        });
    }

    private void okClick() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postResetPassword();
            }
        });
    }

    private void postSendMessage() {
        String strMobile = iePhone.getText().toString().trim();
        Map<String, String> map = new HashMap<String, String>();

        map.put("mobile", strMobile);
        map.put("SendStatus", "2");

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

    private void postResetPassword() {
        String strMobile = iePhone.getText().toString().trim();
        String mobilecode = etVerification.getText().toString().trim();
        String pwd = iePassword.getText().trim();
        String confirmPwd = ieConfirmPassword.getText().trim();
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", strMobile);
        map.put("mobilecode", mobilecode);
        map.put("password", pwd);
        map.put("ConfirmPwd", confirmPwd);

        Request<JSONObject> request = new NormalPostRequest(GlobalEntity.ResetPwdUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String status = jsonObject.getString("status").toString();
                    if (status.equals("true")) {
                        String result = jsonObject.getString("result").toString();
                        Message msg = new Message();
                        msg.what = 4;
                        msg.obj = result;
                        myHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 5;
                        myHandler.sendMessage(msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 6;
                    myHandler.sendMessage(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message msg = new Message();
                msg.what = 6;
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
                    UIHelper.ToastMessage(getActivity(), getString(R.string.reset_pwd_success));
                    break;
                }
                case 5: {

                    break;
                }
                case 6: {
                    UIHelper.ToastMessage(getActivity(), getString(R.string.reset_pwd_failed));
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

