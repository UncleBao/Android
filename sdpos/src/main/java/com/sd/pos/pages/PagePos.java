package com.sd.pos.pages;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sd.pos.BaseFragment;
import com.sd.pos.R;
import com.sd.pos.comm.Config;
import com.sd.pos.ex.PopupMenu;
import com.sd.pos.ex.TextWatcher4Scan;
import com.sd.pos.task.TaskGetSTKList;

import java.util.ArrayList;

/**
 * POS
 */
public class PagePos extends BaseFragment implements OnClickListener {

    TextView vStock;
    EditText vUser, vBarcode;
    ListView vList;

    ArrayList<String> StockList;

    String StockCode;

    @Override
    protected int getLayout() {
        return R.layout.page_pos;
    }

    @Override
    protected void ini() {
        vStock = (TextView) findViewById(R.id.page_pos_stock);
        vUser = (EditText) findViewById(R.id.page_pos_user);
        vBarcode = (EditText) findViewById(R.id.page_pos_barcode);
        vList = (ListView) findViewById(R.id.page_pos_list);

        vStock.setOnClickListener(this);
        vBarcode.addTextChangedListener(new TextWatcher4Scan(vBarcode) {
            @Override
            public void onScanEnter(EditText v, String str) {
                //根据条码获取数据
                System.out.println(str);
            }
        });

        vUser.setText(Config.UserCode);
        vBarcode.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //获取仓库列表
    public void GetStockList() {
        TaskGetSTKList taskGetSTKList = new TaskGetSTKList(activity, Config.UserCode) {
            @Override
            public void onTaskSuccess(ArrayList<String> list) {
                StockList = list;
                if (!isNull(StockList)) {
                    ShowStockSelect();
                }
            }
        };
        taskGetSTKList.execute();
    }

    //显示仓库列表
    private void ShowStockSelect() {
        if (isNull(StockList)) {
            GetStockList();
            return;
        }
        PopupMenu popup = new PopupMenu(activity, StockList) {
            @Override
            public void onItemClick(int position) {
                vStock.setText(StockList.get(position));
            }
        };
        popup.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.page_pos_stock:
                ShowStockSelect();
                break;
            case R.id.page_pos_CreateBill:
                break;
            case R.id.page_pos_GetBill:
                break;
            case R.id.page_pos_SaveBill:
                break;
            case R.id.page_pos_CommitBill:
                break;
        }
    }
}
