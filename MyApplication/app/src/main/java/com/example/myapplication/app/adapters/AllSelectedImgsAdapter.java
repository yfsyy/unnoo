package com.example.myapplication.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.app.R;
import com.example.myapplication.app.entity.ImageDir;
import com.example.myapplication.app.entity.ImageItem;
import com.example.myapplication.app.util.ImagesManager;

import java.util.List;

/**
 * Created by unnoo on 2014/5/15.
 */
public class AllSelectedImgsAdapter extends BaseAdapter {
    //private Context context;
    private List<ImageDir> lt;
    private LayoutInflater inflater;
    public AllSelectedImgsAdapter(Context context, List<ImageDir> lt) {
       // this.context = context;
        this.lt = lt;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int count=0;
        if(lt!=null){
            count= lt.size();
        }
        return  count;
    }

    @Override
    public Object getItem(int i) {
        if(lt !=null){
            return lt.get(i);
        }
        return  null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       ViewHolder holder=null;
        if(view==null){
            view=inflater.inflate(R.layout.alldirs,null);
            holder=new ViewHolder();
            holder.imageView= (ImageView) view.findViewById(R.id.dirImage);
            holder.dirName= (TextView) view.findViewById(R.id.dirName);
            holder.imgCount= (TextView) view.findViewById(R.id.dirCount);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        ImageDir dirs=lt.get(i);
        holder.imgCount.setText("" + dirs.getImgCount());
        holder.dirName.setText(dirs.getDirName());
        List<ImageItem> imgs=dirs.getImageList();
        if(imgs!=null&&imgs.size()>0){
            String imagePath=imgs.get(0).getImagePath();
            holder.imageView.setTag(imagePath);
            ImagesManager manager=ImagesManager.getInstance();
            manager.setImgBitmap(holder.imageView,imagePath);
        }else{
            holder.imageView.setImageBitmap(null);
        }
        return  view;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView dirName;
        TextView imgCount;
    }
}
