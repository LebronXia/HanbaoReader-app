package com.example.riane.hanbaoreader_app.modle;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Riane on 2016/4/10.
 */
@DatabaseTable(tableName = "tb_bookmark")
public class BookMark {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "bookpath")
    private String bookpath;   //书的路径
    @DatabaseField(columnName = "begin")
    private int begin;         //书的起始位置
    @DatabaseField(columnName = "textword")
    private String textword;    //书的内容
    @DatabaseField(columnName = "time")
    private String time;

    private BookMark(){};

    public BookMark(String bookpath, int begin, String textword, String time) {
        this.bookpath = bookpath;
        this.begin = begin;
        this.textword = textword;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookpath() {
        return bookpath;
    }

    public void setBookpath(String bookpath) {
        this.bookpath = bookpath;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public String getTextword() {
        return textword;
    }

    public void setTextword(String textword) {
        this.textword = textword;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
