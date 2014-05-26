package com.wzhz.choseimagesfromsdcard.app;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.wzhz.choseimagesfromsdcard.app.adapter.MyPageAdapter;
import com.wzhz.choseimagesfromsdcard.app.other.HackyViewPager;
import com.wzhz.choseimagesfromsdcard.app.other.NativeImageLoader;

public class ShowPrimaryImages extends Activity {
    private HackyViewPager imageViewPager;
    private int currentPosition;// 当前选中图片的位置
    private List<String> selectedImgsPath;// 所有选中的图片路径
    private MyPageAdapter adapter;
    private int mScreenWidth;
    private int mScreenHeight;
    private ActionBar bar;
    /**
     * 返回剩下的图片
     *
     * @return
     */
    public List<String> getConfirmImgs() {
        return selectedImgsPath;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.activity_photo);
        //  以下四行代码得到屏幕宽高
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;

        selectedImgsPath = getIntent().getStringArrayListExtra("selectedImgsPath");
        currentPosition = getIntent().getIntExtra("currentPosition", 0);
        findViews();

        adapter = new MyPageAdapter(ShowPrimaryImages.this, selectedImgsPath, mScreenWidth,mScreenHeight);
        imageViewPager.setAdapter(adapter);
        imageViewPager.setCurrentItem(currentPosition);
        imageViewPager.setOnPageChangeListener(new ViewPagerChangeListener());
        imageViewPager.setEnabled(false);

        initActionBar();

    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initActionBar(){
        bar=this.getActionBar();
        //允许应用图标向上导航
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(true);
        bar.setTitle(currentPosition+1+"/"+selectedImgsPath.size());
    }
    class ViewPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            bar.setTitle(currentPosition+1+"/"+selectedImgsPath.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void findViews() {
        imageViewPager = (HackyViewPager) findViewById(R.id.viewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_primary_images, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return  true;
            case R.id.delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ShowPrimaryImages.this)
                        .setMessage("要删除这张照片吗？")
                        .setTitle("提示")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String deletePath=selectedImgsPath.get(currentPosition);
                                NativeImageLoader.mSelectMap.remove(deletePath);
                                //Log.e("aa","选择的图片数量"+NativeImageLoader.mSelectMap.size());
                                selectedImgsPath.remove(currentPosition);
                                adapter = new MyPageAdapter(ShowPrimaryImages.this, selectedImgsPath, mScreenWidth, mScreenHeight);
                                imageViewPager.setAdapter(adapter);

                                Intent data = new Intent();
                                data.putStringArrayListExtra("z_result_data", (ArrayList) selectedImgsPath);
                                ShowPrimaryImages.this.setResult(RESULT_OK, data);

                                if (selectedImgsPath.size() != 0) {
                                    if(currentPosition<selectedImgsPath.size()){
                                        imageViewPager.setCurrentItem(currentPosition);
                                    }else{
                                        currentPosition=currentPosition-1;
                                    }
                                    bar.setTitle(currentPosition+1+"/"+selectedImgsPath.size());
                                } else {
                                    /**
                                     * 没有图片了，则返回添加界面
                                     */

                                    finish();
                                }
                            }
                        });
                builder.create().show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
