package com.sd.pos.task;

import android.app.Activity;

import com.sd.pos.comm.Config;
import com.sd.pos.ex.DialogLoading;
import com.yihujiu.util.TaskBase;

import org.json.JSONException;
import org.json.JSONObject;

import static com.yihujiu.util.UtilNetBase.httpPost;

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

    // ----------------------------------工具-------------------------------------//

    protected DialogLoading mDialogLoading; // 加载中对话框,用于网络任务

    /**
     * 加载中对话框(默认)
     */
    protected void showDialogLoading(boolean isShow) {
        if (isShow) {
            if (null == mDialogLoading) {
                mDialogLoading = new DialogLoading(activity);
            }
            mDialogLoading.show(); // 显示加载中对话框
        } else {
            if (null != mDialogLoading) {
                if (mDialogLoading.isShowing()) {
                    try {
                        mDialogLoading.dismiss(); // 取消加载中对话框, 对话框要写在
                    } catch (Exception e) {
                        // 忽略关闭加载框的错误
                    }
                }
            }
        }
    }
}
