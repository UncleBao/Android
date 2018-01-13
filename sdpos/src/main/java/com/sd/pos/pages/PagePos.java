package com.sd.pos.pages;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sd.pos.BaseFragment;
import com.sd.pos.R;

/**
 * POS
 *
 * @author yi.zhe
 */
public class PagePos extends BaseFragment implements OnClickListener {

    ToggleButton btSortDefault, btSortHot;
    ToggleButton btSortEvaluate; // 评价(推荐)
    ToggleButton btModeGrid, btModeList;
    EditText vSearchKey;
    Button btRefresh, btFilter, btEditCancel, btEditDelete;
    ListView vList;
    GridView vGrid;
    TextView vInfo1, vInfo2, vInfo3;

    int mSelectionPosition = 0;

    // AND ( StyleCode LIKE '%%' OR StyleName LIKE '%%' )
    String sqlAndLike = "";
    String sqlAndFilter = "";

    String sqlOrder = ""; // Order By
    int sortType = 1; // 1:默认, 2:价格, 3:人气

    boolean isEditMode = false;

    @Override
    protected int getLayout() {
        return R.layout.page_pos;
    }

    @Override
    protected void ini() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 1:
                break;
        }
    }
}
