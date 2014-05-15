package com.example.myapplication.app.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;

/**
 * Created by unnoo on 2014/5/15.
 */
public class BitmapWorkerTask11 extends AsyncTask<String, Void, Bitmap> {
    ImagesManager manager=ImagesManager.getInstance();
    @Override
    protected Bitmap doInBackground(String... params) {
        final Bitmap bitmap = ImagesManager.decodeSampledBitmapFromResource(
                params[0], Const.REQWIDTH, Const.REQHEIGHT);
        manager.addBitmapToMemoryCache(params[0], bitmap);
        return bitmap;
    }
}