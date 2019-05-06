package com.tl.film.receiver;

import android.content.Context;

import com.tl.film.utils.LogUtil;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class Jpush_Message_Receiver extends JPushMessageReceiver {
    private static final String TAG = "Jpush_Message_Receiver";

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        LogUtil.e(TAG, "序列号：" + jPushMessage.getSequence());
        LogUtil.e(TAG, "别名：" + jPushMessage.getAlias());
        LogUtil.e(TAG, "错误码：" + jPushMessage.getErrorCode());

    }


}
