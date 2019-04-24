package com.android.finalproject;

import java.io.Serializable;

public class Blog implements Serializable {
    private String title;
    private String imageUrl;
    private String blogContents;
    private String creatorEmail;
    private boolean publicBlog;

    public Blog(String creatorEmail) {
        this(Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, creatorEmail, false);
    }

    public Blog(String title, String imageUrl, String blogContents, String creatorEmail, boolean publicBlog) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.blogContents = blogContents;
        this.creatorEmail = creatorEmail;
        this.publicBlog = publicBlog;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBlogContents() {
        return blogContents;
    }

    public void setBlogContents(String blogContents) {
        this.blogContents = blogContents;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public boolean isPublicBlog() {
        return publicBlog;
    }

    public void setPublicBlog(boolean publicBlog) {
        this.publicBlog = publicBlog;
    }
}
