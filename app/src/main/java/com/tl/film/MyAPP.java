package com.tl.film;

import android.app.ActivityManager;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.widget.Toast;

import com.tl.film.utils.LogUtil;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

        LogUtil.e(TAG, "BLE MAC:" + getBtAddressByReflection());

    }


    public static String getBtAddressByReflection() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Field field = null;
        try {
            field = BluetoothAdapter.class.getDeclaredField("mService");
            field.setAccessible(true);
            Object bluetoothManagerService = field.get(bluetoothAdapter);
            if (bluetoothManagerService == null) {
                return null;
            }
            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
            if (method != null) {
                Object obj = method.invoke(bluetoothManagerService);
                if (obj != null) {
                    return obj.toString();
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

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
                LogUtil.e(TAG,s);
            }
        }
    }
}
