package com.programmer.jgallos.ma_i;

public class ReplyRecords {
    private String date, time, reply;

    public ReplyRecords(String date, String time, String reply) {
        this.reply = reply;
        this.date = date;
        this.time = time;
    }

    public ReplyRecords() {

    }


    public void setDate(String date) {
        this.date=date;
    }
    public void setTime(String time) {
        this.time=time;
    }
    public void setReply(String reply) {
        this.reply=reply;
    }

    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getReply() {
        return reply;
    }

}