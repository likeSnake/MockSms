package com.jcl.test2.pojo;

public class ContactsInfo {
    private String name;
    private String phone;
    private String fName;

    public ContactsInfo(String name, String phone,String fName) {
        this.name = name;
        this.phone = phone;
        this.fName = fName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getfName() {
        return fName;
    }
}
