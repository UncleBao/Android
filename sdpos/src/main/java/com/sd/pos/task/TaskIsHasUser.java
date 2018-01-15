package com.sd.pos.task;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/1/13.
 */

public abstract class TaskIsHasUser extends PosTaskBase {
    public String UserCode = "";
    public String Password = "";

    public TaskIsHasUser(Activity activity, String method, String UserCode, String Password) {
        super(activity, method);
        this.UserCode = UserCode;
        this.Password = Password;
    }

    @Override
    public String createParam() {
        try {
            JSONObject params = NetBase.createBasParam();
            params.put("UserCode", UserCode);
            params.put("Password", Password);
            return params.toString();
        } catch (JSONException ex) {
            toast("初始化参数错误:" + ex.getMessage());
            return "";
        }
    }
}
