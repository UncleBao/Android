package com.yihujiu.util.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 输入框监听器,接收回车事件后返回文本
 */
public abstract class TextWatcher4Enter implements TextWatcher {

    EditText vEditText;

    public TextWatcher4Enter(EditText vEditText) {
        this.vEditText = vEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public abstract void onScanEnter(String str);

    @Override
    public void afterTextChanged(Editable s) {
        int enterIndex = s.toString().indexOf('\n');
        if (enterIndex >= 0) {
            String str = trim(s);
            if (isNull(str)) {
                vEditText.setText("");
                return;
            }
            vEditText.setText(str);
            vEditText.setSelection(0, str.length());
            onScanEnter(str);
        } else if (enterIndex == 0) {
            vEditText.setText("");
        }
    }

    /**
     * 对回车后的文本,去回车或者空格,可以根据业务重写
     */
    protected String trim(Editable s) {
        return s.toString().replace("\n", "").trim();
    }

    /**
     * 检查字符串是否是空对象或空字符串
     *
     * @param str
     * @return 为空返回true, 不为空返回false
     */
    public boolean isNull(String str) {
        if (null == str || "".equals(str) || "null".equalsIgnoreCase(str)) {
            return true;
        } else {
            return false;
        }
    }
}