package com.example.riane.hanbaoreader_app.cache;

import android.content.Context;

import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.BookMark;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Riane on 2016/4/10.
 */
public class BookMarkDao {

    private Dao<BookMark, Integer> bookMarkDaoOpe;
    private DatabaseHelper helper;

    public BookMarkDao(Context context){
        try {
            helper = DatabaseHelper.getHelper(context);
            bookMarkDaoOpe = helper.getDao(BookMark.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //添加一本书签
    public void add(BookMark bookMark){
        try {
            bookMarkDaoOpe.create(bookMark);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查询所有书签
    public List<BookMark> getAll(){
        try {
            return bookMarkDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //查询一本书
    public BookMark getBookMark(int begin){
        List<BookMark> bookMarkList = null;
        BookMark bookMark = null;
        try {
            //bookList = bookDaoOpe.queryBuilder().where().eq("name",name).query();
            bookMarkList = bookMarkDaoOpe.queryBuilder().where().eq("begin",begin).query();
            if(bookMarkList != null && bookMarkList.size() >  0){
                bookMark = bookMarkList.get(0);
                return bookMark;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //删除一个书签
    public void delete(BookMark bookMark){
        try {
            bookMarkDaoOpe.delete(bookMark);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
