package com.wzhz.choseimagesfromsdcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.wzhz.choseimagesfromsdcard.NativeImageLoader.NativeImageCallBack;


public class ChildAdapter extends BaseAdapter {
	/**
	 * 用来存储图片的选中情况
	 */
	public  HashMap<String, Boolean> mSelectMap = new HashMap<String, Boolean>();
	private GridView mGridView;
	private List<String> list;//保存选中目录下的图片地址
	protected LayoutInflater mInflater;
	private int totalSelected;//选中的总数
	private Handler handler;
	public ChildAdapter(Context context, List<String> list, GridView mGridView,Handler handler) {
		this.list = list;
		this.mGridView = mGridView;
		this.handler=handler;
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
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.grid_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
			/*
			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
				
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});*/
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		viewHolder.mImageView.setTag(path);
		viewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//如果是未选中的CheckBox,则添加动画
				if(!mSelectMap.containsKey(path) || !mSelectMap.get(path)){
					if(totalSelected<9){
						addAnimation(viewHolder.mCheckBox);
					}
				}
				if(totalSelected<9){
					if(isChecked){
						totalSelected++;
						mSelectMap.put(path, isChecked);
					}else{
						totalSelected--;
						mSelectMap.remove(path);
					}
				}else{
					if(isChecked){
						viewHolder.mCheckBox.setChecked(!isChecked);
						handler.sendEmptyMessage(-1);
					}else{
						viewHolder.mCheckBox.setChecked(isChecked);
						totalSelected--;
						mSelectMap.remove(path);
					}
				}
			}
		});
		
		viewHolder.mCheckBox.setChecked(mSelectMap.containsKey(path) ? mSelectMap.get(path) : false);
		
		//利用NativeImageLoader类加载本地图片
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path,viewHolder.mImageView.getWidth(),viewHolder.mImageView.getHeight(), new NativeImageCallBack() {
			
			@Override
			public void onImageLoader(Bitmap bitmap, String path) {
				ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
				if(bitmap != null && mImageView != null){
					mImageView.setImageBitmap(bitmap);
				}
			}
		});
		
		if(bitmap != null){
			viewHolder.mImageView.setImageBitmap(bitmap);
		}else{
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		
		return convertView;
	}
	
	/**
	 * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画 
	 * @param view
	 */
	private void addAnimation(View view){
		float [] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), 
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
				set.setDuration(150);
		set.start();
	}
	
	
	/**
	 * 获取选中的Item的路径
	 * @return
	 */
	public List<String> getSelectItems(){
		List<String> list = new ArrayList<String>();
		for(Iterator<Entry<String, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<String, Boolean> entry = it.next();
			if(entry.getValue()){
				list.add(entry.getKey());
			}
		}
		
		return list;
	}
	
	
	public class ViewHolder{
		public ImageView mImageView;
		public CheckBox mCheckBox;
	}



}
