package com.tl.film;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.TvTicketTool.TvTicketTool;
import com.ktcp.video.ktsdk.TvTencentSdk;
import com.ktcp.video.thirdagent.JsonUtils;
import com.ktcp.video.thirdagent.KtcpContants;
import com.ktcp.video.thirdagent.KtcpPaySDKCallback;
import com.ktcp.video.thirdagent.KtcpPaySdkProxy;
import com.tl.film.model.Const;
import com.tl.film.model.Save_Key;
import com.tl.film.servlet.Get_MyIP_Servlet;
import com.tl.film.servlet.Get_Vuid_Servlet;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.HashMap;

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
    public static Activity activity;

    private static Context context;

    public static final boolean LogShow = true;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

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

    private int status = -1;//接口状态码
    private String msg;//接口提示信息
    private String guid = "";

    /**
     * @param channel 三方厂商对应的渠道号
     * @param extra   包含guid,QUA，TVPlatform等字段的json字符串
     */
    @Override
    public void doLogin(String channel, String extra) {
        LogUtil.e(TAG, "获取vuid开始" + extra);
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

        if (Const.ktcp_open_type == 1 &&
                (Const.ktcp_vuid != null && Const.ktcp_vuid != "") &&
                (Const.ktcp_vtoken != null && Const.ktcp_vtoken != "") &&
                (Const.ktcp_accessToken != null && Const.ktcp_accessToken != "")) {

            Const.ktcp_open_type = 0;       //标示位复位
            // FIXME:  获取帐号   需要腾讯处理的错误码和提示请沟通好通知处理
            // status 成功返回 0 失败返回对应错误码 厂商业务错误 fixme  腾旅 902xxx 例如902001登录失败
            // msg  错误提示
            // data json数据
            final HashMap<String, Object> loginData = new HashMap<>();
            loginData.put("loginType", "vu");//登录类型 vu ,qq,wx,ph
            loginData.put("vuid", Const.ktcp_vuid);
            loginData.put("vtoken", Const.ktcp_vtoken);
            loginData.put("accessToken", Const.ktcp_accessToken);

            //大票换小票接口
            TvTicketTool.getVirtualTVSKey(this, false, Long.parseLong(Const.ktcp_vuid), Const.ktcp_vtoken, Const.ktcp_accessToken, new TvTencentSdk.OnTVSKeyListener() {
                @Override
                public void OnTVSKeySuccess(String vusession, int expiredTime) {
                    LogUtil.e(TAG, "vusession=" + vusession + ",expiredTime=" + expiredTime);
                    status = 0;
                    msg = "login success";
                    loginData.put("vusession", vusession);
                    //通过onLoginResponse 将数据回传给腾讯
                    KtcpPaySdkProxy.getInstance().onLoginResponse(status, msg, JsonUtils.addJsonValue(loginData));
                }

                @Override
                public void OnTVSKeyFaile(int failedCode, String failedMsg) {
                    LogUtil.e(TAG, "failedCode=" + failedCode + ",msg=" + failedMsg);
                    status = failedCode;
                    msg = failedMsg;
                    KtcpPaySdkProxy.getInstance().onLoginResponse(status, msg, JsonUtils.addJsonValue(loginData));
                }
            });
        } else {

            //調用TencentVuidLoginEventServlet，在TencentVuidLoginEventServlet 獲取到vuid后直接調用TvTicketTool.getVirtualTVSKey
            new Get_Vuid_Servlet().execute(guid);
        }
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
                //示例  {{"extra":"{\"isVip\":false,\"vuid\":278113277,\"msg\":\"login success\",\"code\":0,\"vuSession\":\"97027a6822cce5220250ef76cd58\"}","eventId":2,"type":3}
                break;
            case 3://退出登录回调
                break;
            case 4://APP退出
                break;
            default:
                break;
        }

        try {
            JSONObject extraObj = JsonUtils.getJsonObj(params);
            int code = extraObj.optInt("code");
            String message = extraObj.optString("msg");

//            VIPCallBack_Servlet.TencentVip vip = new VIPCallBack_Servlet.TencentVip();
//            vip.setCode(String.valueOf(code));
//            vip.setMsg(message);
//            vip.setEventId(String.valueOf(eventId));  // 2 账户登录回调 3 退出登录  4 APP退出
//            new VIPCallBack_Servlet().execute(vip);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }

    }
}
