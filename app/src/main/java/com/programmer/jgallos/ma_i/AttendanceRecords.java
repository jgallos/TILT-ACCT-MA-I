package com.programmer.jgallos.ma_i;

import android.util.Log;

public class AttendanceRecords {
    private String date, signin, signout, uid;

    public AttendanceRecords(String date, String signin, String signout, String uid) {
        this.date = date;
        this.signin = signin;
        this.signout = signout;
        this.uid = uid;
    }

    public AttendanceRecords() {

    }

    public void setDate(String date) { this.date=date; }
    public void setSignin(String signin) { this.signin = signin; }
    public void setSignout(String signout) {
        this.signout = signout;
    }
    public void setUid(String uid) { this.uid = uid; }

    public String getDate() { return date; }
    public String getSignin() { return signin; }
    public String getSignout() { return signout; }
    public String getUid() { return uid; }
}

