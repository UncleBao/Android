package com.sd.pos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sd.pos.task.TaskIsHasUser;
import com.sd.pos.util.Util;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.login_sign_in)
    Button vSignIn;
    @Bind(R.id.login_UserCode)
    EditText vUserCode;
    @Bind(R.id.login_UserPassword)
    EditText vPassword;
    @Bind(R.id.login_set)
    ImageView vSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        vSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_sign_in:
                TaskIsHasUser task = new TaskIsHasUser(this, "SP_IsHasUser", getStr(vUserCode), Util.toMD5(getStr(vPassword))) {
                    @Override
                    public void onTaskSuccess(JSONObject jsonObj) {
                        System.out.println("用户存在");
                        doLogin();
                    }

                    @Override
                    public void onTaskFailed(String msg) {
                        if (Util.isNull(msg)) {
                            toast("用户不存在");
                        } else {
                            super.onTaskFailed(msg);
                        }
                    }
                };
                task.execute();
                break;
            case R.id.login_set:
                gotoActivity(LoginSetActivity.class);
                break;
        }
    }

    public void doLogin() {
        TaskIsHasUser task = new TaskIsHasUser(this, "SP_PassIsRight", getStr(vUserCode), Util.toMD5(getStr(vPassword))) {
            @Override
            public void onTaskSuccess(JSONObject jsonObj) {
                System.out.println("登录成功");
                toast("登录成功");
            }

            @Override
            public void onTaskFailed(String msg) {
                if (Util.isNull(msg)) {
                    toast("登录失败");
                } else {
                    super.onTaskFailed(msg);
                }
            }
        };
        task.execute();
    }
}
