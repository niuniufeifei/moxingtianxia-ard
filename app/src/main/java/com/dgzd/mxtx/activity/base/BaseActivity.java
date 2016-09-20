package com.dgzd.mxtx.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.dgzd.mxtx.activity.login.LoginActivity;
import com.dgzd.mxtx.tools.MxtxApplictaion;

public class BaseActivity extends AppCompatActivity {
    private final int REQUEST_LOGIN = 1;
    private boolean requireLogin = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //keyCode==KeyEvent.KEYCODE_BACK||
        if (keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_MENU
                || keyCode == KeyEvent.KEYCODE_ALT_LEFT || keyCode == KeyEvent.KEYCODE_ALT_RIGHT
                || keyCode == KeyEvent.KEYCODE_CTRL_LEFT || keyCode == KeyEvent.KEYCODE_CTRL_RIGHT
                || keyCode == KeyEvent.KEYCODE_META_LEFT || keyCode == KeyEvent.KEYCODE_META_RIGHT) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MxtxApplictaion.getInstance().addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isSigned = MxtxApplictaion.getInstance().isSignedIn();
        if (requireLogin && !isSigned) {
            startActivityForResult(new Intent(BaseActivity.this, LoginActivity.class), REQUEST_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode != RESULT_OK)
                    finish();
                break;
        }
    }


    /**
     * 设置该界面是否需要登录
     *
     * @param requireLogin
     */
    protected void setRequireLogin(boolean requireLogin) {
        this.requireLogin = requireLogin;
    }

}
