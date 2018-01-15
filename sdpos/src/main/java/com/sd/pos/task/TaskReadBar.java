package com.sd.pos.task;

import android.app.Activity;

import com.sd.pos.R;
import com.yihujiu.util.table.DataTable;
import com.yihujiu.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/13.
 */

public abstract class TaskReadBar extends PosTaskBase {
    public String barcode = "";

    public ArrayList<String> list = new ArrayList<String>();

    public TaskReadBar(Activity activity, String barcode) {
        super(activity, "SP_Readbar");
        this.barcode = barcode;
    }

    @Override
    public String createParam() {
        try {
            JSONObject params = NetBase.createBasParam();
            params.put("barcode", barcode);
            return params.toString();
        } catch (JSONException ex) {
            toast("初始化参数错误:" + ex.getMessage());
            return "";
        }
    }

    /**
     * 输出查询到的表格<br>
     * 若是表格为null或者表格没有数据 本函数将不会触发
     *
     * @param table
     */
    protected abstract void onTaskSuccessAndHaveData(DataTable table, boolean isAsk, String msg,
                                                     ArrayList<String> list);

    // 任务失败,或者没有获取到表格
    protected void onTaskFailOrNoData(boolean isSuccess, String msg, ArrayList<String> list) {
        if (!isSuccess) {
            if (Util.isNull(msg)) {
                msg = "获取任务失败!";
            }
            toast(msg);
        } else {
            if (Util.isNull(msg)) {
                toast(R.string.msg_QueryNoData);
            } else {
                toast(msg);
            }
        }
    }

    @Override
    public void onTaskSuccess(JSONObject jsonObj) {
        DataTable dt = null;
        String msg = "";
        ArrayList<String> msgList = null;
        boolean isAsk = false;
        try {
            if (NetBase.isOK(jsonObj)) {
                msg = NetBase.getMsg(jsonObj);
                msgList = NetBase.getMsgList(jsonObj);
                isAsk = NetBase.getIsAsk(jsonObj);

                JSONArray jsonArr = jsonObj.optJSONArray(NetBase.Str.List);
                if (jsonArr == null) {
                    onTaskFailOrNoData(true, msg, msgList);
                    return;
                }
                dt = new DataTable(jsonArr);

                if (!dt.haveData()) {
                    onTaskFailOrNoData(true, msg, msgList);
                    return;
                }
            } else {
                msg = NetBase.getMsg(jsonObj);
                msgList = NetBase.getMsgList(jsonObj);
                onTaskFailOrNoData(false, msg, msgList);
                return;
            }
        } catch (JSONException e) {
            onTaskFailOrNoData(false, "数据解析失败!" + '\n' + jsonObj.toString(), null);
            System.out.println("解析数据失败:" + e.toString() + "\n result:" + jsonObj.toString());
            return;
        }

        onTaskSuccessAndHaveData(dt, isAsk, msg, msgList);
    }
}
