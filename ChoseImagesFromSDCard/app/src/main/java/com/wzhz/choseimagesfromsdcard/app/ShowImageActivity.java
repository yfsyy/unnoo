package com.wzhz.choseimagesfromsdcard.app;

import java.util.ArrayList;
import java.util.List;

import com.wzhz.choseimagesfromsdcard.app.adapter.ChildAdapter;
import com.wzhz.choseimagesfromsdcard.app.other.NativeImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowImageActivity extends Activity {
    private GridView mGridView;
    private List<String> list; //得到Intent存储的图片路径
    private String dirName;
    private ChildAdapter adapter;
    private TextView dirNameText; //显示目录名
    private Button complete; //完成
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                Toast.makeText(ShowImageActivity.this, "最多只能选择9张图片", Toast.LENGTH_LONG).show();
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_activity);

        list = getIntent().getStringArrayListExtra("data");
        dirName = getIntent().getStringExtra("dirName");
        findViews();
        adapter = new ChildAdapter(this, list, mGridView, handler);
        mGridView.setAdapter(adapter);
        complete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent it = new Intent(ShowImageActivity.this, ShowSelectedImagesActivity.class);
                List<String> lt = NativeImageLoader.getSelectItems();
                it.putStringArrayListExtra("selectedpath", (ArrayList) lt);
                ShowImageActivity.this.startActivity(it);
            }
        });
    }

    private void findViews() {
        mGridView = (GridView) findViewById(R.id.child_grid);
        dirNameText = (TextView) findViewById(R.id.dirTitle);
        complete = (Button) findViewById(R.id.complete);
        dirNameText.setText(dirName);
    }



/*
    @Override
	public void onBackPressed() {
		Toast.makeText(this, "选中" + adapter.getSelectItems().size() + " item", Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}
	*/


}
