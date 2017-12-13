package cn.androidstudy.course12;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/12/9.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "person.db"; //数据库名称
    private static final int version = 1; //数据库版本
    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tb_person("+
                "_id       integer primary key autoincrement," +
                "name      varchar(20) not null," +
                "age       Integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
