package com.example.myapplication.app.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;


import java.util.List;

/**
 * Created by unnoo on 2014/5/14.
 */
public class ImagesManager {

    private static ImagesManager manager=null;
    private ImagesManager() {
        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
        // LruCache通过构造函数传入缓存值，以KB为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static ImagesManager getInstance(){
        if(manager==null){
            manager=new ImagesManager();
        }
       return manager;

    }

    private LruCache<String,Bitmap> mMemoryCache=null;
    /**
     * 计算图片压缩比例
     * @param options BitmapFactory.Options
     * @param reqWidth 目标宽度
     * @param reqHeight 需目标高度
     * @return 计算出的比例
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        final int width=options.outWidth;
        final int height=options.outHeight;
        int inSampleSize=1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 得到处理后的Bitmap
     * @param path 图片路径
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeFile(path,options);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void setImgBitmap(ImageView imageView,String path){
        BitmapWorkerTask task=new BitmapWorkerTask(imageView);
        task.execute(path);
    }


    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private  ImageView imageView;
        public  BitmapWorkerTask(ImageView imageView){
            this.imageView=imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            final Bitmap bitmap = decodeSampledBitmapFromResource(
                    params[0], 100 , 100);
            addBitmapToMemoryCache(params[0], bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
    class BitmapsWorkerTask extends AsyncTask<String, Void, List<Bitmap>> {
        // 在后台加载图片。
        List<Bitmap> lt = null;

        @Override
        protected List<Bitmap> doInBackground(String... params) {
            for (int i = 0, j = params.length; i < j; i++) {
                final Bitmap bitmap = decodeSampledBitmapFromResource(
                        params[i], 100, 100);
                addBitmapToMemoryCache(params[i], bitmap);
                lt.add(bitmap);
            }
            return lt;
        }
    }
}
