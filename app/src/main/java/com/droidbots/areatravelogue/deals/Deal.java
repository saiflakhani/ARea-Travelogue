package com.droidbots.areatravelogue.deals;

public class Deal {
    private int countRedeemed, id;
    private String title, description;

    public Deal() {
    }

    public Deal(int id, String title, String description, int countRedeemed) {
        this.countRedeemed = countRedeemed;
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getCountRedeemed() {
        return countRedeemed;
    }

    public void setCountRedeemed(int countRedeemed) {
        this.countRedeemed = countRedeemed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
