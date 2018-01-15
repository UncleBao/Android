/*
 * Copyright (C) 2010 恒康信息科技有限公司
 * 版权所有
 */
package com.sd.pos.ex;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sd.pos.R;
import com.yihujiu.util.table.DataRow;
import com.yihujiu.util.table.DataTable;
import com.yihujiu.util.Util;
import com.yihujiu.util.view.CommonTableAdapter;
import com.yihujiu.util.view.ViewHolder;

/**
 * 通用弹出菜单<br>
 * 适应数据类型: String[] 或 ArrayList<String>
 *
 * @author yizhe
 * @date 2012-6-5
 */
public abstract class SelectWithTable extends Dialog implements OnItemClickListener,
        View.OnClickListener {
    protected Activity activity;
    private TextView titleView;
    protected Button vRefresh, vClear;
    private ListView vList;
    private CommonTableAdapter adapter;
    protected DataTable table;
    private String idColumn, nameColumn;

    protected boolean isVisibility = true;

    String title;
    protected boolean isShowRefreshButton = false;

    public SelectWithTable(Activity activity, DataTable table, String idColumn, String nameColumn) {
        super(activity);
        this.activity = activity;
        this.table = table;
        this.idColumn = idColumn;
        this.nameColumn = nameColumn;
    }

    public SelectWithTable(Activity activity, String title, DataTable table, String idColumn, String colName) {
        this(activity, table, idColumn, colName);
        this.title = title;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.PROGRESS_VISIBILITY_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_menu);

        titleView = (TextView) findViewById(R.id.hk_pop_menu_title);
        vClear = (Button) findViewById(R.id.hk_pop_menu_clear);
        vRefresh = (Button) findViewById(R.id.hk_pop_menu_refresh);
        vList = (ListView) findViewById(R.id.hk_pop_menu_list);

        vRefresh.setOnClickListener(this);
        vClear.setOnClickListener(this);
        vList.setOnItemClickListener(this);

//        vClear.setVisibility(View.VISIBLE);
        setVisibility4vClear();
        if (isShowRefreshButton) {
            vRefresh.setVisibility(View.VISIBLE);
        }

        if (null != title) {
            titleView.setText(title);
        }

        LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        if (null != p) {
            p.width = Util.dip2px(activity, 310);
            // p.width = (int) (Config.screenWidth * 0.9);
        }

        setCanceledOnTouchOutside(true);

        if (null == table) {
            return;
        }
        adapter = new CommonTableAdapter(activity, table, R.layout.i_col1) {
            @Override
            public void convert(ViewHolder holder, int position, DataRow item) {
                holder.setText(R.id.i_col1_info1, item.get(nameColumn));
            }
        };
        vList.setAdapter(adapter);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.dismiss();
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataRow dr = table.rows.get(position);
        onItemClick(position, dr, dr.get(idColumn), dr.get(nameColumn));
        this.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hk_pop_menu_refresh:
                table = null;
                adapter = null;
                vList.setAdapter(null);
                this.hide();
                onRefresh();
                break;
            case R.id.hk_pop_menu_clear:
                this.hide();
                onItemClick(-1, null, "", "");
                break;
            default:
                break;
        }
    }

    //设置清除按钮的可见度,因为在一些情景下的对话框清除按钮不需要用到
    protected void setVisibility4vClear() {
        if (isVisibility) {
            vClear.setVisibility(View.VISIBLE);
        } else {
            vClear.setVisibility(View.GONE);
        }
    }


    public abstract void onItemClick(int menuIndex, DataRow dr, String id, String name);

    protected abstract void onRefresh();

}
