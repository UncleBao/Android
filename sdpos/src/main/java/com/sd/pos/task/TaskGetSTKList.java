package com.sd.pos.task;

import android.app.Activity;
import android.app.ListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/13.
 */

public abstract class TaskGetSTKList extends PosTaskBase {
    public final String Str_ListData = "listData";
    public String UserCode = "";

    public ArrayList<String> list = new ArrayList<String>();

    public TaskGetSTKList(Activity activity) {
        super(activity);
    }

    public TaskGetSTKList(Activity activity, String UserCode) {
        super(activity, "SP_GetSTKList");
        this.UserCode = UserCode;
    }

    @Override
    public String createParam() {
        try {
            JSONObject params = NetBase.createBasParam();
            params.put("UserCode", UserCode);
            return params.toString();
        } catch (JSONException ex) {
            toast("初始化参数错误:" + ex.getMessage());
            return "";
        }
    }

    // 任务成功
    public abstract void onTaskSuccess(ArrayList<String> list);

    @Override
    public void onTaskSuccess(JSONObject jsonObj) {
        try {
            JSONArray jsonArray = jsonObj.getJSONArray(Str_ListData);
            for (int i = 0; i < jsonArray.length(); i++) {
                String str = jsonArray.getString(i);
                list.add(str);
            }
            onTaskSuccess(list);
        } catch (JSONException e) {
            onTaskFailed(e.toString());
        }
    }
}
