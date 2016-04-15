package com.example.riane.hanbaoreader_app.modle;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Riane on 2016/4/14.
 */
@DatabaseTable(tableName = "tb_booktag")
public class BookTag {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "bookPath")
    private String bookPath;
    @DatabaseField(columnName = "bookTag")
    private String bookTag;

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public String getBookTag() {
        return bookTag;
    }

    public void setBookTag(String bookTag) {
        this.bookTag = bookTag;
    }
}
