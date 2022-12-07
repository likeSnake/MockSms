package com.jcl.test2.pojo;

public class MsgInfo {
    public static final int TYPE_RECEIVED = 1; //收到的消息
    public static final int TYPE_SENT = 2; //发送消息
    private int id;
    private String address;
    private String body;
    private int date;
    private int type;
    private String fName;
    private String YTime;
    private int thread_id;
    public String getfName() {
        return fName;
    }

    public MsgInfo(int id, String address, String body, int date, int type, String fName,String YTime,int thread_id) {
        this.id = id;
        this.address = address;
        this.body = body;
        this.date = date;
        this.type = type;
        this.fName = fName;
        this.YTime = YTime;
        this.thread_id = thread_id;
    }

    public int getThread_id(){
        return thread_id;
    }
    public int getId() {
        return id;
    }
    public String getAddress() {
        return address;
    }

    public String getBody() {
        return body;
    }
    public int getDate() {
        return date;
    }
    public int getType() {
        return type;
    }
    public String getYTime(){return YTime;}
}
