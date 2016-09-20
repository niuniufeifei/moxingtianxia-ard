package com.dgzd.mxtx.fragment.base;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.dgzd.mxtx.activity.login.LoginActivity;
import com.dgzd.mxtx.tools.MxtxApplictaion;

public class BaseFragment extends Fragment {
    private final int REQUEST_LOGIN = 1;
    private boolean requireLogin = true;

    @Override
    public void onStart() {
        super.onStart();
        if (requireLogin && !MxtxApplictaion.getInstance().isSignedIn()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode) {
//		case REQUEST_LOGIN:
//			if (resultCode != 1){
//				//TODO
//				
//				break;	
//			}
//		}
//	}
//	

    /**
     * 设置该界面是否需要登录
     *
     * @param requireLogin
     */
    protected void setRequireLogin(boolean requireLogin) {
        this.requireLogin = requireLogin;
    }

}
