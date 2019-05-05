package com.tl.film.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tl.film.utils.ExampleUtil;
import com.tl.film.utils.LogUtil;

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
        LogUtil.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + ExampleUtil.printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            LogUtil.d(TAG, "JPush 用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtil.d(TAG, "接受到推送下来的自定义消息" + bundle.get(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtil.d(TAG, "接受到推送下来的通知");

//            receivingNotification(context,bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtil.d(TAG, "用户点击打开了通知");

//            openNotification(context,bundle);

        } else {
            LogUtil.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
