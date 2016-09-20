package com.dgzd.mxtx.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @version V1.0 <功能>
 * @FileName: SquaredImageView.java
 * @author: Jessica
 * @date: 2016-01-13 16:09
 */

public class SquaredImageView extends ImageView{
    public SquaredImageView(Context context) {
        super(context);
    }

    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
