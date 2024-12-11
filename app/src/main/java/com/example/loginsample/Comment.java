package com.example.loginsample;

public class Comment {
    public int ComTex;
    public int ComFec;
    private String username;
    private String text;
    private int rating;

    public Comment(String username, String text, int rating) {
        this.username = username;
        this.text = text;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }
}

