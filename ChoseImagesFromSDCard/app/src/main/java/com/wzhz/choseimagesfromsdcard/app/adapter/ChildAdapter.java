package com.wzhz.choseimagesfromsdcard.app.adapter;


import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wzhz.choseimagesfromsdcard.app.R;
import com.wzhz.choseimagesfromsdcard.app.other.MyImageView;
import com.wzhz.choseimagesfromsdcard.app.other.NativeImageLoader;


public class ChildAdapter extends BaseAdapter {

    private GridView mGridView;
    private List<String> list;//保存选中目录下的图片地址
    protected LayoutInflater mInflater;
    private int totalSelected;//选中的总数
    private Handler handler;
    private int imgHeight;//测量图片的高度
    private int imgWidth;//测量图片的宽度

    public ChildAdapter(Context context, List<String> list, GridView mGridView, Handler handler) {
        this.list = list;
        this.mGridView = mGridView;
        this.handler = handler;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final String path = list.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_child_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
            //用来监听ImageView的宽和高
            viewHolder.mImageView.setOnMeasureListener(new MyImageView.OnMeasureListener() {

                @Override
                public void onMeasureSize(int width, int height) {
                    imgHeight = height;
                    imgWidth = width;
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }
        viewHolder.mImageView.setTag(path);
        viewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //如果是未选中的CheckBox,则添加动画
                if (!NativeImageLoader.mSelectMap.containsKey(path) || !NativeImageLoader.mSelectMap.get(path)) {
                    if (NativeImageLoader.mSelectMap.size() < 9) {
                        addAnimation(viewHolder.mCheckBox);
                    }
                }
                if (NativeImageLoader.mSelectMap.size() < 9) {
                    if (isChecked) {
                        NativeImageLoader.mSelectMap.put(path, isChecked);
                    } else {
                        //NativeImageLoader.mSelectMap.remove(path);
                    }
                } else {
                    if (isChecked) {
                        viewHolder.mCheckBox.setChecked(!isChecked);
                        handler.sendEmptyMessage(-1);
                    } else {
                        viewHolder.mCheckBox.setChecked(isChecked);
                        //NativeImageLoader.mSelectMap.remove(path);
                    }
                }
            }
        });

        //viewHolder.mCheckBox.setChecked(NativeImageLoader.mSelectMap.containsKey(path) ? NativeImageLoader.mSelectMap.get(path) : false);

        //利用NativeImageLoader类加载本地图片
        Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, imgWidth, imgHeight, new NativeImageLoader.NativeImageCallBack() {

            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
                if (bitmap != null && mImageView != null) {
                    mImageView.setImageBitmap(bitmap);
                }
            }
        });

        if (bitmap != null) {
            viewHolder.mImageView.setImageBitmap(bitmap);
        } else {
            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }

        return convertView;
    }

    /**
     * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
     *
     * @param view
     */
    private void addAnimation(View view) {
        float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
                ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }


    public class ViewHolder {
        public MyImageView mImageView;
        public CheckBox mCheckBox;
    }


}
