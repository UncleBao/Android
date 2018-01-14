package com.sd.pos.dbhelp;

import android.content.Context;

import com.sd.pos.table.DataTable;

/**
 * Created by Administrator on 2018/1/14.
 */

public class EnumHelper extends DBHelper {

    public EnumHelper(Context context) {
        super(context);
    }

    public boolean saveEnum(String key, int id, String code) {
        String result = execSQL("Insert Into Enum (key,id,code) Values (?,?,?)", new String[]{key, id + "", code});
        if ("1".equalsIgnoreCase(result)) {
            return true;
        }
        return false;
    }

    public DataTable getEnum(String key) {
        return getTableBySql("Select * From Enum Where key = ?", new String[]{key});
    }

    public String getOneCode(String key) {
        DataTable dt = getTableBySql("Select * From Enum Where key = ?", new String[]{key});
        if (DataTable.isNull(dt)) {
            return "";
        } else {
            return dt.rows.get(0).get("code");
        }
    }


}
