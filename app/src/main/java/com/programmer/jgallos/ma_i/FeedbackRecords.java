package com.programmer.jgallos.ma_i;

import android.util.Log;


public class FeedbackRecords {
    private String level, desc, status, username, uid;
    private static final String TAG = FeedbackRecords.class.getSimpleName();

    public FeedbackRecords(String level, String desc, String status, String username, String uid) {
        this.level = level;
        this.desc = desc;
        this.status = status;
        this.username = username;
        this.uid = uid;

    }

    public FeedbackRecords() {

    }

    public void setStatus(String status) {
        this.status=status;
    }
    public  String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setUid(String uid) {
        Log.d(TAG,"Set uid = " + uid);

        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public String getLevel() {
        return level;
    }

    public String getDesc() {
        Log.d(TAG,desc);
        return desc;
    }

    public String getUid() {
        return uid;
    }
}
