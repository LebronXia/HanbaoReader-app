package com.example.riane.hanbaoreader_app.cache;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.riane.hanbaoreader_app.modle.Book;

import java.util.Date;
import java.util.List;

/**
 * Created by Xiamu on 2016/3/25.
 */
public class OrmLiteDbTest extends AndroidTestCase {

//    public void testAddArticle(){
//        Book book = new Book("白夜行","dfdfsfdds",new Date(),1,"1%");
//        new BookDao(getContext()).add(book);
//    }

    public void testGetArtivleById(){
        List<Book> booklist = new BookDao(getContext()).getBook("世界上的另一个你.txt");
        Log.d("OrmLiteDbTest", "testGetArtivleById: " + booklist.get(0).getFilePath());
    }

}
