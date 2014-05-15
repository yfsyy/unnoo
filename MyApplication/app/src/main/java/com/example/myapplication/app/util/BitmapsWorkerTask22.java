package com.example.myapplication.app.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by unnoo on 2014/5/15.
 */
public class BitmapsWorkerTask22 extends AsyncTask<String, Void, List<Bitmap>> {
    // 在后台加载图片。
    List<Bitmap> lt = null;
    ImagesManager manager=ImagesManager.getInstance();
    @Override
    protected List<Bitmap> doInBackground(String... params) {
        for (int i = 0, j = params.length; i < j; i++) {
            final Bitmap bitmap = manager.decodeSampledBitmapFromResource(
                    params[i], Const.REQWIDTH, Const.REQHEIGHT);
            manager.addBitmapToMemoryCache(params[i], bitmap);
            lt.add(bitmap);
        }
        return lt;
    }
}

