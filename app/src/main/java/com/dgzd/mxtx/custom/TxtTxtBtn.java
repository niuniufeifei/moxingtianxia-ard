package com.dgzd.mxtx.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgzd.mxtx.R;


public class TxtTxtBtn extends RelativeLayout {
    private TextView textviewTitle;
    private TextView textviewContent;

    public TxtTxtBtn(Context context) {
        super(context);
    }

    public TxtTxtBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_text_text_btn, this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TxtTxtBtnView);
        String textTitle = ta.getString(R.styleable.TxtTxtBtnView_text_title);
        String textContent = ta.getString(R.styleable.TxtTxtBtnView_text_content);
        textviewTitle = (TextView) findViewById(R.id.textview_title);
        textviewContent = (TextView) findViewById(R.id.textview);
        textviewTitle.setText(textTitle);
        textviewContent.setText(textContent);
        ta.recycle();
    }

    public void setTitleText(int resId) {
        textviewTitle.setText(resId);
    }

    public void setContentText(int resId) {
        textviewContent.setText(resId);
    }

    public void setContentText(String content) {
        textviewContent.setText(content);
    }

    public void setTitleSize(int size) {
        textviewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setContentTextSize(int size) {
        textviewContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setTextSize(int size) {
        textviewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textviewContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
}