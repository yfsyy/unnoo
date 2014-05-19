package com.wzhz.choseimagesfromsdcard.app;

import java.util.List;

import com.wzhz.choseimagesfromsdcard.app.adapter.SelectedAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

public class ShowSelectedImagesActivity extends Activity {
    private GridView selectedImagesGridView;
    private SelectedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_selected_images_activity);
        selectedImagesGridView = (GridView) findViewById(R.id.selectedimgsgridview);
        final List<String> selectedPath = getIntent().getStringArrayListExtra("selectedpath");
        adapter = new SelectedAdapter(ShowSelectedImagesActivity.this, selectedPath, selectedImagesGridView);
        selectedImagesGridView.setAdapter(adapter);
        selectedImagesGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (position == selectedPath.size()) {
                    new PopupWindows(ShowSelectedImagesActivity.this, selectedImagesGridView);
                }
            }

        });
    }

    class PopupWindows extends PopupWindow {
        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.popupwindows, null);
            setWidth(LayoutParams.MATCH_PARENT);
            setHeight(LayoutParams.MATCH_PARENT);
            //setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button fromBucket = (Button) view.findViewById(R.id.item_popupwindows_Photo);
            Button cancle = (Button) view.findViewById(R.id.item_popupwindows_cancel);
            fromBucket.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ShowSelectedImagesActivity.this,
                            MainActivity.class);
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
