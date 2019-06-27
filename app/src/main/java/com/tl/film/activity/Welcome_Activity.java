package com.tl.film.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tl.film.BuildConfig;
import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.dialog.NetDialog;
import com.tl.film.model.EventBus_Model;
import com.tl.film.model.Info_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.servlet.Bind_Servlet;
import com.tl.film.servlet.Get_Info_Servlet;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;
import com.tl.film.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class Welcome_Activity extends Base_Activity {
    private static final String TAG = "Welcome_Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        MyAPP.activity = this;
        //设置版本号
        ((TextView) findViewById(R.id.appversion)).setText(BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyAPP.activity = this;
        init();
    }

    public void init() {
        if (!Tools.isNetworkConnected()) {
            Toast.makeText(this, "没有网络", Toast.LENGTH_SHORT).show();
            NetDialog.showW();
            return;
        }

        //验证本地是否存储tlid信息
        String str = SaveUtils.getString(Save_Key.S_Tlid_Model);
        if (str == null || str.length() < 1) {
            new Bind_Servlet().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return;
        }

        //验证本地tlid信息是否有效
        Tlid_Model model = new Gson().fromJson(SaveUtils.getString(Save_Key.S_Tlid_Model), Tlid_Model.class);
        if (model.getData().getTlid() == null || model.getData().getTlid().length() < 1) {
            new Bind_Servlet().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return;
        }

        //验证是否绑定商户
        if (model.getData().getMerchantCode() == null || model.getData().getMerchantCode().length() < 1) {
            Register_Activity.start(this);
            finish();
            return;
        }

        EventBus_Model model1 = new EventBus_Model();
        model1.setCommand_1(EventBus_Model.CMD_ENTRY_HOME);
        onMessage(model1);

    }

    @Subscribe
    public void onMessage(EventBus_Model model) {
        switch (model.getCommand_1()) {
            case EventBus_Model.CMD_NET_CONNECT:
                init();
                break;
            case EventBus_Model.CMD_ENTRY_HOME:
                new Get_Info_Servlet().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SaveUtils.getString(Save_Key.S_TLID));
                break;
            case EventBus_Model.CMD_BIND_MERT:
                Register_Activity.start(this);
                finish();
                break;
            case EventBus_Model.CMD_BIND_FAIL:
                Toast.makeText(Welcome_Activity.this, "初始化失败，请联系酒店客服！", Toast.LENGTH_SHORT).show();
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
        switch (model.getCode()) {
            case 1000:
                //渠道信息
                if (model.getData().getMerchant() != null) {
                    //二次存储
                    try {
                        Tlid_Model tlid_model = new Gson().fromJson(SaveUtils.getString(Save_Key.S_Tlid_Model), Tlid_Model.class);
                        tlid_model.getData().setMerchantId(model.getData().getTerminal().getMerchantId());
                        tlid_model.getData().setMerchantName(model.getData().getTerminal().getMerchantName());
                        tlid_model.getData().setMerchantCode(model.getData().getTerminal().getMerchantCode());
                        SaveUtils.setString(Save_Key.S_Tlid_Model, new Gson().toJson(tlid_model));
                    } catch (Exception e) {
                        LogUtil.e(TAG,e.getMessage());
                    }
                    //跳转方式
                    if (model.getData().getMerchant().getJumpAction() == 1) {
                        Home_Activity.start(this);
                    } else {
                        Buy_Vip_Activity.start(this);
                    }
                    finish();
                }else {
                    Register_Activity.start(this);
                    finish();
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        MyAPP.activity = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        NetDialog.dismiss();

        super.onDestroy();
    }
}
