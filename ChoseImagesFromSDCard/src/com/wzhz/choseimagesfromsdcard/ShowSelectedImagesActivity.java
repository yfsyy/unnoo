package com.wzhz.choseimagesfromsdcard;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class ShowSelectedImagesActivity extends Activity{
	private GridView selectedImagesGridView;
	private SelectedAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_selected_images_activity);
		selectedImagesGridView=(GridView) findViewById(R.id.selectedimgsgridview);
		List<String> selectedPath=getIntent().getStringArrayListExtra("selectedpath");
		adapter=new SelectedAdapter(ShowSelectedImagesActivity.this, selectedPath, selectedImagesGridView);
		selectedImagesGridView.setAdapter(adapter);
	} 
}
