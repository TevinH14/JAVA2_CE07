package com.example.hamiltontevin_ce07.model;

import java.io.Serializable;

public class RedditPost implements Serializable {
    private final String mUrl;
    private final String mBody;
    private final byte[] mImage;

    public RedditPost(String mUrl, String mBody, byte[] mImage) {
        this.mUrl = mUrl;
        this.mBody = mBody;
        this.mImage = mImage;
    }
}
