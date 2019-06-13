package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.model.EventBus_Model;
import com.tl.film.servlet.Register_Servlet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class Register_Activity extends Base_Activity {
    private static final String TAG = "Register_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Register_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        findViewById(R.id.submit).setOnClickListener(v -> {
            String tlsh = ((TextView) findViewById(R.id.tlsh)).getText().toString();
            if (!TextUtils.isEmpty(tlsh)) {
                new Register_Servlet().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tlsh);
            } else {
                Toast.makeText(this, "请输入渠道号", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        MyAPP.activity.finish();
        finish();
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    @Subscribe
    public void onMessage(EventBus_Model eb) {
        switch (eb.getCommand_1()) {
            case EventBus_Model.CMD_ENTRY_HOME:
                Home_Activity.start(this);
                finish();
                break;
            case EventBus_Model.CMD_BIND_MERT_FAIL:
                String msg = "渠道绑定失败";
                if (eb.getData() != null) {
                    msg = (String) eb.getData();
                }
                Toast.makeText(Register_Activity.this, msg, Toast.LENGTH_SHORT).show();
                break;
        }
        return;
    }

}
