package com.tl.film;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.text.TextUtils;

import com.tl.film.model.Const;
import com.tl.film.model.Save_Key;
import com.tl.film.servlet.Bind_Servlet;
import com.tl.film.servlet.Get_MyIP_Servlet;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @author jiangyao
 * date: 2019/3/25
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: Application
 */
public class MyAPP extends Application {
    private static final String TAG = "MyAPP";

    private static Context context;

    public static final boolean LogShow = true;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush


        new Get_MyIP_Servlet().execute();

        if (TextUtils.isEmpty(Const.TLID)) {
            if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_TLID))) {
                Const.TLID = SaveUtils.getString(Save_Key.S_TLID);
            }
        }
//        SaveUtils.setString(Save_Key.S_TLID, "8FEBC4ADB5CCD5235FD2CA97DA89F2C8");

        if (TextUtils.isEmpty(Const.TLID) && TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_TLID))) {
            LogUtil.e(TAG, "绑定设备");
            new Bind_Servlet().execute(getMacAddress());
        } else {
            LogUtil.e(TAG, "" + Const.TLID);
            LogUtil.e(TAG, "" + SaveUtils.getString(Save_Key.S_TLID));

        }
    }

    /**
     * 获取网络的MAC地址
     *
     * @return
     */
    public static String getMacAddress() {

        String macAddress;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
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


    /**
     * 当前IP
     *
     * @return
     */
    public static String getIp() {
        String ip = SaveUtils.getString(Save_Key.S_IP);

        if (TextUtils.isEmpty(ip)) {
            return "127.0.0.1";
        } else {
            return ip;
        }
    }
}
