package com.tl.film.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.model.EventBus_Model;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.Tools;

import org.greenrobot.eventbus.EventBus;

/**
 * @author jiangadmin
 * date: 2017/8/29.
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 网络状态提示
 */

public class NetDialog {
    private static final String TAG = "NetDialog";
    private static NetWarningDialog netWarningDialog;
    private static NetLoadingDialog netLoadingDialog;

    static TimeCount timeCount;

    /**
     * 显示警告框
     */
    public static void showW() {
        if (MyAPP.activity != null){
            if (netWarningDialog == null){
                try {
                    netWarningDialog = new NetWarningDialog(MyAPP.activity);
                    netWarningDialog.show();
                    LogUtil.e(TAG,"显示");
                } catch (RuntimeException e) {
                    LogUtil.e(TAG,e.getMessage());
                }
            }else {
                try {
                    netWarningDialog.show();
                    LogUtil.e(TAG,"显示");
                } catch (RuntimeException e) {
                    LogUtil.e(TAG,e.getMessage());
                }
            }
        }
    }

    /**
     * 显示等待框
     */
    public static void showL() {
        if (MyAPP.activity != null && netLoadingDialog == null) {
            netLoadingDialog = new NetLoadingDialog(MyAPP.activity);
            netLoadingDialog.show();

            timeCount = new TimeCount(30000, 1000);
            timeCount.start();
        }
    }

    /**
     * 关闭
     */
    public static void dismiss() {
        try {
            //关闭等待框
            if (netLoadingDialog != null) {
                netLoadingDialog.dismiss();
                netLoadingDialog = null;
            }
            //关闭警告框
            if (netWarningDialog != null) {
                netWarningDialog.dismiss();
                netWarningDialog = null;
            }

        } catch (Exception e) {
        }
    }

    /**
     * 警告框
     */
    public static class NetWarningDialog extends Dialog {
        public NetWarningDialog(@NonNull Context context) {
            super(context, R.style.DefDialog);
        }

        Button esc, setting;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_netwarning);
            setCancelable(false);

            esc = findViewById(R.id.dialog_esc);
            setting = findViewById(R.id.dialog_setting);

            esc.setOnClickListener(v -> System.exit(0));
            setting.setOnClickListener(v -> getContext().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));
        }
    }

    /**
     * 等待框
     */
    public static class NetLoadingDialog extends Dialog {
        public NetLoadingDialog(@NonNull Context context) {
            super(context, R.style.MyDialog);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.dialog_netloading);
        }
    }


    /**
     * 计时器
     */
    static class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        //倒计时完成
        @Override
        public void onFinish() {
            //等待框依旧没关闭
            if (netLoadingDialog != null) {
                //关闭等待框
                dismiss();
                //判断网络
                if (!Tools.isNetworkConnected())
                    showW();
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示

        }
    }
}
