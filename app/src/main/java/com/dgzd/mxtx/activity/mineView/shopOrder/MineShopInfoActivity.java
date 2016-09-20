package com.dgzd.mxtx.activity.mineView.shopOrder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.activity.mineView.userVideo.SelectPicPopupWindow;
import com.dgzd.mxtx.custom.TxtTxtBtn;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;

/**
 * @version V1.0 <功能>
 * @FileName: MinePersonalActivity.java
 * @author: Jessica
 * @date: 2015-12-21 13:38
 */

public class MineShopInfoActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TxtTxtBtn txttxtbtnMyNickname;
    private TextView tvUploadShopPhoto;
    private RelativeLayout layoutShopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_shop_info);
        initView();
        backClick();
        uploadShopPhotoClick();
        shopNameClick();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        txttxtbtnMyNickname = (TxtTxtBtn) findViewById(R.id.txttxtbtn_my_nickname);
        tvUploadShopPhoto = (TextView) findViewById(R.id.tv_upload_shop_photo);
        layoutShopName = (RelativeLayout) findViewById(R.id.layout_shop_name);
        txttxtbtnMyNickname.setContentText(MxtxApplictaion.getInstance().personalInfo.getStoreName());
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void uploadShopPhotoClick() {
        tvUploadShopPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineShopInfoActivity.this, SelectPicPopupWindow.class));

            }
        });
    }

    private void shopNameClick() {
        layoutShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MineShopInfoActivity.this, EditShopNameActivity.class), GlobalEntity.MODIFY_SHOP_INFO_NAME);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == GlobalEntity.MODIFY_SHOP_INFO_NAME) {
            if (data != null) {
                String shopName = data.getStringExtra("shopName");
                txttxtbtnMyNickname.setContentText(MxtxApplictaion.getInstance().personalInfo.getStoreName());
            }
        }
    }
}
