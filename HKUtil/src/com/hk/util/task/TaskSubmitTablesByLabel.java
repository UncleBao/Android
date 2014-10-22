package com.hk.util.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hk.util.TaskBase;

import android.app.Activity;

/**
 * 提交多个表格,最多三个表格
 * 
 * @author 包子大叔
 * @time 2013-9-27 上午10:07:28
 */
public abstract class TaskSubmitTablesByLabel extends TaskBase {
	String label;
	String[] paramArray;
	JSONArray table0, table1, table2;

	/**
	 * @param jsonArray
	 *            参数列表,已经封装好的json二维数组(必须是二维数组哦)
	 */
	public TaskSubmitTablesByLabel(Activity activity, String label,
			String[] params, JSONArray table0, JSONArray table1,
			JSONArray table2) {
		super(activity);
		this.label = label;
		this.paramArray = params;
		this.table0 = table0;
		this.table1 = table1;
		this.table2 = table2;
	}

	// 选择列表项后,本函数输出结果
	public abstract void onTaskOver(boolean isOk, boolean isAsk, String msg,
			ArrayList<String> msgList);

	@Override
	protected String doInThread() {
		return HKNet.submitTablesByLabel(label, paramArray, table0,
				table1, table2);
	}

	@Override
	protected void onTaskFinish(String result) {
		try {
			JSONObject jsonObj = new JSONObject(result);

			onTaskOver(HKNet.isNetWorkSuccess(jsonObj),
					HKNet.getIsAsk(jsonObj), HKNet.getMsg(jsonObj),
					HKNet.getMsgList(jsonObj));
		} catch (JSONException e) {
			onTaskOver(false, false, e.toString(), null);
			toast("解析数据失败:" + e.toString());
			System.out
					.println("解析数据失败:" + e.toString() + "\n result:" + result);
		}
	}

}
