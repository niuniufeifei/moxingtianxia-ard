package com.dgzd.mxtx.activity.mineView.userVideo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.tools.GlobalEntity;

public class SelectPicPopupWindow extends Activity implements OnClickListener {

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private LinearLayout layout;
    public static final String IMAGE_SIDE = "image_side";
    private boolean isFont = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);
        btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

        layout = (LinearLayout) findViewById(R.id.pop_layout);

        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);

        Intent intent = getIntent();
        isFont = intent.getBooleanExtra(IMAGE_SIDE, true);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo: {
                Intent intent = new Intent();
                intent.putExtra(IMAGE_SIDE, isFont);
                setResult(GlobalEntity.POPWINDOW_TAKE_PHOTO, intent);
                finish();
                break;
            }
            case R.id.btn_pick_photo: {
                Intent intent = new Intent();
                intent.putExtra(IMAGE_SIDE, isFont);
                setResult(GlobalEntity.POPWINDOW_PICK_PHOTO, intent);
                finish();
                break;
            }
            case R.id.btn_cancel: {
                setResult(GlobalEntity.POPWINDOW_CANCEL);
                finish();
                break;
            }
            default:
                break;
        }
    }

}
