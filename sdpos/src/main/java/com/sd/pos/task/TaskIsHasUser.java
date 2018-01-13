package com.sd.pos.task;

import android.app.Activity;

import com.sd.pos.comm.Config;
import com.sd.pos.util.TaskBase;
import com.sd.pos.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sd.pos.util.UtilNetBase.httpPost;

/**
 * Created by Administrator on 2018/1/13.
 */

public abstract class TaskIsHasUser extends PosTaskBase {
    public String UserCode = "";
    public String Password = "";

    public TaskIsHasUser(Activity activity) {
        super(activity);
    }

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
