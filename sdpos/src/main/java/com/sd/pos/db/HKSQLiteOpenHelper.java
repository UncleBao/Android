package com.sd.pos.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.sd.pos.ex.DialogOK;
import com.sd.pos.table.DataRow;
import com.sd.pos.table.DataTable;

import java.util.ArrayList;

/**
 * 数据库基本操作类,数据库的创建,更新的操作都在这里进行
 *
 * @author yizhe
 * @date 2012-5-18
 */
public abstract class HKSQLiteOpenHelper extends SQLiteOpenHelper {

    static String name = "sdpos.db"; // 数据库名称
    static CursorFactory cursorFactory = null;
    protected Context context;

    protected String tableName = ""; // 表名,留给子类设置

    public HKSQLiteOpenHelper(Context context, int version) {
        super(context, name, cursorFactory, version);
        this.context = context;
        this.tableName = setTableName();
    }

    /**
     * 软件第一次安装的时候会调用,覆盖安装不会调用
     */
    public void onCreate(SQLiteDatabase db) {
        // 所有表的创建过程都在这里进行
        setDBCreater().CreateDatabase(db);
    }

    /**
     * 覆盖安装,当版本号version发生变化的时候,这个方法才会被调用,而且只执行一次
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        setDBCreater().CreateDatabase(db);
        setDBCreater().UpdateDatabase(db, oldVersion, newVersion);
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

    // --------------------------表配置---------------------------------//

    /**
     * 设置创建数据库的类
     */
    protected abstract IDBCreater setDBCreater();

    // 强制实现,防止漏写表名
    protected abstract String setTableName();

    // --------------------------sql方法---------------------------------//

    /**
     * 执行sql, 返回1表示成功, 返回其他表示执行失败, 返回的内容就是错误提示
     */
    public String execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL(sql);
            db.setTransactionSuccessful();
            return "1";
        } catch (Exception ex) {
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
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL(sql, param);
            db.setTransactionSuccessful();
            return "1";
        } catch (Exception ex) {
            return ex.getMessage();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 通用执行sql,,多用于事务中,db在这里不会关闭
     */
    public static void execSQL(SQLiteDatabase db, String sql) {
        System.out.println("==execSQL==" + sql);
        db.execSQL(sql);
    }


    /**
     * 删
     *
     * @param whereClause the optional WHERE clause to apply when deleting.
     *                    Passing null will delete all rows.
     * @param whereArgs   You may include ?s in the where clause, which
     *                    will be replaced by the values from whereArgs. The values
     *                    will be bound as Strings.
     * @return the number of rows affected if a whereClause is passed in, 0
     * otherwise. To remove all rows and get a count pass "1" as the
     * whereClause.
     **/
    public int delete(String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(tableName, whereClause, whereArgs);
        System.out.println("===delete=== " + tableName + ":result:" + result + ":" + whereClause + whereArgs);
        db.close();
        return result;
    }


    public int delete(SQLiteDatabase db, String whereStr, String[] arr) {
        int result = db.delete(tableName, whereStr, arr);
        System.out.println("===delete=== " + tableName + ":result:" + result + ":" + whereStr + ":" + arr.toString());
        return result;
    }

    //--------------------------根据ID执行的语句---表中主键必须是"id"(整型,自增长)--------------------

    /**
     * 根据id删除记录
     *
     * @param id 表中必须有"id"字段
     * @return the number of rows affected if a whereClause is passed in, 0
     * otherwise. To remove all rows and get a count pass "1" as the
     * whereClause.
     */
    public int deleteByID(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(tableName, "id=?", new String[]{id + ""});
        db.close();
        return result;
    }

    /**
     * 删除全表数据
     *
     * @return
     */
    public int delete() {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(tableName, null, null);
        db.close();
        return result;
    }

    //SQL参数防注入
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
        } catch (Exception e) {
            showError("(getTableBySql):" + e.getMessage());
            return null;
        } finally {
            if (null != cursor) {
                cursor.close();
            }
            db.close();
        }
    }

    protected void showError(String msg) {
        DialogOK dialog1 = new DialogOK(context, "APP数据库错误", msg);
        dialog1.show();
    }

}
