package com.droidbots.areatravelogue.deals;

public class Guide {
    private String name;
    private int contact, id, rating;
    private String lat, lng, imgUrl;

    public Guide() {
    }

    public Guide(int id, String name, String imgUrl, int contact, String lat, String lng) {
        this.name = name;
        this.contact = contact;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
