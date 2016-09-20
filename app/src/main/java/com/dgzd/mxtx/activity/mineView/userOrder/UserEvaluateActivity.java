package com.dgzd.mxtx.activity.mineView.userOrder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.dgzd.mxtx.R;

/**
 * @version V1.0 <评价>
 * @FileName: UserEvaluateActivity.java
 * @author: Jessica
 * @date: 2015-12-23 14:24
 */

public class UserEvaluateActivity extends AppCompatActivity{

    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_evaluate);
        initView();
        backClick();
    }

    private void initView(){
        ivBack = (ImageView)findViewById(R.id.iv_back);
    }

    private void backClick(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
