package com.sd.pos;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.pos.ex.DialogLoading;
import com.sd.pos.ex.DialogOK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class BaseActivity extends Activity {

	protected BaseActivity activity;
	public Resources res; // 通用资源缩写

	// 实例化fragmentmanager类
	FragmentManager fragmentManager = getFragmentManager();

	public TextView vTitlePage; // 标题文本view

	public DialogLoading mDialogLoading; // 加载中对话框,用于网络任务

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;

		res = getResources(); // 通用资源缩写

		// 优化输入法模式
		// 优化输入模式
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		getWindow().setFormat(PixelFormat.RGBA_8888);
	}

	/**
	 * 返回键,结束界面
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (null != mDialogLoading) {
				mDialogLoading.dismiss();
			}
			mDialogLoading = null;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		mDialogLoading = null;
		// if (null != soundPool) {
		// soundPool.release(); //使用静态方式,不能够释放
		// }
		super.onDestroy();
	}

	/**
	 * 加载中对话框(默认)
	 */
	public void showDialogLoading(boolean isShow) {
		if (isShow) {
			if (null == mDialogLoading) {
				mDialogLoading = new DialogLoading(this);
			}
			mDialogLoading.show(); // 显示加载中对话框
		} else {
			if (null != mDialogLoading) {
				if (mDialogLoading.isShowing()) {
					mDialogLoading.dismiss(); // 取消加载中对话框, 对话框要写在
				}
			}
		}
	}

	// -----------------------------------------------声音-----------------------------------


	// ------------------------------------------提示-----------------------------------
	// 只有一个OK按钮的对话框,确定后还没有提示
	public void showDialogOK(int resID) {
		showDialogOK(getStr(resID));
	}

	// 只有一个OK按钮的对话框,确定后还没有提示
	public void showDialogOK(String msg) {
		DialogOK dailog = new DialogOK(this, msg);
		dailog.show();
	}

	// ---------------------------------专用方法-------------------------------//


	// -------------------------------工具方法-------------------------//
	/**
	 * 检查字符串是否是空对象或空字符串
	 * 
	 * @return 为空返回true,不为空返回false
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
	 * @return 为空返回true,不为空返回false
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
	 * @return 为空返回true,不为空返回false
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
	 * @return 为空返回true,不为空返回false
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
	 * @return 为空返回true,不为空返回false
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
	 * @return 为空返回true,不为空返回false
	 */
	public boolean isStr(String str) {
		return !isNull(str);
	}

	/**
	 * 从当前activity跳转到目标activity,<br>
	 * 如果目标activity曾经打开过,就重新展现,<br>
	 * 如果从来没打开过,就新建一个打开
	 * 
	 * @param cls
	 */
	public void gotoExistActivity(Class<?> cls) {
		Intent intent;
		intent = new Intent(this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	/**
	 * 新建一个activity打开
	 * 
	 * @param cls
	 */
	public void gotoActivity(Class<?> cls) {
		Intent intent;
		intent = new Intent(this, cls);
		startActivity(intent);
	}

	public void toast(String msg) {
		toastMy(msg, Toast.LENGTH_SHORT);
	}

	public void toast(int resId) {
		toastMy(res.getString(resId), Toast.LENGTH_SHORT);
	}

	public void toastShort(String msg) {
		toastMy(msg, Toast.LENGTH_SHORT);
	}

	public void toastShort(int resId) {
		toastMy(res.getString(resId), Toast.LENGTH_SHORT);
	}

	public void toastMy(String msg, int duration) {
		Toast.makeText(this, msg, duration).show();
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
	 */
	public String getStr(EditText v) {
		if (null == v) {
			return null;
		}
		return v.getText().toString().trim();
	}

	/**
	 * 从EditText 获取字符串,默认去除前后空格(trim())
	 */
	public String getStr(TextView v) {
		if (null == v) {
			return null;
		}
		return v.getText().toString().trim();
	}

	public String getStr(JSONObject json, String name) throws JSONException {
		if (null == json) {
			return null;
		}
		return json.getString(name);
	}

}
