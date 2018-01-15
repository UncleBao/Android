package com.sd.pos.dbhelp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yihujiu.util.table.DataTable;

/**
 * Created by Administrator on 2018/1/14.
 */

public class EnumHelper extends DBHelper {

    public EnumHelper(Context context) {
        super(context);
    }

    /**
     * 保存配置
     * @param key
     * @param value
     * @return
     */
    public boolean savePreferences(String key, String value) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("Delete From Enum Where key = ? And id = ?", new String[]{key, "0"});
            db.execSQL("Insert Into Enum (key,id,code) Values (?,?,?)", new String[]{key, "0", value});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            showError("(saveEnum):" + ex.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 读取配置
     * @param key
     * @return
     */
    public String getPreferences(String key) {
        DataTable dt = getTableBySql("Select * From Enum Where key = ? and id =0 ", new String[]{key});
        if (DataTable.isNull(dt)) {
            return "";
        } else {
            return dt.rows.get(0).get("code");
        }
    }

    /**
     * 保存枚举
     * @param key
     * @param id
     * @param code
     * @return
     */
    public boolean saveEnum(String key, int id, String code) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("Delete From Enum Where key = ? And id = ?", new String[]{key, id + ""});
            db.execSQL("Insert Into Enum (key,id,code) Values (?,?,?)", new String[]{key, id + "", code});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            showError("(saveEnum):" + ex.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 获取枚举
     * @param key
     * @return
     */
    public DataTable getEnum(String key) {
        return getTableBySql("Select * From Enum Where key = ?", new String[]{key});
    }

}
