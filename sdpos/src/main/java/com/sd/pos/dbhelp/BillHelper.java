package com.sd.pos.dbhelp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.yihujiu.util.Util;
import com.yihujiu.util.table.DataRow;
import com.yihujiu.util.table.DataTable;

/**
 * Created by Administrator on 2018/1/14.
 */

public class BillHelper extends DBHelper {

    public BillHelper(Context context) {
        super(context);
    }

    //挂单
    public boolean saveBill(String ManualBillNo, String UserCode, String StockCode, String StockName, DataTable dtDetail) {
        if (Util.isNull(ManualBillNo) || Util.isNull(UserCode) || Util.isNull(StockCode)) {
            showError("参数为空!");
            return false;
        }
        if (DataTable.isNull(dtDetail)) {
            showError("明细为空!");
            return false;
        }
        String CreateTime = Util.timeFormatTime();
//        execSQL("Delete From BillMaster Where ManualBillNo = ? ", new String[]{ManualBillNo + "D"});
//        execSQL("Insert Into BillMaster (ManualBillNo,UserCode,StockCode,StockName,CreateTime) Values (?,?,?,?,?)",
//                new String[]{ManualBillNo + "f", UserCode, StockCode, StockName, CreateTime});
//        return true;

        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("Delete From BillMaster Where ManualBillNo = ? ", new String[]{ManualBillNo + "D"});
            db.execSQL("Insert Into BillMaster (ManualBillNo,UserCode,StockCode,StockName,CreateTime) Values (?,?,?,?,?)",
                    new String[]{ManualBillNo + "D", UserCode, StockCode, StockName, CreateTime});
            //["fgdcode","fgdname","fyscode","fysname","fccode","fcname","saleprice"]
            db.execSQL("Delete From BillDetail Where ManualBillNo = ? ", new String[]{ManualBillNo + "D"});
            for (DataRow dr : dtDetail.rows) {
                db.execSQL("Insert Into BillDetail (ManualBillNo,fgdcode,fgdname,fyscode,fysname,fccode,fcname,saleprice,discount,qty) Values (?,?,?,?,?,?,?,?,?,?)",
                        new String[]{ManualBillNo + "D", dr.get("fgdcode"), dr.get("fgdname"), dr.get("fyscode"), dr.get("fysname"), dr.get("fccode"),
                                dr.get("fcname"), dr.get("saleprice"), dr.get("discount"), dr.get("qty")});
            }
            db.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            showError("(saveBill):" + ex.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    //获取草稿列表
    public DataTable getBill4Select() {
        return getTableBySql("Select * From BillMaster Order By CreateTime Desc", new String[]{});
    }

    //取单
    public DataRow getBillMaster(String ManualBillNo) {
        DataTable dt = getTableBySql("Select * From BillMaster Where ManualBillNo = ?", new String[]{ManualBillNo});
        if (DataTable.isNull(dt)) {
            return null;
        } else {
            return dt.rows.get(0);
        }
    }

    //取单
    public DataTable getBillDetail(String ManualBillNo) {
        if (Util.isNull(ManualBillNo)) {
            return getTableBySql("Select fgdcode,fgdname,fyscode,fysname,fccode,fcname,saleprice,discount,qty,amount,barcode From BillDetail Where 1=2 ", null);
        } else {
            return getTableBySql("Select fgdcode,fgdname,fyscode,fysname,fccode,fcname,saleprice,discount,qty,amount,barcode From BillDetail Where ManualBillNo = ?", new String[]{ManualBillNo});
        }
    }

    //删除单据
    public boolean delBillDetail(String ManualBillNo) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("Delete From BillMaster Where ManualBillNo = ? ", new String[]{ManualBillNo});
            db.execSQL("Delete From BillDetail Where ManualBillNo = ? ", new String[]{ManualBillNo});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            showError("(saveBill):" + ex.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
