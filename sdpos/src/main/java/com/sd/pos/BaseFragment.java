package com.sd.pos;

import java.util.List;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.pos.dbhelp.EnumHelper;
import com.sd.pos.ex.DialogOK;

/**
 * 碎片基类
 *
 * @author yi.zhe
 * @time 2014-7-1 下午4:13:46
 */
public abstract class BaseFragment extends Fragment {
    protected View vMain;
    public MainActivity activity;
    protected Resources res; // 通用资源缩写
    public static final String BundleKey1 = "BundleKey1";// 传递参数用的,默认用这个key传,用这个key获取第一个参数
    public static final String BundleKey2 = "BundleKey2";//
    public static final String BundleKey3 = "BundleKey3";//

    protected EnumHelper enumHelper; //枚举值操作类

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == vMain) {
            vMain = inflater.inflate(getLayout(), container, false);
            activity = MainActivity.instans;
            res = getResources(); // 通用资源缩写

            // 优化输入模式
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            enumHelper = new EnumHelper(activity);
            ini();
        }
        return vMain;
    }

    /**
     * 返回子类对应的layout
     */
    protected abstract int getLayout();

    /**
     * 初始化;会在iniMainView函数中被调用到
     */
    protected abstract void ini();

    // list专用,更新布局
    public void updateLayout() {
    }

    // 给Fragment调用
    public void gotoFragment(Fragment fragment) {
        activity.gotoFragment(fragment);
    }

    // -------------------------------工具方法-------------------------//
    protected View findViewById(int id) {
        if (null != vMain) {
            return vMain.findViewById(id);
        } else {
            throw new NullPointerException();
        }
    }

    public void toast(String msg) {
        activity.toastMy(msg, Toast.LENGTH_LONG);
    }

    public void toast(int resId) {
        activity.toastMy(res.getString(resId), Toast.LENGTH_LONG);
    }

    /**
     * 检查字符串是否是空对象或空字符串
     *
     * @return 为空返回true, 不为空返回false
     */
    public boolean isNull(EditText v) {
        String str = v.getText().toString();
        if (null == str || "".equals(str) || "null".equalsIgnoreCase(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查字符串是否是空对象或空字符串
     *
     * @return 为空返回true, 不为空返回false
     */
    public boolean isNull(TextView v) {
        String str = v.getText().toString();
        if (null == str || "".equals(str) || "null".equalsIgnoreCase(str)) {
            return true;
        } else {
            return false;
        }
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

    /**
     * 列表为null 或者 list.size()<=0 返回true
     *
     * @return 为空返回true, 不为空返回false
     */
    public boolean isNull(List<?> list) {
        if (null == list || list.size() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 数组为null 或者 arr.length<=0 返回true
     *
     * @return 为空返回true, 不为空返回false
     */
    public boolean isNull(Object[] arr) {
        if (null == arr || arr.length <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查字符串是否是字符串
     *
     * @param str
     * @return 为空返回true, 不为空返回false
     */
    public boolean isStr(String str) {
        return !isNull(str);
    }

    /**
     * 从资源获取字符串
     *
     * @param resId
     * @return
     */
    public String getStr(int resId) {
        return res.getString(resId);
    }

    /**
     * 从EditText 获取字符串,默认去除前后空格(trim())
     *
     * @return
     */
    public String getStr(EditText v) {
        if (null == v) {
            return null;
        }
        return v.getText().toString().trim();
    }

    /**
     * 从EditText 获取字符串,默认去除前后空格(trim())
     *
     * @return
     */
    public String getStr(TextView v) {
        if (null == v) {
            return null;
        }
        return v.getText().toString().trim();
    }
}
