package com.sd.pos.ex;

import android.content.Context;

import com.sd.pos.R;

/**
 * 加载中对话框
 */
public class DialogLoading extends com.yihujiu.util.view.DialogLoading {
    public DialogLoading(Context context) {
        super(context, R.style.dialog_loading, R.anim.rotate_repeat, R.drawable.loading_icon);
    }
}
