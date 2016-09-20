package com.dgzd.mxtx.activity.mineView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgzd.mxtx.R;

/**
 * @version V1.0 <功能>
 * @FileName: EditNicknameActivity.java
 * @author: Jessica
 * @date: 2015-12-22 14:43
 */

public class EditQQNumberActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_qq_number);
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvSave = (TextView)findViewById(R.id.tv_save);
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
