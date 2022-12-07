package com.jcl.test2.pojo;

import android.graphics.Bitmap;

public class AllInfo implements Comparable<AllInfo>{
    private int thread_id;
    private String body;
    private int date;
    private int type;
    private Bitmap bitmap;
    private String fName;
    private int id;

    public AllInfo(int thread_id, String body, int date, int type,Bitmap bitmap,String fName,int id) {
        this.thread_id = thread_id;
        this.body = body;
        this.date = date;
        this.type = type;
        this.bitmap = bitmap;
        this.fName = fName;
        this.id = id;
    }


    public AllInfo(){}

    public int getId(){
        return id;
    }
    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    @Override
    public int compareTo(AllInfo o) {
        return o.date - this.date;
    }
}
