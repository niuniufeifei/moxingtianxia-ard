package com.dgzd.mxtx.activity.startView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.mainView.HomePageActivity;

/**
 * @version V1.0 <启动页面>
 * @FileName: SplashActivity.java
 * @author: Jessica
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 2000);
    }

    class splashhandler implements Runnable {

        public void run() {
            startActivity(new Intent(getApplication(), HomePageActivity.class));
            SplashActivity.this.finish();
        }
    }
}
