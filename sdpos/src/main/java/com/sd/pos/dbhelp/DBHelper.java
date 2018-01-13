package com.sd.pos.dbhelp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sd.pos.db.HKSQLiteOpenHelper;
import com.sd.pos.db.IDBCreater;

/**
 * Created by yi.zhe on 2016/5/6.
 */
public abstract class DBHelper extends HKSQLiteOpenHelper {

    static int DBVersion = 100;

    public DBHelper(Context context) {
        super(context, DBVersion);
    }

    @Override
    protected IDBCreater setDBCreater() {
        return new DBCreater();
    }
}


class DBCreater implements IDBCreater {
    //创建数据库脚本
    public void CreateDatabase(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            System.out.println("IniDatabase...");
            //通用表格
            createNetTaskBuffer(db);
            createENUM(db);

            //业务表格
            createBillMaster(db);

            System.out.println("CreateTableOver");

            //更新表结构
            BillMaster_ALTER(db);

            System.out.println("AlterTableOver");

            //创建视图
            //createViewScanMat(db);

            System.out.println("CreateViewOver");

            System.out.println("InsertOver");

            System.out.println("CreateDatabaseSuccess!");
            // 提交数据
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.println("CreateDatabaseFailure!");
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void UpdateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println(oldVersion + "abc" + newVersion);
    }

    // 网络数据缓存
    private static void createNetTaskBuffer(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS NetTaskBuffer (");
        sql.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,"); // 这一列不能修改
        sql.append(" label TEXT COLLATE NOCASE,");
        sql.append(" param TEXT COLLATE NOCASE,");
        sql.append(" result TEXT COLLATE NOCASE,");
        sql.append(" remark TEXT COLLATE NOCASE,");
        sql.append(" time LONG)");
        db.execSQL(sql.toString());
    }

    // 枚举表
    public static void createENUM(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ENUM (");
        sql.append(" EnumType TEXT COLLATE NOCASE,"); //类型
        sql.append(" EnumID INTEGER COLLATE NOCASE,"); //
        sql.append(" EnumCode TEXT COLLATE NOCASE,"); //
        sql.append(" EnumName TEXT COLLATE NOCASE,"); //
        sql.append(" EnumKey TEXT COLLATE NOCASE,"); //键,扩展用
        sql.append(" PRIMARY KEY (StateType,StateID) )");
        db.execSQL(sql.toString());
    }

    // 单据主表
    private static void createBillMaster(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS BillMaster (");
        sql.append(" CompanyID TEXT COLLATE NOCASE,");
        sql.append(" BillNo TEXT COLLATE NOCASE,");
        sql.append(" BillTypeID TEXT COLLATE NOCASE,");
        sql.append(" ManualBillNo TEXT COLLATE NOCASE,");
        sql.append(" BillDate TEXT COLLATE NOCASE,");
        sql.append(" SourceBillNo TEXT COLLATE NOCASE,");
        sql.append(" BillStatus TEXT COLLATE NOCASE,");
        sql.append(" StockID TEXT COLLATE NOCASE,");
        sql.append(" Operator TEXT COLLATE NOCASE,");
        sql.append(" Checker TEXT COLLATE NOCASE,");
        sql.append(" FirstAuditBillStatus TEXT COLLATE NOCASE,");
        sql.append(" CallbackAuditBillStatus TEXT COLLATE NOCASE,");
        sql.append(" ModifyDTM TEXT COLLATE NOCASE,");
        sql.append(" SourceBill TEXT COLLATE NOCASE,");
        sql.append(" FirstAuditBillStatusName TEXT COLLATE NOCASE,");
        sql.append(" PRIMARY KEY (CompanyID,BillNo,BillTypeID) )");
        db.execSQL(sql.toString());
    }

    // 单据主表
    private static void createBillDetail(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS BillDetail (");
        sql.append(" CompanyID TEXT COLLATE NOCASE,");
        sql.append(" BillNo TEXT COLLATE NOCASE,");
        sql.append(" BillTypeID TEXT COLLATE NOCASE,");
        sql.append(" ManualBillNo TEXT COLLATE NOCASE,");
        sql.append(" BillDate TEXT COLLATE NOCASE,");
        sql.append(" SourceBillNo TEXT COLLATE NOCASE,");
        sql.append(" BillStatus TEXT COLLATE NOCASE,");
        sql.append(" StockID TEXT COLLATE NOCASE,");
        sql.append(" Operator TEXT COLLATE NOCASE,");
        sql.append(" Checker TEXT COLLATE NOCASE,");
        sql.append(" FirstAuditBillStatus TEXT COLLATE NOCASE,");
        sql.append(" CallbackAuditBillStatus TEXT COLLATE NOCASE,");
        sql.append(" ModifyDTM TEXT COLLATE NOCASE,");
        sql.append(" SourceBill TEXT COLLATE NOCASE,");
        sql.append(" FirstAuditBillStatusName TEXT COLLATE NOCASE,");
        sql.append(" PRIMARY KEY (CompanyID,BillNo,BillTypeID) )");
        db.execSQL(sql.toString());
    }

    // 修改表
    private static void BillMaster_ALTER(SQLiteDatabase db) {
//        if (!isFieldExist(db, "BillCheckMaster", "Remark")) {
//            db.execSQL(" ALTER TABLE BillCheckMaster ADD COLUMN Remark TEXT COLLATE NOCASE ");
//        }
    }


    /**
     * 判断某表里某字段是否存在
     *
     * @param db
     * @param tableName
     * @param fieldName
     * @return
     */
    private static boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName) {
        String queryStr = "select sql from sqlite_master where type = 'table' and name = '%s'";
        queryStr = String.format(queryStr, tableName);
        Cursor c = db.rawQuery(queryStr, null);
        String tableCreateSql = null;
        try {
            if (c != null && c.moveToFirst()) {
                tableCreateSql = c.getString(c.getColumnIndex("sql"));
            }
        } finally {
            if (c != null)
                c.close();
        }
        if (tableCreateSql != null && tableCreateSql.contains(fieldName))
            return true;
        return false;
    }
}