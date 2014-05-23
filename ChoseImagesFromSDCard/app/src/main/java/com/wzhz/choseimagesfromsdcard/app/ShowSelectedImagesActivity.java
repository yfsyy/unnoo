package com.wzhz.choseimagesfromsdcard.app;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.wzhz.choseimagesfromsdcard.app.adapter.SelectedAdapter;

public class ShowSelectedImagesActivity extends Activity {
    /**
     * 进入浏览选择的图片界面
     */
    private static final int REQ_VIEW_IMAGE = 0x110;
    /**
     *
     */
    private List<String> mSelectImgs;

    private GridView selectedImagesGridView;
    private SelectedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_selected_images_activity);
        selectedImagesGridView = (GridView) findViewById(R.id.selectedimgsgridview);
        mSelectImgs = getIntent().getStringArrayListExtra("selectedpath");

        adapter = new SelectedAdapter(ShowSelectedImagesActivity.this, mSelectImgs, selectedImagesGridView);

        selectedImagesGridView.setAdapter(adapter);
        selectedImagesGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mSelectImgs.size()) {
                    new PopupWindows(ShowSelectedImagesActivity.this, selectedImagesGridView);
                } else {
                    Intent intent = new Intent(ShowSelectedImagesActivity.this, ShowPrimaryImages.class);
                    intent.putCharSequenceArrayListExtra("selectedImgsPath", (ArrayList) mSelectImgs);
                    intent.putExtra("currentPosition", position);
                    startActivityForResult(intent, REQ_VIEW_IMAGE);

                }
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ShowSelectedImagesActivity.this)
                    .setMessage("放弃本次编辑？")
                    .setTitle("小密圈")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ExitApplication.getInstance().exit();
                            System.exit(0);
                        }
                    });
            builder.create().show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQ_VIEW_IMAGE) {
            mSelectImgs = data.getStringArrayListExtra("z_result_data");
            adapter = new SelectedAdapter(ShowSelectedImagesActivity.this, mSelectImgs, selectedImagesGridView);
            selectedImagesGridView.setAdapter(adapter);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class PopupWindows extends PopupWindow {
        public PopupWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.popupwindows, null);
            setWidth(LayoutParams.MATCH_PARENT);
            setHeight(LayoutParams.MATCH_PARENT);
            // setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button fromBucket = (Button) view.findViewById(R.id.item_popupwindows_Photo);
            Button cancle = (Button) view.findViewById(R.id.item_popupwindows_cancel);
            fromBucket.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ShowSelectedImagesActivity.this, MainActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });
            cancle.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }
}
