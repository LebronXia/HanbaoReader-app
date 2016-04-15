package com.example.riane.hanbaoreader_app.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.riane.hanbaoreader_app.modle.Book;
import com.example.riane.hanbaoreader_app.modle.BookMark;
import com.example.riane.hanbaoreader_app.modle.BookTag;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xiamu on 2016/3/25.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String TABLE_NAME = "table_book.db";
    //private Dao bookdao = null;
    private Map<String, Dao> daos = new HashMap<String, Dao>();
    private DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 4);
    }

    //创建BOOK表
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource,Book.class);
            TableUtils.createTable(connectionSource, BookMark.class);
            TableUtils.createTable(connectionSource, BookTag.class);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    //更新Book表
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try{
            TableUtils.dropTable(connectionSource,Book.class,true);
            TableUtils.dropTable(connectionSource,BookMark.class,true);
            TableUtils.dropTable(connectionSource,BookTag.class,true);
            onCreate(database, connectionSource);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    /*
    * 单例获得Helper
    * */
    public static DatabaseHelper getHelper(Context context){
        context = context.getApplicationContext();
        if (instance == null){
            synchronized (DatabaseHelper.class){
                if (instance == null){
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    //获得userDao
    public synchronized  Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
//        if (bookdao == null){
//            bookdao = super.getDao(clazz);
//        }
        if (daos.containsKey(className)){
            dao = daos.get(className);
        }
        if (dao == null){
            dao = super.getDao(clazz);
            daos.put(className,dao);
        }
        return dao;
    }

    //释放资源
    @Override
    public void close() {
        super.close();
        //bookdao = null;
        for (String key : daos.keySet()){
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
