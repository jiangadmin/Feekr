package com.tl.film.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tl.film.dialog.NetDialog;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.Tools;

/**
 * @author jiangadmin
 * date: 2017/7/15.
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 网络变化广播监听
 */

public class NetReceiver extends BroadcastReceiver {
    private static final String TAG = "NetReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    LogUtil.e(TAG, "有线网络");

                    if (Tools.isNetworkConnected())
                        NetDialog.dismiss();

                }
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    LogUtil.e(TAG, "无线网络");

                    if (Tools.isNetworkConnected())
                        NetDialog.dismiss();
                }
            } else {
                LogUtil.e(TAG, "网络断开");
                NetDialog.showW();
            }
        }
    }
}
