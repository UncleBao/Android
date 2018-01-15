package com.sd.pos.ex;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.sd.pos.R;
import com.yihujiu.util.Util;
import com.yihujiu.util.view.CommonAdapter;
import com.yihujiu.util.view.ViewHolder;

import java.util.ArrayList;

/**
 * 通用弹出菜单
 *
 * @author yizhe
 * @date 2012-6-5
 */
public abstract class SelectMenu extends Dialog implements OnItemClickListener {
    Context context;
    private TextView titleView;
    private ListView vList;
    public CommonAdapter<String> adapter;
    int mTextSize = 20;// 菜单项字体大小,单位pd

    ArrayList<String> list;
    String title;

    public SelectMenu(Context context, String[] arr) {
        super(context);
        this.context = context;
        list = new ArrayList<String>();
        for (String str : arr) {
            list.add(str);
        }
    }

    /**
     * 创建对话框,使用默认标题 R.string.dialog_defaultTitle
     */
    public SelectMenu(Context context, ArrayList<String> list) {
        super(context);
        this.context = context;
        this.list = list;
    }

    /**
     * 创建对话框,使用默认标题 R.string.dialog_defaultTitle
     */
    public SelectMenu(Context context, String title, ArrayList<String> list) {
        super(context);
        this.context = context;
        this.title = title;
        this.list = list;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.PROGRESS_VISIBILITY_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_menu);

        titleView = (TextView) findViewById(R.id.hk_pop_menu_title);
        vList = (ListView) findViewById(R.id.hk_pop_menu_list);

        vList.setOnItemClickListener(this);

        if (null != title) {
            titleView.setText(title);
        }

        LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        if (null != p) {
            p.width = Util.dip2px(context, 310);
            //p.width = (int) (Comm.screenWidth * 0.9);
        }

        ini();
    }

    // 初始化,设置按钮的可见性,文本等,会在onCreate函数中的最后调用
    private void ini() {
        adapter = new CommonAdapter<String>(context, list, R.layout.i_col1) {
            @Override
            public void convert(ViewHolder holder, String item) {
                holder.setText(R.id.i_col1_info1, item);
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
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        onItemClick(position);
        this.dismiss();
    }

    public abstract void onItemClick(int position);

}

//  //调用举例
//  HKPopMenu menu;
//
//	void showMenu() {
//		if (null == menu) {
//			ArrayList<String> arr = new ArrayList<String>();
//			arr.add("123");
//			arr.add("ssssf");
//			menu = new HKPopMenu(this, arr) {
//				public void onItemClick(android.widget.AdapterView<?> parent,
//										View view, int position, long id) {
//					this.dismiss();
//					switch (position) {
//						case 0:
//							//
//							break;
//						case 1:
//							//
//						default:
//							break;
//					}
//				};
//			};
//		}
//		menu.show();
//	}
