package com.rdc.imageloader.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rdc.imageloader.R;
import com.rdc.imageloader.blackwhite.ImageLoader;

import java.util.List;

/**
 * Created by blackwhite on 15-11-7.
 */
public class ImageAdapter extends BaseAdapter {

    private List<String> mUrlList;
    private Context mContext;
    private ImageLoader mImageLoader;
    private boolean mIsGridViewIdle = false;

    public ImageAdapter(Context mContext, List<String> mUrlList) {
        this.mContext = mContext;
        this.mUrlList = mUrlList;
        mImageLoader = new ImageLoader(mContext);
    }

    public void setGridViewScrollState(boolean isGridViewIdle) {
        mIsGridViewIdle = isGridViewIdle;
    }

    @Override
    public int getCount() {
        return mUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, null, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!getItem(position).equals(holder.imageView.getTag())) {
            holder.imageView.setImageResource(R.drawable.ic_refresh_blue_a400_18dp);
        }

        String urlStr = mUrlList.get(position);
        int maxWidth = holder.imageView.getWidth();
        int maxHeight = holder.imageView.getHeight();
        holder.imageView.setTag(urlStr);
        if (mIsGridViewIdle) {
//            mImageLoader.bindImageView(mUrlList.get(position), holder.imageView);
            mImageLoader.loadBitmap(urlStr, maxWidth, maxHeight, new ImageLoader.ImageListener() {
                @Override
                public void onResonse(ImageLoader.ImageContainer container) {
                    Bitmap bitmap = container.getBitmap();
                    String tag = (String) holder.imageView.getTag();
                    if (bitmap != null && container.getUrlStr().equals(tag)) {
                        holder.imageView.setImageBitmap(bitmap);
                    } else {
                        holder.imageView.setImageResource(R.drawable.ic_refresh_blue_a400_18dp);
                    }
                }

            });
        }
        return convertView;
    }


    class ViewHolder {
        ImageView imageView;
    }
}
