package com.programmer.jgallos.ma_i;

public class AcadRecords {
    private String title, desc, imageUrl, username, uid;


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

    public  String getUsername() {
        return username;
    }


    public String getTitle() {
        return title;
    }

    public String getDesc() { return desc; }

    public String getUid() {
        return uid;
    }
}
