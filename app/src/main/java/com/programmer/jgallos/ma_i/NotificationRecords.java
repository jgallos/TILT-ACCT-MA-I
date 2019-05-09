package com.programmer.jgallos.ma_i;

//import android.app.Notification;

public class NotificationRecords {
    private String ndate, ntime, nnotif;

    public NotificationRecords(String ndate, String ntime, String nnotif) {
        this.nnotif = nnotif;
        this.ndate = ndate;
        this.ntime = ntime;
    }

    public NotificationRecords() {

    }


    public void setNdate(String ndate) {
        this.ndate=ndate;
    }
    public void setNtime(String ntime) {
        this.ntime=ntime;
    }
    public void setNnotif(String nnotif) {
        this.nnotif=nnotif;
    }

    public String getNdate() {
        return ndate;
    }
    public String getNtime() {
        return ntime;
    }
    public String getNnotif() {
        return nnotif;
    }
}