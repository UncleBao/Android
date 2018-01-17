package com.sd.pos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sd.pos.comm.Config;
import com.sd.pos.pages.PagePos;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.main_nav_pos)
    ToggleButton vPos;
    @Bind(R.id.main_nav_report)
    ToggleButton vReport;
    @Bind(R.id.main_nav_about)
    ToggleButton vAbout;
    @Bind(R.id.main_nav_bill)
    ToggleButton vBill;
    @Bind(R.id.main_nav_check)
    ToggleButton vCheck;
    @Bind(R.id.main_nav_set)
    ToggleButton vSet;
    @Bind(R.id.main_nav_info)
    ToggleButton vInfo;
    @Bind(R.id.main_nav_inv)
    ToggleButton vInv;

    @Bind(R.id.main_top_info2)
    TextView vInfo2;


    public static MainActivity instans;

    BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instans = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        vInfo2.setText("当前操作员:" + Config.UserCode);
        showDetail(0);
    }

    // 给Fragment调用
    public void gotoFragment(Fragment fragment) {
        currentFragment = (BaseFragment) fragment;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.replace(R.id.main_detail, fragment);
        }
        ft.addToBackStack(null);// 添加到返回堆栈
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    // 本类调用,或者子类调用
    protected void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.replace(R.id.main_detail, fragment);
        }
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_nav_pos:
                showDetail(0);
                break;
            case R.id.main_nav_info:
                showDetail(1);
                break;
            case R.id.main_nav_bill:
                showDetail(2);
                break;
            case R.id.main_nav_inv:
                showDetail(3);
                break;
            case R.id.main_nav_check:
                showDetail(4);
                break;
            case R.id.main_nav_set:
                showDetail(5);
                break;
            case R.id.main_nav_report:
                showDetail(6);
                break;
            case R.id.main_nav_about:
                showDetail(7);
                break;
            default:
                break;
        }
    }

    private void showDetail(int i) {
        //保存缓存标志,到这里肯定是缓存成功了
        vPos.setChecked(false);
        vBill.setChecked(false);
        vCheck.setChecked(false);
        vReport.setChecked(false);
        vSet.setChecked(false);
        vInfo.setChecked(false);
        vInv.setChecked(false);
        vAbout.setChecked(false);

        int count = fragmentManager.getBackStackEntryCount();
        for (int k = 0; k < count; k++) {
            fragmentManager.popBackStack();
        }
        switch (i) {
            case 0:
                vPos.setChecked(true);
                replaceFragment(getPostPage());
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                vAbout.setChecked(true);
                //replaceFragment(getFrReport());
                break;
            default:
                break;
        }
    }

    PagePos pagePos;

    private Fragment getPostPage() {
        if (null == pagePos) {
            pagePos = new PagePos();
            pagePos.setRetainInstance(true);
        }
        return pagePos;
    }

}
