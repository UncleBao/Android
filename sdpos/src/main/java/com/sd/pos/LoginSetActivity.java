package com.sd.pos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sd.pos.comm.Config;
import com.sd.pos.comm.SharedPre;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginSetActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.login_set_apikey)
    EditText vApiKey;
    @Bind(R.id.login_set_service_url)
    EditText vUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_set);
        ButterKnife.bind(this);

        if (isNull(Config.ApiKey)) {
        } else {
            vApiKey.setText(Config.ApiKey);
        }
        if (isNull(Config.URL)) {
        } else {
            vUrl.setText(Config.URL);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_set_back:
                this.finish();
                break;
            case R.id.login_set_save:
                SharedPre.save(this, SharedPre.Str.APIKey, getStr(vApiKey));
                Config.URL = getStr(vUrl);
                SharedPre.save(this, SharedPre.Str.ServiceURL, getStr(vUrl));
                break;
        }
    }

}
