/*
TILT-ACCT - Transparency in Learning and Teaching through Android and Cloud Computing Technologies
Programmer: Joseph M. Gallos
Date: May 2019
Software License: GNU-General Public License
*/
package com.programmer.jgallos.ma_i;

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
