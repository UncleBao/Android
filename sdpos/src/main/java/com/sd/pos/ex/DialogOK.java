package com.sd.pos.ex;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sd.pos.R;
import com.yihujiu.util.Util;


/**
 * 通用对话框<br/>
 * 最多支持3个按钮,第2,3个按钮默认隐藏<br/>
 * 对话框创建后自动弹出
 *
 * @author yizhe
 * @date 2012-6-5
 */
public class DialogOK extends android.app.Dialog implements OnClickListener {
    Context context;
    public TextView titleView, msgView;
    public Button btOK, btCancel;
    private TextView vInput; // 用于接收回车事件

    String title, msg = "";

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.PROGRESS_VISIBILITY_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        titleView = (TextView) findViewById(R.id.HKDialog_title);
        msgView = (TextView) findViewById(R.id.HKDialog_msg);
        btOK = (Button) findViewById(R.id.HKDialog_bt1);
        btCancel = (Button) findViewById(R.id.HKDialog_bt2);

        btOK.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        if (null == title) {
            title = context.getResources().getString(R.string.dialog_title);
        }
        titleView.setText(title);
        msgView.setText(msg);

        LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        if (null != p) {
            // WindowManager windowManager = (WindowManager) getWindow();
            // Display display = windowManager.getDefaultDisplay();
            // WindowManager.LayoutParams lp = getWindow().getAttributes();
            // dialog.getWindow().setAttributes(lp);
            // 设置宽度
            p.width = Util.dip2px(context, 320);
            // p.width = (int) (Config.screenWidth * 0.5);
        }

        ini();
        setCanceledOnTouchOutside(false);

        vInput = (TextView) findViewById(R.id.HKDialog_input);
        vInput.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                int enterIndex = s.toString().indexOf('\n');
                if (enterIndex >= 0) {
                    DialogOK.this.btnOKClick();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        vInput.requestFocus();
    }

    /**
     * 初始化,设置按钮的可见性,文本等,会在onCreate函数中的最后调用
     */
    protected void ini() {

    }

    @Override
    public void show() {
        super.show();
    }

    /**
     * 创建对话框,使用默认标题 R.string.dialog_defaultTitle
     */
    public DialogOK(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 创建对话框,使用默认标题 R.string.dialog_defaultTitle
     *
     * @param context
     * @param msg     消息
     */
    public DialogOK(Context context, String msg) {
        super(context);
        this.context = context;
        this.msg = msg;
    }

    /**
     * 创建对话框,使用默认标题 R.string.dialog_defaultTitle
     */
    public DialogOK(Context context, int resId) {
        super(context);
        this.context = context;
        this.msg = context.getString(resId);
    }

    /**
     * 创建对话框
     *
     * @param context
     * @param title   标题
     * @param msg     消息
     */
    public DialogOK(Context context, String title, String msg) {
        super(context);
        this.context = context;
        this.title = title;
        this.msg = msg;
    }

    /**
     * 设置对话框高度
     *
     * @param height
     */
    public void setHeight(int height) {
        LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        if (null != p) {
            p.height = height;
        }
    }

    /**
     * 设置按钮标题,将在onCreate方法中调用到
     */
    protected void setButtonText() {

    }

    /**
     * 设置按钮是否可用
     *
     * @param index 按钮序号: 1,2,3
     * @param need  是否可用
     */
    public void setButtonVisible(int index, boolean need) {
        switch (index) {
            case 1:
                if (need) {
                    btOK.setVisibility(View.VISIBLE);
                } else {
                    btOK.setVisibility(View.GONE);
                }
                break;
            case 2:
                if (need) {
                    btCancel.setVisibility(View.VISIBLE);
                } else {
                    btCancel.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 设置按钮的文本
     *
     * @param index 按钮序号 1,OK  2,Cancel  3,其他
     * @param text  按钮文本,为null或者""时不修改
     */
    public void setButtonText(int index, String text) {
        if (Util.isNull(text)) {
            return;
        }
        switch (index) {
            case 1:
                btOK.setText(text);
                break;
            case 2:
                btCancel.setText(text);
                break;
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    // --------------------单击事件,用到请重写-------------------//

    protected void btnOKClick() {
        vInput.setText("");
        this.dismiss();// dismiss不会触发oncancel事件
    }

    public void onBtnCancelClick() {

        this.dismiss();
    }

    protected void onBtn3Click() {

        this.dismiss();
    }

    // --------------------------on-----------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.HKDialog_bt1:
                btnOKClick();
                break;
            case R.id.HKDialog_bt2:
                onBtnCancelClick();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("keyCode:" + keyCode);
        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.dismiss();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            btnOKClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
