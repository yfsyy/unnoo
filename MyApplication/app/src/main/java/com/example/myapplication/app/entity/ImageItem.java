package com.example.myapplication.app.entity;

import java.io.Serializable;

/**
 * 一个图片对象
 * Created by unnoo on 2014/5/15.
 */
public class ImageItem implements Serializable {
    private String imageId;
    private String imagePath;
    private boolean isSelected=false;

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getImageId() {
        return imageId;
    }
}
