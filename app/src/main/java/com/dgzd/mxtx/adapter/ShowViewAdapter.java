package com.dgzd.mxtx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgzd.mxtx.R;
import com.dgzd.mxtx.custom.MarqueeTextView;
import com.dgzd.mxtx.custom.SquaredImageView;
import com.dgzd.mxtx.entirety.HotViewInfo;
import com.squareup.picasso.Picasso;


public class ShowViewAdapter extends BaseAdapter {
    private HotViewInfo showViewArray;
    private Context context;
    private LayoutInflater mInflater;

    public ShowViewAdapter() {
        super();
    }

    public ShowViewAdapter(HotViewInfo showViewArray, Context context) {
        super();
        if (null == context)
            return;
        this.context = context;
        this.mInflater = LayoutInflater.from(this.context);
        this.showViewArray = showViewArray;
    }

    @Override
    public int getCount() {
        return showViewArray.getInfoList().size();
    }

    @Override
    public Object getItem(int arg0) {
        return showViewArray.getInfoList().get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate( R.layout.show_hot_view_item, null);
            holder = new ViewHolder();

            holder.tvText = (MarqueeTextView) convertView.findViewById(R.id.tv_show);
            holder.ivTitle = (ImageView) convertView.findViewById(R.id.iv_title);
            holder.ivImage = (SquaredImageView) convertView.findViewById(R.id.iv_show);
            holder.tvPriceNum = (TextView)convertView.findViewById(R.id.tv_price_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivTitle.setBackgroundResource(showViewArray.getImgResourceId());
        holder.tvText.setText(showViewArray.getInfoList().get(arg0).strText);
        boolean isShowPrice = showViewArray.getInfoList().get(arg0).isShowPrice;
        if (isShowPrice){
            holder.tvPriceNum.setVisibility(View.VISIBLE);
            holder.tvPriceNum.setText(showViewArray.getInfoList().get(arg0).strPrice);
        }else {
            holder.tvPriceNum.setVisibility(View.GONE);
        }
        Picasso.with(context).load(showViewArray.getInfoList().get(arg0).imgUrl).error(R.mipmap.ic_launcher).into(holder.ivImage);

//        DisplayImageOptions options = new DisplayImageOptions.Builder()
////                .showStubImage(R.mipmap.ic_launcher) //加载图片时的图片
////                .showImageForEmptyUri(R.mipmap.ic_launcher) // 没有图片资源时的默认图片
//                .showImageOnFail(R.mipmap.ic_launcher) //加载失败时的图片
//                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .cacheInMemory(true) // 启用内存缓存
//                .cacheOnDisc(true) // 启用外存缓存
//                        // .considerExifParams(true) //启用EXIF和JPEG图像格式
//                        // .displayer(new RoundedBitmapDisplayer(20))
//                        // //设置显示风格这里是圆角矩形
//                .build();
//
//        ImageLoader.getInstance().displayImage(showViewArray.get(arg0).imgUrl, holder.ivImage, options);
        return convertView;
    }

    private final class ViewHolder {
        private SquaredImageView ivImage;
        private MarqueeTextView tvText;
        private ImageView ivTitle;
        private TextView tvPriceNum;
    }
}
