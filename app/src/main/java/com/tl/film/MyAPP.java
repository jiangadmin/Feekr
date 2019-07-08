package com.tl.film;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.ktcp.video.thirdagent.JsonUtils;
import com.ktcp.video.thirdagent.KtcpContants;
import com.ktcp.video.thirdagent.KtcpPaySDKCallback;
import com.ktcp.video.thirdagent.KtcpPaySdkProxy;
import com.tl.film.model.Save_Key;
import com.tl.film.servlet.Get_MyIP_Servlet;
import com.tl.film.servlet.Get_Vuid_Servlet;
import com.tl.film.servlet.VIPCallBack_Servlet;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;

/**
 * @author jiangyao
 * date: 2019/3/25
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: Application
 */
public class MyAPP extends Application implements KtcpPaySDKCallback {
    private static final String TAG = "MyAPP";

    private static Context context;

    public static final boolean LogShow = true;

    public static Context getContext() {
        return context;
    }

    private static List<Activity> activities;

    //记录Activity的总个数
    public int count = 0;

    public static Stack<Activity> store;

    public static boolean isShow = true;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        store = new Stack<>();

        registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());


        new Get_MyIP_Servlet().execute();

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

        //添加监听
        KtcpPaySdkProxy.getInstance().setPaySDKCallback(this);

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

    /**
     * @param channel 三方厂商对应的渠道号
     * @param extra   包含guid,QUA，TVPlatform等字段的json字符串
     */
    @Override
    public void doLogin(String channel, String extra) {
        LogUtil.e(TAG, "获取vuid开始" + extra);
        String guid = "";
        //解析guid
        if (extra != null && extra.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(extra);
                guid = jsonObject.getString("guid");
            } catch (Exception e) {
                LogUtil.e(TAG, "解析guid报错" + e.getMessage());
            }
        }
        LogUtil.e(TAG, "guid = " + guid);

        //请求获取vuid
        new Get_Vuid_Servlet().execute(guid);
    }

    /**
     * 订单处理  需要腾讯处理的错误码和提示请沟通好通知处理
     *
     * @param vuserId
     * @param productId
     * @param extra
     */
    @Override
    public void doOrder(String vuserId, String productId, String extra) {
        LogUtil.e(TAG, "extra:" + extra);
        // status 成功返回0 失败返回对应错误码 厂商业务错误 fixme  腾旅 902xxx 例如902101订购失败
        // msg  错误提示
        // data json数据 { "vuid":"","extra":{"orderId":""}}

        //订单返回结果示例
        HashMap<String, Object> params = new HashMap<>();
        int status = 0;//0 订单处理成功 非0失败
        String msg = "make order success";
        params.put("vuid", vuserId);

        JSONObject orderJson;
        try {
            orderJson = new JSONObject();
            orderJson.put("orderId", "orderId12345678");
        } catch (JSONException e) {
            e.printStackTrace();
            orderJson = new JSONObject();
        }
        params.put("extra", orderJson);

        KtcpPaySdkProxy.getInstance().onOrderResponse(status, msg, JsonUtils.addJsonValue(params));

    }

    /**
     * 接收来自腾讯的通知事件
     *
     * @param eventId 1 接收应用启动事件 由三方APP决定登录时机发起登录 2 帐号登录回调  3 帐号退出回调 4 APP退出回调
     * @param params
     */
    @Override
    public void onEvent(int eventId, String params) {
        LogUtil.e(TAG, "eventId:" + eventId + "params:" + params);
        switch (eventId) {
            //接收应用启动事件 由三方APP决定登录时机发起登录
            case 1:
                KtcpPaySdkProxy.getInstance().onEventResponse(KtcpContants.EVENT_ACCOUNT_LOGIN, "");
                break;
            //帐号登录回调
            case 2:
                onTencentLoginEvent(eventId, params);
                break;
            case 3://退出登录回调
                onTencentLogoutEvent(eventId, params);
                break;
            case 4://APP退出
                onTencentExitEvent(eventId, params);
                break;
            default:
                break;
        }
        return;
    }


    /**
     * 腾讯APK APP退出事件
     *
     * @param eventId
     * @param params
     */
    public void onTencentExitEvent(int eventId, String params) {
        LogUtil.i(TAG, "onTencentExitEvent=" + params);
        try {
            if (params != null && params.length() > 0) {
                //创建回调参数
                Map<String, String> map = new HashMap<>();

                JSONObject extraObj = JsonUtils.getJsonObj(params);
                map.put("eventId", String.valueOf(eventId));
                map.put("vuid", extraObj.getString("vuid"));
                map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
                map.put("msg", extraObj.getString("msg"));
                new VIPCallBack_Servlet().execute(map);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }

    /**
     * 腾讯APK 账户登出（暂不处理）
     *
     * @param eventId
     * @param params
     */
    public void onTencentLogoutEvent(int eventId, String params) {
        LogUtil.i(TAG, "onTencentLogoutEvent=" + params);
        try {

        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }

    /**
     * 腾讯APK 登录登录接口回调事件
     *
     * @param eventId
     * @param params
     */
    public void onTencentLoginEvent(int eventId, String params) {
        LogUtil.i(TAG, "onTencentLoginEvent=" + params);
        try {
            if (params != null && params.length() > 0) {
                //创建回调参数
                Map<String, String> map = new HashMap<>();

                //解析腾讯登录回调结果
                JSONObject extraObj = JsonUtils.getJsonObj(params);
                map.put("eventId", String.valueOf(eventId));
                map.put("vuid", extraObj.getString("vuid"));
                map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
                map.put("code", extraObj.getString("code"));
                map.put("msg", extraObj.getString("msg"));
                map.put("vuSession", extraObj.getString("vuSession"));
                new VIPCallBack_Servlet().execute(map);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }


    /**
     * 添加Activity到栈
     *
     * @param activity
     */
    public static void AddActivity(Activity activity) {
        if (activities == null) {
            activities = new ArrayList<>();
        }

        if (QueryActivity(activity.getClass())) {
            activities.remove(activity);
        }

        activities.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (activities != null && activities.size() > 0) {
            return activities.get(activities.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishActivity() {
        if (activities != null && activities.size() > 0) {
            finishActivity(activities.get(activities.size() - 1));
        } else {
            LogUtil.e(TAG, "无法关闭");
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */

    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activities.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束其他Activity
     */
    public static void finishOtherActivity(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (!activity.getClass().equals(cls)) {
                    activities.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(cls)) {
                    activities.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 查询栈中是否有这个
     *
     * @param cls
     */
    public static boolean QueryActivity(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (int i = 0, size = activities.size(); i < size; i++) {
            if (null != activities.get(i)) {
                activities.get(i).finish();
            }
        }
        activities.clear();

        System.exit(0);
    }

    /**
     * 退出应用程序
     */
    public static void AppExit() {
        try {
            LogUtil.e(TAG, "走正常关闭流程");
            finishAllActivity();
        } catch (Exception e) {
            LogUtil.e(TAG, "直接干死");
            System.exit(0);
        }
    }


    private class SwitchBackgroundCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
//            if(activity instanceof ActivityDetail) {
//                if(store.size() >= MAX_ACTIVITY_DETAIL_NUM){
//                    store.peek().finish(); //移除栈底的详情页并finish,保证商品详情页个数最大为10
//                }
            store.add(activity);
//            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (count == 0) { //后台切换到前台
                isShow = true;
                LogUtil.v(TAG, ">>>>>>>>>>>>>>>>>>>App切到前台");
            }
            count++;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            count--;
            if (count == 0) { //前台切换到后台
                isShow = false;
                LogUtil.v(TAG, ">>>>>>>>>>>>>>>>>>>App切到后台");
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            store.remove(activity);
        }
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public static Activity getCurActivity() {
        return store.lastElement();
    }


}
