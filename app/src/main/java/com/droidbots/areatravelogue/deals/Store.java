package com.droidbots.areatravelogue.deals;

import java.util.List;

public class Store {
    private int id;
    private String name, addr;
    private List<Deal> deals;
    private List<Review> reviews;

    public Store() {
    }

    public Store(int id, String name, String addr, List<Deal> deals, List<Review> reviews) {
        this.id = id;
        this.name = name;
        this.addr = addr;
        this.deals = deals;
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
