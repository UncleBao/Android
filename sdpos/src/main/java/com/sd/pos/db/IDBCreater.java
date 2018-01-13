package com.sd.pos.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * 建库脚本基类,建表,表修改,建视图等
 */
public interface IDBCreater {
    //创建数据库脚本,一般的,所有的脚本支持重复执行
    void CreateDatabase(SQLiteDatabase db);

    //修改数据库版本触发,一般的,CreateDatabase支持脚本重复执行了,不是特殊情况这里实现一个空方法就可以了
    void UpdateDatabase(SQLiteDatabase db, int oldVersion, int newversion);
}
