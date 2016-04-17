package com.example.riane.hanbaoreader_app.cache;

import android.content.Context;

import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.BookMark;
import com.example.riane.hanbaoreader_app.modle.BookTag;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Riane on 2016/4/14.
 */
public class BookTagDao {
    private Dao<BookTag, Integer> bookTagDaoOpe;
    private DatabaseHelper helper;

    public BookTagDao(Context context){
        try {
            helper = DatabaseHelper.getHelper(context);
            bookTagDaoOpe = helper.getDao(BookTag.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //添加书标签
    public void add(BookTag bookTag){
        try {
            bookTagDaoOpe.create(bookTag);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查询所有标签
    public List<BookTag> getAll(){
        try {
            return bookTagDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //查询一本书标签
    public BookTag getBookTag(String bookPath){
        List<BookTag> bookTagList = null;
        BookTag bookTag = null;
        try {
            //bookList = bookDaoOpe.queryBuilder().where().eq("name",name).query();
            bookTagList = bookTagDaoOpe.queryBuilder().where().eq("bookPath",bookPath).query();
            if(bookTagList != null && bookTagList.size() >  0){
                bookTag = bookTagList.get(0);
                return bookTag;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //更新一本书标签
    public void update(BookTag bookTag){
        try {
            bookTagDaoOpe.update(bookTag);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
