package com.example.android.sa3ed;

public class card {
    private Integer cardimage;
    private String name;
    private String time;
    private String destrict;
    private String status;
    private String quantity;
    private String item;
    private int id;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public card(Integer cardimage) {
        this.cardimage = cardimage;
    }

    public Integer getCardimage() {
        return cardimage;
    }

    public void setCardimage(Integer cardimage) {
        this.cardimage = cardimage;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDestrict() {
        return destrict;
    }

    public void setDestrict(String destrict) {
        this.destrict = destrict;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
