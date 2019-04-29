package com.android.finalproject;

import java.io.Serializable;
import java.util.UUID;

public class Blog implements Serializable {
    private String id;
    private String title;
    private String imageUrl;
    private String blogContents;
    private String creatorEmail;
    private boolean publicBlog;

    public Blog() {
        id = UUID.randomUUID().toString();
    }

    public Blog(String creatorEmail) {
        this(Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, creatorEmail, false);
    }

    public Blog(String title, String imageUrl, String blogContents, String creatorEmail, boolean publicBlog) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.imageUrl = imageUrl;
        this.blogContents = blogContents;
        this.creatorEmail = creatorEmail;
        this.publicBlog = publicBlog;
    }

    public String getId() {
        return id;
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
