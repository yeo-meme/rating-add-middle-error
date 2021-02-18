package com.nalazoocare.ratingaddt;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by nalazoo.yeomeme@gmail.com on 2020-05-29
 */
public class Rating {

    private String userId;
    private String userName;
    private double rating;
    private String text;
    private @ServerTimestamp
    Date timestamp;

    public Rating() {}

    public Rating(String user, double rating, String text) {
        this.userId = "meme";
        this.userName = "메메";
        if (TextUtils.isEmpty(this.userName)) {
            this.userName ="메메email";
        }

        this.rating = rating;
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
