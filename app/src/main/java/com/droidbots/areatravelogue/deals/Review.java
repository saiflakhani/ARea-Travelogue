package com.droidbots.areatravelogue.deals;

public class Review {
    private int id, rating;
    private String title, body, username;

    public Review() {
    }

    public Review(int id, int rating, String title, String body, String username) {
        this.id = id;
        this.rating = rating;
        this.title = title;
        this.body = body;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
