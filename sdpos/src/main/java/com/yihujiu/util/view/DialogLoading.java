package com.yihujiu.util.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 加载中对话框
 */
public class DialogLoading extends Dialog {
    Context context;
    int animResID;
    int drawableResID;


    ImageView vLoading; // 圆型进度条
    Animation anim;// 动画

    /**
     * @param context
     * @param styleResID    对话框样式: 自适应大小,透明背景
     * @param animResID     动画资源ID: 循环旋转
     * @param drawableResID 旋转的图片
     */
    public DialogLoading(Context context, int styleResID, int animResID, int drawableResID) {
        super(context, styleResID);
        this.context = context;
        this.animResID = animResID;
        this.drawableResID = drawableResID;
        this.context = context;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.PROGRESS_VISIBILITY_ON);
        super.onCreate(savedInstanceState);

        anim = AnimationUtils.loadAnimation(this.getContext(), animResID);
        anim.setInterpolator(new LinearInterpolator());

        vLoading = new ImageView(context);
        vLoading.setImageResource(drawableResID);// 加载中的图片,建议圆形的

        // 布局
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        addContentView(vLoading, lp);

        setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 按下了键盘上返回按钮
            this.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void show() {
        try {
            super.show();
            vLoading.startAnimation(anim);
        } catch (Exception e) {

        }
    }
}


/* 用法
 public class DialogLoading extends com.yihujiu.util.view.DialogLoading {
     public DialogLoading(Context context) {
        super(context, R.style.dialog_loading, R.anim.rotate_repeat, R.drawable.loading_icon);
     }
 }
 */