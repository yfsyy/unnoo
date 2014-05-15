package com.example.myapplication.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by unnoo on 2014/5/14.
 */
public class ShowAllDirectory extends Activity {
    ListView allImgsDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showallimgs);
        findViews();
    }

    private void findViews() {
        allImgsDir=(ListView)findViewById(R.id.allImgsDirs);
    }
}
