package com.tl.film.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.model.Push_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.utils.ExampleUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

public class Jpush_Receiver extends BroadcastReceiver {
    private static final String TAG = "Jpush_Receiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();

        switch (intent.getAction()) {
            case JPushInterface.ACTION_REGISTRATION_ID:
                LogUtil.e(TAG, "JPush 用户注册成功");
                break;
            case JPushInterface.ACTION_CONNECTION_CHANGE:
                LogUtil.e(TAG, "JPush 服务的连接状态发生变化");

                JPushInterface.setAlias(context, 0, SaveUtils.getString(Save_Key.S_TLID));
                break;
            case JPushInterface.ACTION_MESSAGE_RECEIVED:
                Push_Model model;
                try {
                    model = new Gson().fromJson(bundle.get(JPushInterface.EXTRA_MESSAGE).toString(), Push_Model.class);
                    if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_Tlid_Model))) {
                        Tlid_Model model1 = new Gson().fromJson(SaveUtils.getString(Save_Key.S_Tlid_Model), Tlid_Model.class);
                        if (model1.getData().getTlid().equals(model.getData().getTlid())) {
                            EventBus.getDefault().post(model);
                        }
                    } else {
                        EventBus.getDefault().post(model);
                    }

                } catch (Exception e) {
                    LogUtil.e(TAG, "无法解析推送数据：" + bundle.get(JPushInterface.EXTRA_MESSAGE));
                }
                break;
            case JPushInterface.ACTION_NOTIFICATION_RECEIVED:
                LogUtil.d(TAG, "接受到推送下来的通知");
                break;
            case JPushInterface.ACTION_NOTIFICATION_OPENED:
                LogUtil.e(TAG, "用户点击打开了通知");
                break;
            default:
                LogUtil.e(TAG, "onReceive - " + intent.getAction() + ", extras: " + ExampleUtil.printBundle(bundle));
                break;
        }

    }

}
