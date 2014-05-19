package com.wzhz.choseimagesfromsdcard.app.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

/**
 * 本地图片加载器,采用的是异步解析本地图片，单例模式利用getInstance()获取NativeImageLoader实例
 * 调用loadNativeImage()方法加载本地图片，此类可作为一个加载本地图片的工具类
 * 
 *
 */
public class NativeImageLoader {
	private LruCache<String, Bitmap> mMemoryCache;
	private static NativeImageLoader mInstance = new NativeImageLoader();
	private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(1);
	
	/**
	 * 用来存储图片的选中情况
	 */
	public static  HashMap<String, Boolean> mSelectMap = new HashMap<String, Boolean>();
	
	
	private NativeImageLoader(){
		// 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		//用最大内存的1/4来存储图片
		final int cacheSize = maxMemory / 8;
		// LruCache通过构造函数传入缓存值，以KB为单位。
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			
			//获取每张图片的大小
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
	}
	
	/**
	 * 通过此方法来获取NativeImageLoader的实例
	 * @return
	 */
	public static NativeImageLoader getInstance(){
		return mInstance;
	}
	
	
	/**
	 * 加载本地图片，对图片不进行裁剪
	 * @param path
	 * @param mCallBack
	 * @return
	 */
/*
	public Bitmap loadNativeImage(final String path, final NativeImageCallBack mCallBack){
		return this.loadNativeImage(path, null, mCallBack);
	}*/
	
	/**
	 * 此方法来加载本地图片，根据ImageView控件的大小来裁剪Bitmap
	 * @param path
	 * @param width
	 * @param height
	 * @param mCallBack
	 * @return
	 */
	public Bitmap loadNativeImage(final String path, final int width,final int height, final NativeImageCallBack mCallBack){
		//先获取内存中的Bitmap
		Bitmap bitmap = getBitmapFromMemCache(path);
		
		final Handler mHander = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				mCallBack.onImageLoader((Bitmap)msg.obj, path);
			}
			
		};
		
		//若该Bitmap不在内存缓存中，则启用线程去加载本地的图片，并将Bitmap加入到mMemoryCache中
		if(bitmap == null){
			mImageThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					//先获取图片的缩略图
					Bitmap mBitmap = decodeThumbBitmapForFile(path, width,height);
					Message msg = mHander.obtainMessage();
					msg.obj = mBitmap;
					mHander.sendMessage(msg);
					
					//将图片加入到内存缓存
					addBitmapToMemoryCache(path, mBitmap);
				}
			});
		}
		return bitmap;
		
	}

	
	
	/**
	 * 往内存缓存中添加Bitmap
	 * 
	 * @param key
	 * @param bitmap
	 */
	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * 根据key来获取内存中的图片
	 * @param key
	 * @return
	 */
	private Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}
	
	/**
	 * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
	 * @param path
	 * @param viewWidth
	 * @param viewHeight
	 * @return
	 */
	private Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight){
		BitmapFactory.Options options = new BitmapFactory.Options();
		//设置为true,表示解析Bitmap对象，该对象不占内存
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		double ratio = 1D;
        if (viewWidth > 0 && viewHeight <= 0) {
        	// 限定宽度，高度不做限制
            ratio = Math.ceil(options.outWidth / viewWidth);
        } else if (viewHeight > 0 && viewWidth <= 0) {
        	 // 限定高度，不限制宽度
            ratio = Math.ceil(options.outHeight / viewHeight);
        } else if (viewWidth > 0 && viewHeight > 0) {
        	// 高度和宽度都做了限制，这时候我们计算在这个限制内能容纳的最大的图片尺寸，不会使图片变形
            double _widthRatio = Math.ceil(options.outWidth / viewWidth);
            double _heightRatio = (double) Math.ceil(options.outHeight / viewHeight);
            ratio = _widthRatio > _heightRatio ? _widthRatio : _heightRatio;
        }
        if (ratio > 1) {
            options.inSampleSize = (int) ratio;
        }else{
        	//设置缩放比例
        	options.inSampleSize = computeScale(options, viewWidth, viewHeight);
        }
        //设置为false,解析Bitmap对象加入到内存中
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeFile(path, options);
	}
	
	
	/**
	 * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
	 * @param options
	 * @param width
	 * @param height
	 */
	private int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight){
		int inSampleSize = 1;
		if(viewWidth == 0 || viewWidth == 0){
			return inSampleSize;
		}
		int bitmapWidth = options.outWidth;
		int bitmapHeight = options.outHeight;
		
		//假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
		if(bitmapWidth > viewWidth || bitmapHeight > viewWidth){
			int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
			int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);
			
			//为了保证图片不缩放变形，我们取宽高比例最小的那个
			inSampleSize = widthScale < heightScale ? widthScale : heightScale;
		}
		return inSampleSize;
	}
	
	/**
	 * 获取选中的Item的路径
	 * @return
	 */
	public static List<String> getSelectItems(){
		List<String> list = new ArrayList<String>();
		for(Iterator<Entry<String, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext();){
			Entry<String, Boolean> entry = it.next();
			if(entry.getValue()){
				list.add(entry.getKey());
			}
		}
		
		return list;
	}
	
	
	
	/**
	 * 加载本地图片的回调接口
	 * 
	 */
	public interface NativeImageCallBack{
		/**
		 * 当子线程加载完了本地的图片，将Bitmap和图片路径回调在此方法中
		 * @param bitmap
		 * @param path
		 */
		public void onImageLoader(Bitmap bitmap, String path);
	}
}
