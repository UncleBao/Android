package com.hk.util.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.hk.util.HKDialog1;
import com.hk.util.TaskBase;
import com.hk.util.base.Util;
import com.hk.util.hktable.DataTable;

/**
 * 获取多个表格
 * 
 * @author 包子大叔
 * @time 2013-9-27 上午10:07:28
 */
public abstract class TaskGetWhenNotLoginedIn extends TaskBase {
	String label;
	String paramStr = "";
	String[] paramArray;

	public TaskGetWhenNotLoginedIn(Activity activity, String label, String[] params) {
		super(activity);
		this.label = label;
		this.paramArray = params;
	}

	public void execute() {
		if (Util.isNull(label)) {
			toast("调用服务失败,资源代码为空,请联系系统管理员.");
			return;
		}

		String result = null;
		if (Util.isNull(result)) {
			super.execute();
		} else {
			onTaskFinish(result);
		}
	}

	// 选择列表项后,本函数输出结果
	public abstract void onTaskSuccess(DataTable[] ds, boolean isAsk, String msg, ArrayList<String> msgList);

	public void onTaskFailed(JSONObject result, String message) {
		if (Util.isNull(message)) {
			message = "获取任务失败!";
		}
		HKDialog1 dailog = new HKDialog1(activity, message);
		dailog.show();
	}

	DataTable createDataTable(JSONObject jsonObj, String listName) {
		try {
			JSONArray jsonArr = jsonObj.getJSONArray(listName);
			if (null != jsonArr && jsonArr.length() > 0) {
				return new DataTable(jsonArr);
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("生成表格失败:" + listName + ":" + e.getMessage());
			return null;
		}
	}

	@Override
	protected String doInThread() {
		return HKNet.getWhenNotLoginedIn(label, paramArray);
	}

	@Override
	protected void onTaskFinish(String result) {
		try {
			JSONObject jsonObj = new JSONObject(result);
			if (HKNet.isNetWorkSuccess(jsonObj)) {
				DataTable dt0 = createDataTable(jsonObj, "list0");
				DataTable dt1 = createDataTable(jsonObj, "list1");
				DataTable dt2 = createDataTable(jsonObj, "list2");
				DataTable dt3 = createDataTable(jsonObj, "list3");
				DataTable dt4 = createDataTable(jsonObj, "list4");
				DataTable dt5 = createDataTable(jsonObj, "list5");
				DataTable dt6 = createDataTable(jsonObj, "list6");
				DataTable dt7 = createDataTable(jsonObj, "list7");
				DataTable dt8 = createDataTable(jsonObj, "list8");
				DataTable dt9 = createDataTable(jsonObj, "list9");

				DataTable[] dataSet = new DataTable[] { dt0, dt1, dt2, dt3, dt4, dt5, dt6, dt7, dt8, dt9 };

				onTaskSuccess(dataSet, HKNet.getIsAsk(jsonObj), HKNet.getMsg(jsonObj),
						HKNet.getMsgList(jsonObj));
			} else {
				onTaskFailed(jsonObj, HKNet.getMsg(jsonObj));
			}
		} catch (JSONException e) {
			onTaskFailed(null, "数据解析失败!");
			toast("解析数据失败:" + e.toString());
			System.out.println("解析数据失败:" + e.toString() + "\n result:" + result);
		}
	}
}
