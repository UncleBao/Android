package com.sd.pos.task;

import android.app.Activity;

import com.sd.pos.comm.Config;
import com.sd.pos.util.TaskBase;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sd.pos.util.UtilNetBase.httpPost;

public abstract class PosTaskBase extends TaskBase {
    public String method = "";

    public PosTaskBase(Activity activity) {
        super(activity);
    }

    public PosTaskBase(Activity activity, String method) {
        super(activity);
        this.method = method;
    }

    @Override
    protected String doInThread() {
        return httpPost(Config.URL + method, createParam());
    }

    // 任务成功
    public abstract String createParam();

    // 任务成功
    public abstract void onTaskSuccess(JSONObject jsonObj);

    // 任务失败
    public void onTaskFailed(String msg) {
        toast(msg);
    }

    @Override
    protected void onTaskFinish(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            if (NetBase.isOK(jsonObj)) {
                onTaskSuccess(jsonObj);
            } else {
                onTaskFailed(NetBase.getMsg(jsonObj));
            }
        } catch (JSONException e) {
            onTaskFailed(e.toString());
            System.out.println("解析数据失败:" + e.toString() + "\n result:" + result);
        }
    }
}
