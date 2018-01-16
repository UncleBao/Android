package com.sd.pos.dbhelp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

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
    public boolean saveBill(String ManualBill, String UserCode, String StockCode, DataTable dtDetail) {
        if (DataTable.isNull(dtDetail)) {
            return false;
        }
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("Delete From BillMaster Where ManualBill = ? ", new String[]{ManualBill});
            db.execSQL("Insert Into BillMaster (ManualBill,UserCode,StockCode) Values (?,?,?)", new String[]{ManualBill, UserCode, StockCode});
            db.execSQL("Delete From BillDetail Where ManualBill = ? ", new String[]{ManualBill});
            //["fgdcode","fgdname","fyscode","fysname","fccode","fcname","saleprice"]
            for (DataRow dr : dtDetail.rows) {
                db.execSQL("Insert Into BillDetail (ManualBill,fgdcode,fgdname,fyscode,fysname,fccode,fcname,saleprice,discount,qty) Values (?,?,?,?,?,?,?,?,?,?)",
                        new String[]{ManualBill, dr.get("fgdcode"), dr.get("fgdname"), dr.get("fyscode"), dr.get("fysname"), dr.get("fccode"),
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
        return getTableBySql("Select * From BillMaster", null);
    }

    //取单
    public DataTable getBillMaster(String ManualBill) {
        return null;
    }

    //取单
    public DataTable getBillDetail(String ManualBill) {
        return null;
    }


}
