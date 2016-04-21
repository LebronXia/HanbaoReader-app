package com.example.riane.hanbaoreader_app.modle.entity;

/**
 * Created by Riane on 2016/4/15.
 */
public class BookVO {
    private int id;
    private String name;
    private String author;
    private String publish;
    private String picture;
    private String bookResource;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBookResource() {
        return bookResource;
    }

    public void setBookResource(String bookResource) {
        this.bookResource = bookResource;
    }
}
