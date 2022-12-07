package com.jcl.test2.pojo;

public class MainInfo {
    private int id;
    private String address;
    private String body;
    private String date;
    private String fName;
    private String name;

    public MainInfo(String address, String body, String date, String fName,String name,int id) {
        this.address = address;
        this.body = body;
        this.date = date;
        this.fName = fName;
        this.name = name;
        this.id = id;
    }

    public int getId(){
        return id;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
