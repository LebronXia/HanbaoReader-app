package com.example.riane.hanbaoreader_app.cache;

import android.test.AndroidTestCase;

import com.example.riane.hanbaoreader_app.modle.Book;

import java.util.Date;

/**
 * Created by Xiamu on 2016/3/25.
 */
public class OrmLiteDbTest extends AndroidTestCase {

    public void testAddArticle(){
        Book book = new Book("白夜行","dfdfsfdds",new Date(),1,"1%");
        new BookDao(getContext()).add(book);
    }

}
