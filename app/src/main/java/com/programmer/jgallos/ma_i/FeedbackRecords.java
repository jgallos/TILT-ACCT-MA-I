/*
TILT-ACCT - Transparency in Learning and Teaching through Android and Cloud Computing Technologies
Programmer: Joseph M. Gallos
Date: May 2019
Software License: GNU-General Public License
*/
package com.programmer.jgallos.ma_i;


public class FeedbackRecords {
    private String level, desc, status, username, uid;

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
    public void setUsername(String username) {
        this.username = username;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setUid(String uid) { this.uid = uid; }


    public String getStatus() {
        return status;
    }

    public  String getUsername() {
        return username;
    }

    public String getLevel() {
        return level;
    }

    public String getDesc() { return desc; }

    public String getUid() {
        return uid;
    }
}
