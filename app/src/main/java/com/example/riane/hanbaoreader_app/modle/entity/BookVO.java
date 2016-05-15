package com.example.riane.hanbaoreader_app.modle.entity;

import java.io.Serializable;

/**
 * Created by Riane on 2016/4/15.
 */
public class BookVO implements Serializable {
    private int id;
    private String name;
    private String author;
    private String isbn;
    private String publish;
    private String picture;
    private String bookResource;
    private String bookDescription;
    private String bookList;

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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookList() {
        return bookList;
    }

    public void setBookList(String bookList) {
        this.bookList = bookList;
    }
}
