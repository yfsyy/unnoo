package com.wzhz.choseimagesfromsdcard.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wzhz.choseimagesfromsdcard.app.R;
import com.wzhz.choseimagesfromsdcard.app.other.NativeImageLoader;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by unnoo on 2014/5/20.
 */
public class MyPageAdapter extends PagerAdapter {
    private List<String> selectedImgsPath;//所有选中的图片路径
    private int size;// 页数
    private Context context;
    private LayoutInflater inflater;
    private int screenWidth;
    private int screenHeight;
    //private Handler handler;
    private Bitmap bitmap;
    public MyPageAdapter(Context context, List<String> lt, int screenWidth, int screenHeight) {
        this.context = context;
        this.selectedImgsPath = lt;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        //this.handler=handler;
        size = lt == null ? 0 : lt.size();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View view = inflater.inflate(R.layout.zoom_image_layout, null);
        PhotoView imgView = (PhotoView) view.findViewById(R.id.id_image);
        imgView.setTag(selectedImgsPath.get(position));
         bitmap = NativeImageLoader.getInstance().loadNativeImage(selectedImgsPath.get(position), screenWidth, screenHeight, new NativeImageLoader.NativeImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                PhotoView imgView = (PhotoView) view.findViewWithTag(selectedImgsPath.get(position));
                imgView.setImageBitmap(bitmap);
            }
        });
        if (bitmap == null) {

            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.friends_sends_pictures_no);
            Log.e("wrongString", selectedImgsPath.get(position) + " is not right");
            // Toast.makeText(context,"图片损坏", Toast.LENGTH_SHORT).show();
        } else {
            imgView.setImageBitmap(bitmap);
           /* Message msg=new Message();
            msg.arg1=bitmap.getWidth();
            msg.arg2=bitmap.getHeight();
            handler.sendMessage(msg);*/
            //Toast.makeText(context,"图片宽："+bitmap.getWidth()+" 高："+bitmap.getHeight(),Toast.LENGTH_SHORT).show();
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
