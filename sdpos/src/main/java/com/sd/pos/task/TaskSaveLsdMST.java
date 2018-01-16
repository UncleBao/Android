package com.sd.pos.task;

import android.app.Activity;

import com.yihujiu.util.table.DataRow;
import com.yihujiu.util.table.DataTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.xml.datatype.DatatypeConfigurationException;

/**
 * Created by Administrator on 2018/1/13.
 */

public abstract class TaskSaveLsdMST extends PosTaskBase {
    public String StockCode = "";
    public String UserCode = "";
    public DataTable dtDetail;


    public TaskSaveLsdMST(Activity activity, String StockCode, String UserCode, DataTable dtDetail) {
        super(activity, "SP_SaveLsdMST");
        this.StockCode = StockCode;
        this.UserCode = UserCode;
        this.dtDetail = dtDetail;
    }

    @Override
    public String createParam() {
        if (DataTable.isNull(dtDetail)) {
            return "";
        }
        try {
            JSONObject params = NetBase.createBasParam();
            params.put("StockCode", StockCode);
            params.put("UserCode", UserCode);
            params.put("listData", getJsonArray());
            return params.toString();
        } catch (JSONException ex) {
            toast("初始化参数错误:" + ex.getMessage());
            return "";
        }
    }

    JSONArray getJsonArray() {
        //listData;["fgdcode","fyscode","fccode","saleprice","discount","qty","Aoumt"],
        JSONArray arrObj = new JSONArray();
        JSONArray rowHeader = new JSONArray();
        rowHeader.put("fgdcode");
        rowHeader.put("fyscode");
        rowHeader.put("fccode");
        rowHeader.put("saleprice");
        rowHeader.put("discount");
        rowHeader.put("qty");
        rowHeader.put("Aoumt");
        arrObj.put(rowHeader);
        for (DataRow dr : dtDetail.rows) {
            JSONArray row = new JSONArray();
            row.put(dr.get("fgdcode"));
            row.put(dr.get("fyscode"));
            row.put(dr.get("fccode"));
            row.put(dr.get("saleprice"));
            row.put(dr.get("discount"));
            row.put(dr.get("qty"));
            row.put(dr.get("Aoumt"));
            arrObj.put(row);
        }
        return arrObj;
    }

}
