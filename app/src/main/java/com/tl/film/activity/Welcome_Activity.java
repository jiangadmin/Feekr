package com.tl.film.activity;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tl.film.BuildConfig;
import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.model.EventBus_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.servlet.Bind_Servlet;
import com.tl.film.servlet.DefTheme_Servlet;
import com.tl.film.servlet.FirstFilms_Servlet;
import com.tl.film.servlet.Update_Servlet;
import com.tl.film.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class Welcome_Activity extends Base_Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        EventBus.getDefault().register(this);

        //设置版本号
        ((TextView) findViewById(R.id.appversion)).setText(BuildConfig.VERSION_NAME);

        //验证本地是否存储tlid信息
        String str = SaveUtils.getString(Save_Key.S_Tlid_Model);
        if(str==null || str.length() < 1 ){
            new Bind_Servlet().execute();
            return;
        }

        //验证本地tlid信息是否有效
        Tlid_Model model = new Gson().fromJson(SaveUtils.getString(Save_Key.S_Tlid_Model), Tlid_Model.class);
        if(model.getData().getTlid() == null || model.getData().getTlid().length() < 1){
            new Bind_Servlet().execute();
            return;
        }

        //验证是否绑定商户
        if(model.getData().getMerchantCode() == null || model.getData().getMerchantCode().length() < 1){
            Register_Activity.start(this);
            finish();
            return;
        }

        //启动主页
        new Handler().postDelayed(() -> {
            Home_Activity.start(this);
            finish();
            }, 1000);
    }

    @Subscribe
    public void onMessage(EventBus_Model model) {
        switch (model.getCommand_1()) {
            case EventBus_Model.CMD_ENTRY_HOME:
                Home_Activity.start(this);
                finish();
                break;
            case EventBus_Model.CMD_BIND_MERT:
                Register_Activity.start(this);
                finish();
                break;
            case EventBus_Model.CMD_BIND_FAIL:
                Toast.makeText(Welcome_Activity.this, "初始化失败，请联系酒店客服！",Toast.LENGTH_SHORT).show();
                break;
        }
        return;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
