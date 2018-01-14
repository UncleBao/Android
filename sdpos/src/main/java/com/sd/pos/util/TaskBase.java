package com.sd.pos.util;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.sd.pos.ex.DialogLoading;

/**
 * 通用的任务基类2,已经包含MyTask,直接实现两个方法就可以<br>
 * 本基类完成了一下封装:<br>
 * 1,任务是否在执行的判断<br>
 * 2,弹出与取消[加载中...]对话框<br>
 * 3,调用类不存在时自动取消任务<br>
 */
public abstract class TaskBase {
    public Activity activity;
    boolean isNeedLoadingDialog = true;// 是否需要显示加载中对话框
    protected boolean isExecuteInBackground = false;// 是否不显示加载中对话框

    protected MyTask myTask;

    public TaskBase(Activity activity) {
        this.activity = activity;
    }

    public TaskBase(Activity activity, boolean isNeedLoadingDialog) {
        this.activity = activity;
        this.isNeedLoadingDialog = isNeedLoadingDialog;
    }

    public void execute() {
        beforeExecute();
        isExecuteInBackground = false;
        if (null == myTask) {
            myTask = new MyTask();
            myTask.execute();
            showDialogLoading(true);
        } else {
            //toast(R.string.msg_TaskIsWorking);
        }
    }

    // 后台执行,不弹出加载中对话框
    public void executeInBackground() {
        beforeExecute();
        isExecuteInBackground = true;
        if (null == myTask) {
            myTask = new MyTask();
            myTask.execute();
        }
    }

    /**
     * 在正式执行网络请求前执行,用于校验或者修改请求的参数
     */
    protected void beforeExecute() {

    }

    /**
     * 线程中后台执行的任务体(调用网络的Post方法)
     *
     * @return 从网络获取到的字符串
     */
    protected abstract String doInThread();

    /**
     * 网络任务结束后的动作
     *
     * @param result 从网络获取到的字符串
     */
    protected abstract void onTaskFinish(String result);

    // ----------------------------------网络任务-------------------------------------//
    private class MyTask extends AsyncTask<Void, Void, Boolean> {
        String result;

        @Override
        protected Boolean doInBackground(Void... params) {
            result = doInThread();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            myTask = null;
            showDialogLoading(false);
            if (null == activity || activity.isFinishing()) {
                // 任务取消: (调用任务的类不存在)
                return;
            }
            onTaskFinish(result);
        }

        @Override
        protected void onCancelled() {
            myTask = null;
            showDialogLoading(false);
        }
    }

    // ----------------------------------工具-------------------------------------//

    protected DialogLoading mDialogLoading; // 加载中对话框,用于网络任务
    /**
     * 加载中对话框(默认)
     */
    protected void showDialogLoading(boolean isShow) {
        if (isShow && isNeedLoadingDialog) {
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

    /**
     * 通用消息提示
     *
     * @param resID
     */
    public String getResStr(int resID){
        return activity.getResources().getString(resID);
    }

    public void toast(String msg) {
        toastMy(msg, Toast.LENGTH_SHORT);
    }

    public void toast(int resID) {
        toastMy(activity.getResources().getString(resID), Toast.LENGTH_SHORT);
    }

    public void toastLong(String msg) {
        toastMy(msg, Toast.LENGTH_LONG);
    }

    Toast toast;

    public void toastMy(String msg, int duration) {
        if (toast == null) {
            toast =  Toast.makeText(activity, msg, duration);
            toast.show();
        } else {
            toast.show();
        }
    }
}
