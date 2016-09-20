package com.dgzd.mxtx.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dgzd.mxtx.R;

/**
 * @version V1.0 <ImageView&EditText结合的控件>
 * @FileName: EditImageView.java
 * @author: Jessica
 * @date: 2015-12-18 09:54
 */

public class ImageEditView extends RelativeLayout {
    private Context context;
    private ImageView ivIcon;
    private EditText etText;
    private RelativeLayout layoutRoot;

    public ImageEditView(Context context) {
        super(context);
    }

    public ImageEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyImageEditView);
        String strHint = ta.getString(R.styleable.MyImageEditView_hint);
        Drawable draw = ta.getDrawable(R.styleable.MyImageEditView_src);
        int textColor = ta.getColor(R.styleable.MyImageEditView_textColor, getResources().getColor(R.color.ghost_white));
        int textColorHint = ta.getColor(R.styleable.MyImageEditView_textColorHint, getResources().getColor(R.color.ghost_white));
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_image_edit_view, this);
        layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        ivIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ivIcon.setImageDrawable(draw);
        etText = (EditText) findViewById(R.id.et_text);
        etText.setHint(strHint);
        etText.setTextColor(textColor);
        etText.setHintTextColor(textColorHint);
    }

    public String getText() {
        return etText.getText().toString();
    }

}
