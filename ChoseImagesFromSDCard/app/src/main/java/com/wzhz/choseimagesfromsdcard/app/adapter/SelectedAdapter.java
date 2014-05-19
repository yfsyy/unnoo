package com.wzhz.choseimagesfromsdcard.app.adapter;

import java.util.List;


import com.wzhz.choseimagesfromsdcard.app.R;
import com.wzhz.choseimagesfromsdcard.app.ShowSelectedImagesActivity;
import com.wzhz.choseimagesfromsdcard.app.other.NativeImageLoader;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class SelectedAdapter extends BaseAdapter {
    private List<String> lt;//选中图片的路径
    private LayoutInflater inflater;
    private GridView selectedGridView;
    private ShowSelectedImagesActivity context;

    public SelectedAdapter(ShowSelectedImagesActivity context, List<String> lt, GridView selectedGridView) {
        this.context = context;
        this.lt = lt;
        this.selectedGridView = selectedGridView;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lt.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        String path = "";
        if (position < lt.size()) {
            path = lt.get(position);
        }
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.showselectedimage, null);
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.item_grid_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.image.setTag(path);
        System.out.println("current position:" + position);
        if (position == context.getIntent().getStringArrayListExtra("selectedpath").size()) {

            //选择的图片小于9张时，在后面有个添加图片
            holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
            if (position == 9) {
                //选择为9张时，添加图片 消失
                holder.image.setVisibility(View.GONE);
            }
        } else {
            Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, holder.image.getWidth(), holder.image.getHeight(), new NativeImageLoader.NativeImageCallBack() {

                @Override
                public void onImageLoader(Bitmap bitmap, String path) {
                    ImageView mImageView = (ImageView) selectedGridView.findViewWithTag(path);
                    if (bitmap != null && mImageView != null) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            });

            if (bitmap != null) {
                holder.image.setImageBitmap(bitmap);
            } else {
                holder.image.setImageResource(R.drawable.friends_sends_pictures_no);
            }

        }
        return view;
    }

    public class ViewHolder {
        ImageView image;
    }

}
