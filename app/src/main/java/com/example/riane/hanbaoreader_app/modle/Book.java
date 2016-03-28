package com.example.riane.hanbaoreader_app.modle;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;
/**
 * Created by Xiamu on 2016/3/25.
 */
@DatabaseTable(tableName = "tb_book")
public class Book {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "name")
    private String name;        //书名
    @DatabaseField(columnName = "password")
    private String password;    //密码
    @DatabaseField(columnName = "file_path")
    private String filePath;    //文件路径
    @DatabaseField(columnName = "last_readtime")
    private Date lastReadTime;  //最后阅读时间
    @DatabaseField(columnName = "begin")
    private int begin;          //从哪里开始看书
    @DatabaseField(columnName = "progress")
    private String progress;    //进度比例

    public Book(){

    };

    public Book(String name, String filePath, Date lastReadTime, int begin, String progress) {
        this.name = name;
        this.filePath = filePath;
        this.lastReadTime = lastReadTime;
        this.begin = begin;
        this.progress = progress;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Date lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", filePath='" + filePath + '\'' +
                ", lastReadTime=" + lastReadTime +
                ", begin=" + begin +
                ", progress='" + progress + '\'' +
                '}';
    }
}
