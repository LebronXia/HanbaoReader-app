package com.example.riane.hanbaoreader_app.cache;

import android.content.Context;

import com.example.riane.hanbaoreader_app.modle.Book;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Xiamu on 2016/3/25.
 */
public class BookDao {
    private Context mContext;
    private Dao<Book, Integer> bookDaoOpe;
    private DatabaseHelper mHelper;

    public BookDao(Context context){
        this.mContext = context;
        try {
            mHelper = DatabaseHelper.getHelper(context);
            bookDaoOpe = mHelper.getDao(Book.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //增加一本书
    public void add(Book book){
        try {
            bookDaoOpe.create(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查询所有图书
    public List<Book> getAll(){
        List<Book> bookList = null;
        try {
            bookList = bookDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }
    //删除一本书
    public void delete(int id){
        try {
            bookDaoOpe.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
