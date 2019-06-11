package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.model.Base_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.servlet.Bind_Servlet;
import com.tl.film.servlet.Register_Servlet;
import com.tl.film.utils.SaveUtils;

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

        findViewById(R.id.submit).setOnClickListener(v -> {
            String tlsh = ((TextView) findViewById(R.id.tlsh)).getText().toString();
            if (!TextUtils.isEmpty(tlsh)) {
                new Register_Servlet(this).execute(tlsh);
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
        MyAPP.register_activity = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        MyAPP.register_activity = this;
        super.onResume();
    }

    @Override
    protected void onStop() {
        MyAPP.register_activity = null;
        super.onStop();
    }

    public void onMessage(Tlid_Model model) {
        Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
        switch (model.getCode()) {
            case 1000:
            case 13406:
                SaveUtils.setString(Save_Key.S_Tlid_Model, new Gson().toJson(model));
                Home_Activity.start(this);
                finish();
                break;
        }
    }

}
