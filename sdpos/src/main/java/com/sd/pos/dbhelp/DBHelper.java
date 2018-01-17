package com.sd.pos.dbhelp;

import android.content.Context;

import com.yihujiu.util.sqlit.IDBCreator;
import com.yihujiu.util.sqlit.SQLiteOpenHelperBase;
import com.sd.pos.ex.DialogOK;

/**
 * 数据库操作类,主要用法释放执行SQL和获取DataTable
 */
public class DBHelper extends SQLiteOpenHelperBase {

    static int DBVersion = 101;

    public DBHelper(Context context) {
        super(context, DBVersion);
    }

    @Override
    protected IDBCreator setDBCreator() {
        return new DBCreator();
    }

    @Override
    public void showError(String msg) {
        DialogOK dialog1 = new DialogOK(context, "APP数据库错误", msg);
        dialog1.show();
    }
}


