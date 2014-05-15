package com.example.myapplication.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 相册目录对象
 * Created by unnoo on 2014/5/15.
 */
public class ImageDir implements Serializable{
    private List<ImageItem> imageList;
    private String dirName;
    private int imgCount;

    public List<ImageItem> getImageList() {
        return imageList;
    }

    public String getDirName() {
        return dirName;
    }

    public int getImgCount() {
        return imgCount;
    }

    public void setImageList(List<ImageItem> imageList) {
        this.imageList = imageList;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public void setImgCount(int imgCount) {
        this.imgCount = imgCount;
    }
}
