package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.model.EventBus_Model;
import com.tl.film.model.Info_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.servlet.Get_Info_Servlet;
import com.tl.film.servlet.Register_Servlet;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class Register_Activity extends AppCompatActivity {
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
        MyAPP.AddActivity(this);
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
        MyAPP.AppExit();
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
                new Get_Info_Servlet().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SaveUtils.getString(Save_Key.S_TLID));

                break;
            case EventBus_Model.CMD_BIND_MERT_FAIL:
                String msg = "渠道绑定失败";
                if (eb.getData() != null) {
                    msg = (String) eb.getData();
                }
                Toast.makeText(Register_Activity.this, msg, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /**
     * 设备信息
     *
     * @param model
     */
    @Subscribe
    public void onMessage(Info_Model model) {
        if (model.getCode() == 1000) {
            if (model.getData().getMerchant() != null) {
                //二次存储
                try {
                    Tlid_Model tlid_model = new Gson().fromJson(SaveUtils.getString(Save_Key.S_Tlid_Model), Tlid_Model.class);
                    tlid_model.getData().setMerchantId(model.getData().getTerminal().getMerchantId());
                    tlid_model.getData().setMerchantName(model.getData().getTerminal().getMerchantName());
                    tlid_model.getData().setMerchantCode(model.getData().getTerminal().getMerchantCode());
                    SaveUtils.setString(Save_Key.S_Tlid_Model, new Gson().toJson(tlid_model));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                }

                MyAPP.finishActivity();
                if (model.getData().getMerchant().getJumpAction() == 1) {
                    Home_Activity.start(this);
                } else {
                    Buy_Vip_Activity.start(this);
                }
//                finish();
            }
        }
    }

}
