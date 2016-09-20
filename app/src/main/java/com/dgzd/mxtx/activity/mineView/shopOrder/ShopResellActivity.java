package com.dgzd.mxtx.activity.mineView.shopOrder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.custom.CircleImageView;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.squareup.picasso.Picasso;

/**
 * @version V1.0 <我的二手转卖>
 * @FileName: ShopResellActivity.java
 * @author: Jessica
 * @date: 2015-12-28 14:23
 */

public class ShopResellActivity extends AppCompatActivity {

    private ImageView ivBack;
    private CircleImageView ivPhoto;
    private TextView tvShopName;
    private TextView tvCompleteModify;
    private RelativeLayout layoutPostTrad, layoutManageProducts, layoutOrderManagement;
    private MxtxApplictaion myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_shop_resell);
        myApp = MxtxApplictaion.getInstance();
        initView();
        backClick();
        completeCModifyClick();
        postTradeClick();
        manageProductsClick();
        orderManagementClick();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivPhoto = (CircleImageView) findViewById(R.id.iv_photo);
        tvShopName = (TextView)findViewById(R.id.tv_shop_name);
        tvCompleteModify = (TextView) findViewById(R.id.tv_complete_modify);
        layoutPostTrad = (RelativeLayout) findViewById(R.id.layout_post_trad);
        layoutManageProducts = (RelativeLayout) findViewById(R.id.layout_manage_products);
        layoutOrderManagement = (RelativeLayout) findViewById(R.id.layout_order_management);

        String storeLogoUrl = myApp.personalInfo.getStoreLogo();
        Picasso.with(this).load(storeLogoUrl).into(ivPhoto);
        tvShopName.setText(myApp.personalInfo.getStoreName());
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void completeCModifyClick() {
        tvCompleteModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ShopResellActivity.this, MineShopInfoActivity.class), GlobalEntity.MODIFY_SHOP_INFO_NAME);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalEntity.MODIFY_SHOP_INFO_NAME) {
            if (resultCode == GlobalEntity.MODIFY_USER_INFO_SUCCESS) {
                if (data != null) {
                    String shopName = data.getStringExtra("shopName");
//                    tvShopName.setText(shopName);
                    tvShopName.setText(myApp.personalInfo.getStoreName());
                }
            }
        }
    }

    private void postTradeClick() {
        layoutPostTrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopResellActivity.this, PosTradeActivity.class));
            }
        });
    }

    private void manageProductsClick() {
        layoutManageProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopResellActivity.this, ManageProductsActivity.class));
            }
        });
    }

    private void orderManagementClick() {
        layoutOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopResellActivity.this, OrderManagementActivity.class));
            }
        });
    }

}
