package com.tl.film;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.widget.Toast;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * @author jiangyao
 * date: 2019/3/25
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: Application
 */
public class MyAPP extends Application {
    private static final String TAG = "MyAPP";

    // MI.
    private static final String APP_ID = "2882303761517970521";
    private static final String APP_KEY = "5251797038521";

    private static ApplicationHandler sHandler = null;

    public static final boolean LogShow = true;

    @Override
    public void onCreate() {
        super.onCreate();
        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }

        if (sHandler == null) {
            sHandler = new ApplicationHandler(getApplicationContext());
        }

    }


    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static void reInitPush(Context ctx) {
        MiPushClient.registerPush(ctx.getApplicationContext(), APP_ID, APP_KEY);
    }

    public static ApplicationHandler getHandler() {
        return sHandler;
    }


    public static class ApplicationHandler extends Handler {

        private Context context;

        public ApplicationHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;

            if (!TextUtils.isEmpty(s)) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        }
    }
}
