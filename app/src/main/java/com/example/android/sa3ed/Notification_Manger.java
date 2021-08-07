package com.example.android.sa3ed;

public class Notification_Manger {
    private int img;
    private String name;
    private String status;
    private String you_for;
    private String item;
    private String quatntity;

    public Notification_Manger(int img, String name, String status, String you_for,String quatntity, String item) {
        this.img = img;
        this.name = name;
        this.status = status;
        this.you_for = you_for;
        this.item = item;
        this.quatntity = quatntity;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYou_for() {
        return you_for;
    }

    public void setYou_for(String you_for) {
        this.you_for = you_for;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuatntity() {
        return quatntity;
    }

    public void setQuatntity(String quatntity) {
        this.quatntity = quatntity;
    }
}
