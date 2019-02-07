package com.programmer.jgallos.ma_i;

import android.util.Log;

public class AcadRecords {
    private String title, desc, imageUrl, username, uid;
    private static final String TAG = AcadRecords.class.getSimpleName();

    public AcadRecords(String title, String desc, String imageUrl, String username, String uid) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.username = username;
        this.uid = uid;

    }

    public AcadRecords() {

    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl=imageUrl;
    }
    public  String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        Log.d(TAG,desc);
        return desc;
    }

    public String getUid() {
        return uid;
    }
}
