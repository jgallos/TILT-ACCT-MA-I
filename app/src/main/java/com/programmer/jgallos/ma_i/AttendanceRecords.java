package com.programmer.jgallos.ma_i;

import android.util.Log;

public class AttendanceRecords {
    private String date, signin, signout, uid;
    private static final String TAG = AttendanceRecords.class.getSimpleName();

    public AttendanceRecords(String date, String signin, String signout, String uid) {
        this.date = date;
        this.signin = signin;
        this.signout = signout;
        this.uid = uid;
    }

    public AttendanceRecords() {

    }

    public void setDate(String date) {
        this.date=date;
        Log.d(TAG,"Set Date = " + date);
    }
        /*public  String getUsername() {
            return username;
        }*/

        /*public void setUsername(String username) {
            this.username = username;
        }*/

    public void setSignin(String signin) {
        Log.d(TAG,"Set signin_time = " + signin);

        this.signin = signin;
    }

    public void setSignout(String signout) {
        this.signout = signout;
    }
    public void setUid(String uid) {
        Log.d(TAG,"Set uid = " + uid);

        this.uid = uid;
    }

    public String getDate() {
        Log.d(TAG,"Pulled Date = " + date);
        return date;
    }
    public String getSignin() {
        Log.d(TAG,"Pulled Signin Time = " + signin);
        return signin;
    }
    public String getSignout() {
        Log.d(TAG,"Pulled Signout Time = " + signout);
        return signout;
    }
    public String getUid() {
        Log.d(TAG,"Pulled Uid = " + uid);

        return uid;
    }
}

