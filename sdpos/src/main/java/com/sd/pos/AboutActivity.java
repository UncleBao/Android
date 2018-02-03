/*
 * Copyright (C) 2010 恒康信息科技有限公司
 * 版权所有
 */
package com.sd.pos;

import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



/**
 * 关于页面
 * 
 * @author yizhe
 * @date 2014-2-25
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
	TextView vVersion, vVersionCheck, vAdvice;

	public static String ver; // 当前版本号,纯数字:1.0
	String newVersionNo;// 新版本号

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		vVersion = (TextView) findViewById(R.id.about_version);
		vVersionCheck = (TextView) findViewById(R.id.about_version_check);
		vAdvice = (TextView) findViewById(R.id.about_version_advice);

		vVersionCheck.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		vAdvice.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		vVersionCheck.setOnClickListener(this);
		vAdvice.setOnClickListener(this);

		ini();
	}

	void ini() {
		ver = "1.0";
		try {
			ver = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			showDialogOK(e.getMessage());
		}
		vVersion.setText("当前版本 v" + ver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_version_check:
			break;
		case R.id.about_version_advice:
			break;
		default:
			break;
		}
	}
}