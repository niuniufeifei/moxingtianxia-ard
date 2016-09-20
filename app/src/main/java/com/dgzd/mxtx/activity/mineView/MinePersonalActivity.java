package com.dgzd.mxtx.activity.mineView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.custom.CircleImageView;
import com.dgzd.mxtx.custom.TxtTxtBtn;
import com.dgzd.mxtx.tools.GlobalEntity;
import com.dgzd.mxtx.tools.MxtxApplictaion;
import com.squareup.picasso.Picasso;

/**
 * @version V1.0 <功能>
 * @FileName: MinePersonalActivity.java
 * @author: Jessica
 * @date: 2015-12-21 13:38
 */

public class MinePersonalActivity extends AppCompatActivity {

    private ImageView ivBack;
    private CircleImageView ivPhoto;
    private RelativeLayout layoutUserNickname;
    private RelativeLayout layoutCellPhone;
    private RelativeLayout layoutAddress;

    private TxtTxtBtn txttxtbtnNickname;
    private TxtTxtBtn txttxtbtnCellPhone;
    private TxtTxtBtn txttxtbtnAddress;
    private MxtxApplictaion myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_page_mine_personal);
        myApp = MxtxApplictaion.getInstance();
        initView();
        backClick();
        nickNameClick();
        cellPhoneClick();
        addressClick();
    }

    private void initView() {
        ivPhoto = (CircleImageView) findViewById(R.id.iv_photo);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        layoutUserNickname = (RelativeLayout) findViewById(R.id.layout_my_nickname);
        layoutCellPhone = (RelativeLayout) findViewById(R.id.layout_cell_phone);
        layoutAddress = (RelativeLayout) findViewById(R.id.layout_my_address);
        txttxtbtnNickname = (TxtTxtBtn) findViewById(R.id.txttxtbtn_my_nickname);
        txttxtbtnCellPhone = (TxtTxtBtn) findViewById(R.id.txttxtbtn_cell_phone);
        txttxtbtnAddress = (TxtTxtBtn) findViewById(R.id.txttxtbtn_my_address);


        txttxtbtnNickname.setContentText(myApp.personalInfo.getNickName());
        txttxtbtnCellPhone.setContentText(myApp.personalInfo.getMobile());
        txttxtbtnAddress.setContentText(myApp.personalInfo.getAddress());

        if (myApp.personalInfo != null) {
            String photoImageUrl = myApp.personalInfo.getAvatar();
            Picasso.with(MinePersonalActivity.this).load(photoImageUrl).into(ivPhoto);
        } else {
            ivPhoto.setBackgroundResource(R.drawable.default_photo);
        }
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void nickNameClick() {
        layoutUserNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MinePersonalActivity.this, EditNicknameActivity.class), GlobalEntity.MODIFY_USER_INFO_NICKNAME);
            }
        });
    }

    private void cellPhoneClick() {
        layoutCellPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MinePersonalActivity.this, EditCellPhoneActivity.class);
                startActivityForResult(intent, GlobalEntity.MODIFY_USER_INFO_CELL_PHONE);
            }
        });
    }

    private void addressClick() {
        layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MinePersonalActivity.this, EditAddressActivity.class), GlobalEntity.MODIFY_USER_INFO_ADDRESS);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalEntity.MODIFY_USER_INFO_NICKNAME) {
            if (resultCode == GlobalEntity.MODIFY_USER_INFO_SUCCESS) {
                if (data != null) {
                    String nickName = data.getStringExtra("nickName");
                    txttxtbtnNickname.setContentText(nickName);
                }
            }
        } else if (requestCode == GlobalEntity.MODIFY_USER_INFO_CELL_PHONE) {
            if (resultCode == GlobalEntity.MODIFY_USER_INFO_SUCCESS) {
                if (data != null) {
                    String cellPhone = data.getStringExtra("cellPhone");
                    txttxtbtnCellPhone.setContentText(cellPhone);
                }
            }
        } else if (requestCode == GlobalEntity.MODIFY_USER_INFO_ADDRESS) {
            if (resultCode == GlobalEntity.MODIFY_USER_INFO_SUCCESS) {
                if (data != null) {
                    String address = data.getStringExtra("address");
                    txttxtbtnAddress.setContentText(address);
                }
            }
        }
    }
}
