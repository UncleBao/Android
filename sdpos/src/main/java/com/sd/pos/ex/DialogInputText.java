package com.sd.pos.ex;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.sd.pos.R;
import com.yihujiu.util.Util;


/**
 * 文本录入对话框
 */
public abstract class DialogInputText extends Dialog implements
        OnClickListener {
    Context context;
    public TextView vTitle;
    public TextView vMsg;
    public EditText vEdit;
    public Button btOK, btCancel;

    String title;
    String msg = "";
    String defaultValue = "";

    /**
     * 创建对话框,,默认输入数字,使用默认标题 R.string.dialog_defaultTitle
     */
    public DialogInputText(Context context) {
        super(context);
        this.context = context;

    }

    public DialogInputText(Context context, String defaultValue) {
        this(context);
        this.defaultValue = defaultValue;
    }

    public DialogInputText(Context context, String title, String defaultValue) {
        this(context, defaultValue);
        this.title = title;
        this.defaultValue = defaultValue;
    }

    public DialogInputText(Context context, String title, String defaultValue, String msg) {
        this(context, title, defaultValue);
        this.msg = msg;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.PROGRESS_VISIBILITY_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_text);

        vTitle = (TextView) findViewById(R.id.hk_dialog_input_title);
        vMsg = (TextView) findViewById(R.id.hk_dialog_input_msg);
        vEdit = (EditText) findViewById(R.id.hk_dialog_input_edit);
        btOK = (Button) findViewById(R.id.hk_dialog_input_ok);
        btCancel = (Button) findViewById(R.id.hk_dialog_input_cancel);

        btOK.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        if (null == title || "".equals(title)) {
            title = "录入";
        }
        vTitle.setText(title);
        vEdit.setText(defaultValue);
        if (null == msg || "".equals(msg)) {
            vMsg.setVisibility(View.GONE);
        } else {
            vMsg.setText(msg);
        }

        LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        if (null != p) {
            p.width = Util.dip2px(context, 280);
        }

        // 监控输入框的换行符
        vEdit.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                onBtnOKClick();
                return false;
            }
        });

        vEdit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                int enterIndex = s.toString().indexOf('\n');
                if (enterIndex >= 0) {
                    DialogInputText.this.onBtnOKClick();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        ini();
    }

    protected void ini() {
        vEdit.setSelection(0, vEdit.length());
    }

    /**
     * 设置对话框高度
     */
    public void setHeight(int height) {
        LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        if (null != p) {
            p.height = height;
        }
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
     * @param index 按钮序号
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

    public void setDefaultValue(int defaultValue) {
        // this.defualtValue = defaultValue;
    }

    // --------------------单击事件,用到请重写-------------------//
    protected abstract void onBtnOKClick(String val);

    protected void onBtnOKClick() {
        onBtnOKClick(vEdit.getText().toString());
        this.dismiss();// 不会触发oncancel事件
    }

    protected void onBtnCancelClick() {
        this.dismiss();
    }

    // --------------------------on-----------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hk_dialog_input_ok:
                onBtnOKClick();
                break;
            case R.id.hk_dialog_input_cancel:
                onBtnCancelClick();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.dismiss();
        }
        return true;
    }
}
