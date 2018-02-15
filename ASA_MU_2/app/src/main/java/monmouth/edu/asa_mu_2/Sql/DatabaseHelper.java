package monmouth.edu.asa_mu_2.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by chengli on 2017/11/5.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {

        //context :上下文   ， name：数据库文件的名称    factory：用来创建cursor对象，默认为null
        //version:数据库的版本号，从1开始，如果发生改变，onUpgrade方法将会调用,4.0之后只能升不能将
        super(context, "meeting.db", null,2);
    }

    //oncreate方法是数据库第一次创建的时候会被调用;  特别适合做表结构的初始化,需要执行sql语句；SQLiteDatabase db可以用来执行sql语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        //通过SQLiteDatabase执行一个创建表的sql语句
        db.execSQL("create table imeet(id integer,date varchar(60),time varchar(60),session varcar(80))");
    }

    //onUpgrade数据库版本号发生改变时才会执行； 特别适合做表结构的修改
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("create table imeet(id integer primary key autoincrement,date varchar(20),time varchar(20),session varcar(60))");
    }
}
