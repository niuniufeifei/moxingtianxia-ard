package com.dgzd.mxtx.activity.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.tools.GlobalEntity;

/**
 * @version V1.0 <图片预览>
 * @FileName: ImagePreView.java
 * @author: Jessica
 */

public class ImagePreView extends Activity {

    public static final String IMAGE_PATH = "image_path";
    public static final String IMAGE_SIDE = "image_side";
    private RelativeLayout layout_top;
    private ImageView ivBack, ivDelete, ivShow;
    private String imagePath;
    private boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        getIntentData();
        initView();
        backClick();
        previewClick();
        deleteClick();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        imagePath = intent.getStringExtra(IMAGE_PATH);
        isFront = intent.getBooleanExtra(ImagePreView.IMAGE_SIDE, true);
    }

    private void initView() {
        layout_top = (RelativeLayout) findViewById(R.id.layout_top);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        ivShow = (ImageView) findViewById(R.id.iv_show);

        if (null != imagePath) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeFile(imagePath, options);
            ivShow.setImageBitmap(bm);
        }

    }

    private void previewClick() {
        ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_top.isShown()) {
                    layout_top.setVisibility(View.INVISIBLE);
                } else {
                    layout_top.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void backClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deleteClick() {
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(IMAGE_SIDE, isFront);
                setResult(GlobalEntity.PREVIEW_DELETE, intent);
                finish();
            }
        });
    }

}
