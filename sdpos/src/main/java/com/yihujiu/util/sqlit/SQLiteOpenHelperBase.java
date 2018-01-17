package com.yihujiu.util.sqlit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.yihujiu.util.table.DataRow;
import com.yihujiu.util.table.DataTable;

import java.util.ArrayList;

/**
 * 数据库基本操作类,数据库的创建,更新的操作都在这里进行
 */
public abstract class SQLiteOpenHelperBase extends SQLiteOpenHelper {

    static String name = "sdpos.db"; // 数据库名称
    static CursorFactory cursorFactory = null;
    protected Context context;

    public SQLiteOpenHelperBase(Context context, int version) {
        super(context, name, cursorFactory, version);
        this.context = context;
    }

    /**
     * 软件第一次安装的时候会调用,覆盖安装不会调用
     */
    public void onCreate(SQLiteDatabase db) {
        // 所有表的创建过程都在这里进行
        setDBCreator().CreateDatabase(db);
    }

    /**
     * 覆盖安装,当版本号version发生变化的时候,这个方法才会被调用,而且只执行一次
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        setDBCreator().CreateDatabase(db);
        setDBCreator().UpdateDatabase(db, oldVersion, newVersion);
    }

    /**
     * 每次成功打开数据库后首先被执行
     */
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db); // 每次成功打开数据库后首先被执行
    }

    public void finalize() {
        close();
    }

    // --------------------------创建数据库---------------------------------//

    /**
     * 设置创建数据库的类
     */
    protected abstract IDBCreator setDBCreator();

    // --------------------------sql方法---------------------------------//

    /**
     * 执行sql, 返回1表示成功, 返回其他表示执行失败, 返回的内容就是错误提示
     */
    public String execSQL(String sql) {
        System.out.println("==execSQL0==" + sql);
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL(sql);
            db.setTransactionSuccessful();
            return "1";
        } catch (Exception ex) {
            System.out.println(ex);
            showError("(execSQL0):" + ex.getMessage());
            return ex.getMessage();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 执行sql, 返回1表示成功, 返回其他表示执行失败, 返回的内容就是错误提示
     */
    public String execSQL(String sql, String[] param) {
        System.out.println("==execSQL2==" + sql);
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql, param);
            return "1";
        } catch (Exception ex) {
            System.out.println(ex);
            showError("(execSQL2):" + ex.getMessage());
            return ex.getMessage();
        } finally {
            db.close();
        }
    }

    /**
     * 执行sql, 返回1表示成功, 返回其他表示执行失败, 返回的内容就是错误提示
     */
    public String execSQL(ArrayList<String> sqlList) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            for (String sql : sqlList) {
                db.execSQL(sql);
            }
            db.setTransactionSuccessful();
            return "1";
        } catch (Exception ex) {
            System.out.println(ex);
            showError("(execSQL2):" + ex.getMessage());
            return ex.getMessage();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * SQL参数转换,防注入
     */
    public static String SPF(String str) {
        if (null != str) {
            String tmp = str.replace("'", "''");
            return "'" + tmp + "'";
        } else {
            return "null";
        }
    }

    /**
     * 通过传入的sql语句以及条件,获取DataTable
     *
     * @param sql
     * @param params
     */
    public DataTable getTableBySql(String sql, String[] params) {
        System.out.println("==getTableBySql==" + sql);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, params);

            DataTable table = new DataTable();
            table.setColumns(cursor.getColumnNames());

            ArrayList<String> row = new ArrayList<String>();
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                row.clear();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row.add(cursor.getString(i));
                }
                table.addRow(new DataRow(table, row));
            }
            return table;
        } catch (Exception ex) {
            System.out.println(ex);
            showError("(getTableBySql):" + ex.getMessage());
            return null;
        } finally {
            if (null != cursor) {
                cursor.close();
            }
            db.close();
        }
    }


    public abstract void showError(String msg);

}
