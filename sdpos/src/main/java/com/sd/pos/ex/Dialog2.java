package com.sd.pos.ex;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * 通用对话框<br/>
 * 最多支持3个按钮,第2,3个按钮默认隐藏<br/>
 * 对话框创建后自动弹出
 *
 * @author yizhe
 * @date 2012-6-5
 */
public abstract class Dialog2 extends DialogOK {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btCancel.setVisibility(View.VISIBLE);
    }

    /**
     * 创建对话框,使用默认标题 R.string.dialog_defaultTitle
     */
    public Dialog2(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 创建对话框,使用默认标题 R.string.dialog_defaultTitle
     */
    public Dialog2(Context context, String msg) {
        super(context);
        this.context = context;
        this.msg = msg;
    }

    /**
     * 创建对话框,使用默认标题 R.string.dialog_defaultTitle
     */
    public Dialog2(Context context, int msgResId) {
        super(context);
        this.context = context;
        this.msg = context.getString(msgResId);
    }

    /**
     * 创建对话框
     *
     * @param context
     * @param title   标题
     * @param msg     消息
     */
    public Dialog2(Context context, String title, String msg) {
        super(context);
        this.context = context;
        this.title = title;
        this.msg = msg;
    }

    protected abstract void onBtnOKClick();

    protected void btnOKClick() {
        onBtnOKClick();
        this.dismiss();// 不会触发oncancel事件
    }
}
