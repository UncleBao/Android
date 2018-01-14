package com.sd.pos.dbhelp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sd.pos.db.SQLiteOpenHelperBase;
import com.sd.pos.db.IDBCreator;

/**
 * 数据库操作类,主要用法释放执行SQL和获取DataTable
 */
public class DBHelper extends SQLiteOpenHelperBase {

    static int DBVersion = 100;

    public DBHelper(Context context) {
        super(context, DBVersion);
    }

    @Override
    protected IDBCreator setDBCreator() {
        return new DBCreator();
    }
}


