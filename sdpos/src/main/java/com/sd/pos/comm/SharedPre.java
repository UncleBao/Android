package com.sd.pos.comm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences 操作类
 * 
 * @author 包子大叔
 * @time 2013-8-15 下午3:36:23
 */
public class SharedPre {

	public static enum Str {
		UserCode, UserPassword,
		APIKey, ServiceURL,
	}

	public static String get(Context context, Str name) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getString(name.name(), "");
	}

	/**
	 * 
	 * @param context
	 * @param name
	 *            : SharedPre.Str
	 * @param defaultValue
	 *            : 默认值
	 * @return
	 */
	public static String get(Context context, Str name, String defaultValue) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getString(name.name(), defaultValue);
	}

	public static int getInt(Context context, Str name) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getInt(name.name(), 0);
	}

	public static Long getLong(Context context, Str name) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getLong(name.name(), 0);
	}

	public static Boolean getBoolean(Context context, Str name) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getBoolean(name.name(), false);
	}

	public static Float getSharedPreFloat(Context context, Str name) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getFloat(name.name(), 0);
	}

	/**
	 * 保存值到SharedPreferences
	 */
	public static void save(Context context, Str name, Object value) {
		SharedPreferences settings = getSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit(); // 取得编辑对象
		if (value instanceof String) {
			editor.putString(name.name(), (String) value);
		} else if (value instanceof Long) {
			editor.putLong(name.name(), (Long) value);
		} else if (value instanceof Integer) {
			editor.putInt(name.name(), (Integer) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(name.name(), (Boolean) value);
		} else if (value instanceof Float) {
			editor.putFloat(name.name(), (Float) value);
		}
		editor.commit();// 提交保存
	}

	/**
	 * 保存字符型
	 * 
	 * @param name
	 *            动态的key
	 * @param value
	 *            保存的value
	 */
	public static void save2(Context context, String name, String value) {
		SharedPreferences settings = getSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit(); // 取得编辑对象
		editor.putString(name, value);
		editor.commit();// 提交保存
	}

	/**
	 * 获取字符型
	 * 
	 * @param name
	 *            动态的key
	 */
	public static String get2(Context context, String name) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getString(name, "");
	}

	// -------------公共方法---------------------------------
	/**
	 * 获取到配置文件
	 * 
	 * @param activity
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		SharedPreferences settings = context.getSharedPreferences("yihujiu", Activity.MODE_PRIVATE);
		return settings;
	}

}
