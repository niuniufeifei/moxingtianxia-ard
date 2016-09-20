package com.dgzd.mxtx.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.adapter.ShowViewAdapter;
import com.dgzd.mxtx.entirety.HotViewInfo;
import com.dgzd.mxtx.entirety.ImageTextInfo;

import java.util.Collections;
import java.util.List;

public class HotView extends LinearLayout {
    public interface MoreClickListener {
        public void onClickButton();
    }

    private MoreClickListener moreListener = null;

    public void setOnMoreClick(MoreClickListener listener) {
        moreListener = listener;
    }

    private TextView tvHotTitle;
    private TextView tvMore;
    private NoScrollGridView gvHotView;
    private Context context;
    private HotViewInfo imageIdList;

    public HotView(Context context) {
        super(context);
    }

    public HotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyHotView);
        String text = ta.getString(R.styleable.MyHotView_text);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_hot_view, this);

        tvHotTitle = (TextView) findViewById(R.id.tv_hot_title);
        tvHotTitle.setText(text);
        tvMore = (TextView) findViewById(R.id.tv_more);
        gvHotView = (NoScrollGridView) findViewById(R.id.gv_hot_view);
        hotMoreClick();
        ta.recycle();
    }

    public void setHotTitle(int resId) {
        tvHotTitle.setText(resId);
    }

    public void setMore(int resId) {
        tvMore.setText(resId);
    }

    public void setGridImageList(HotViewInfo imageIdList) {
        this.imageIdList = imageIdList;
        initGrid();
    }

    private void initGrid() {
        hotMoreClick();
        ShowViewAdapter adapter = new ShowViewAdapter(imageIdList, context);
        gvHotView.setAdapter(adapter);
//        measureHeight();
    }

    public void hotMoreClick() {
        tvMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moreListener.onClickButton();
            }
        });
    }

    private int measureHeight() {
        // get ListView adapter
        ListAdapter adapter = gvHotView.getAdapter();
        if (null == adapter) {
            return 0;
        }

        int totalHeight = 0;

        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, gvHotView);
            if (null == item) continue;
            // measure each item width and height
            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gvHotView.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (1 * (adapter.getCount() - 1));

        gvHotView.setLayoutParams(params);

        return params.height;
    }
}