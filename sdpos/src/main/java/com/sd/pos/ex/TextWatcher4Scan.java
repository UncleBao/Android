package com.sd.pos.ex;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Administrator on 2018/1/13.
 */

public abstract class TextWatcher4Scan implements TextWatcher {

    EditText vEditText;

    public TextWatcher4Scan(EditText vEditText) {
        this.vEditText = vEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    protected String editableToBarcode(Editable s) {
        return s.toString().replace("\n", "").trim();
    }

    public abstract void onScanEnter(EditText v, String str);

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

    @Override
    public void afterTextChanged(Editable s) {
        int enterIndex = s.toString().indexOf('\n');
        if (enterIndex >= 0) {
            String str = editableToBarcode(s);
            if (isNull(str)) {
                vEditText.setText("");
                return;
            }
            vEditText.setText(str);
            vEditText.setSelection(0, str.length());
            onScanEnter(vEditText, str);
        } else if (enterIndex == 0) {
            vEditText.setText("");
        }
    }
}