package com.dgzd.mxtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.entirety.ImageTextInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Collections;
import java.util.List;


public class HotViewAdapter extends BaseAdapter {
    private List<ImageTextInfo> hotViewArray = Collections.emptyList();
    private Context context;
    private LayoutInflater mInflater;

    public HotViewAdapter(List<ImageTextInfo> hotViewArray, Context context) {
        super();
        if (null == context)
            return;
        this.context = context;
        this.mInflater = LayoutInflater.from(this.context);
        this.hotViewArray = hotViewArray;
    }

    @Override
    public int getCount() {
        return hotViewArray.size();
    }

    @Override
    public Object getItem(int arg0) {
        return hotViewArray.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.show_view_item, null);
            holder = new ViewHolder();
            holder.tvText = (TextView) convertView.findViewById(R.id.tv_show);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_show);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvText.setText(hotViewArray.get(arg0).strText);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // .showImageOnLoading(R.drawable.ic_stub) //加载图片时的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher) // 没有图片资源时的默认图片
                        // .showImageOnFail(R.drawable.ic_error) //加载失败时的图片
                .cacheInMemory(true) // 启用内存缓存
                .cacheOnDisc(true) // 启用外存缓存
                        // .considerExifParams(true) //启用EXIF和JPEG图像格式
                        // .displayer(new RoundedBitmapDisplayer(20))
                        // //设置显示风格这里是圆角矩形
                .build();

        ImageLoader.getInstance().displayImage(hotViewArray.get(arg0).imgUrl, holder.ivImage, options);

        return convertView;
    }

    private final class ViewHolder {
        private ImageView ivImage;
        private TextView tvText;
    }
}
