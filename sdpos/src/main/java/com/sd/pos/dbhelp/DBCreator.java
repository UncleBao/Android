package com.sd.pos.dbhelp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yihujiu.util.sqlit.IDBCreator;

/**
 * Created by Administrator on 2018/1/14.
 */

class DBCreator implements IDBCreator {
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
            createBillDetail(db);

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
        sql.append(" key TEXT COLLATE NoCase,"); //枚举类型
        sql.append(" id INTEGER COLLATE NoCase,"); //枚举键
        sql.append(" code TEXT COLLATE NoCase,"); //枚举值
        sql.append(" code2 TEXT COLLATE NoCase,"); //扩展
        sql.append(" code3 TEXT COLLATE NoCase,"); //扩展
        sql.append(" PRIMARY KEY (key,id) )");
        db.execSQL(sql.toString());
    }

    // 单据主表
    private static void createBillMaster(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS BillMaster (");
        sql.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,"); // 这一列不能修改
        sql.append(" Company TEXT COLLATE NOCASE,");
        sql.append(" ManualBillNo TEXT COLLATE NOCASE,"); //手工单号
        sql.append(" BillNo TEXT COLLATE NOCASE,");
        sql.append(" BillType TEXT COLLATE NOCASE,"); //单据类型
        sql.append(" BillDate TEXT COLLATE NOCASE,");
        sql.append(" BillStatus TEXT COLLATE NOCASE,");
        sql.append(" VipCardCode TEXT COLLATE NOCASE,"); //Vip卡号
        sql.append(" PriceType TEXT COLLATE NOCASE,"); //价格类型
        sql.append(" UserCode TEXT COLLATE NOCASE,"); //用户
        sql.append(" UserName TEXT COLLATE NoCase,");
        sql.append(" StockCode TEXT COLLATE NOCASE,"); //仓库
        sql.append(" StockName TEXT COLLATE NOCASE,");
        sql.append(" DepartmentCode TEXT COLLATE NOCASE,"); //部门
        sql.append(" DepartmentName TEXT COLLATE NOCASE,");
        sql.append(" CustomerCode TEXT COLLATE NOCASE,"); //客户
        sql.append(" CustomerName TEXT COLLATE NOCASE,");
        sql.append(" ContractNumber TEXT COLLATE NOCASE,"); //合同号
        sql.append(" IsMatMoreLine INTEGER COLLATE NOCASE,");//允许同一商品占用多行
        sql.append(" IsInvoice INTEGER COLLATE NOCASE,");  //是否开票
        sql.append(" Remark TEXT COLLATE NOCASE,");  //是否开票
        sql.append(" DraftStatus INTEGER COLLATE NOCASE,"); // -1:已删除, 0:挂单, 1:已取单, 2:已提交
        sql.append(" CreateTime TEXT COLLATE NOCASE,"); //保存时间 "2018-01-15 11:59:59"
        sql.append(" ModifyTime TEXT COLLATE NOCASE,"); //修改时间 "2018-01-15 11:59:59"
        sql.append(" time LONG)");
        db.execSQL(sql.toString());
    }

    // 单据明细
    private static void createBillDetail(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS BillDetail (");
        sql.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,"); // 这一列不能修改
        sql.append(" ManualBillNo TEXT COLLATE NOCASE,"); //手工单号 //BillMaster表主键
        sql.append(" fgdcode TEXT COLLATE NOCASE,");//货品
        sql.append(" fgdname TEXT COLLATE NOCASE,");
        sql.append(" fyscode TEXT COLLATE NOCASE,");//颜色
        sql.append(" fysname TEXT COLLATE NOCASE,");
        sql.append(" fccode TEXT COLLATE NOCASE,");//尺码
        sql.append(" fcname TEXT COLLATE NOCASE,");
        sql.append(" barcode TEXT COLLATE NOCASE,");
        sql.append(" saleprice REAL COLLATE NOCASE,");//单价
        sql.append(" discount REAL COLLATE NOCASE,");//折扣
        sql.append(" qty INTEGER COLLATE NOCASE,");//数量
        sql.append(" amount REAL COLLATE NOCASE,");//金额
        sql.append(" time LONG)");
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
