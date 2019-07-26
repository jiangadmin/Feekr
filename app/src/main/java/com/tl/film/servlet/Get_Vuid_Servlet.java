package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.TvTicketTool.TvTicketTool;
import com.google.gson.Gson;
import com.ktcp.video.ktsdk.TvTencentSdk;
import com.ktcp.video.thirdagent.JsonUtils;
import com.ktcp.video.thirdagent.KtcpPaySdkProxy;
import com.tl.film.MyAPP;
import com.tl.film.model.Const;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Vuid_Model;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;
import com.tl.film.utils.Tools;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangyao
 * date: 2019-05-17
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 获取VUID
 */
public class Get_Vuid_Servlet extends AsyncTask<String, Integer, Vuid_Model> {
    private static final String TAG = "Get_Vuid_Servlet";

    @Override
    protected Vuid_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
        map.put("mac", MyAPP.getMacAddress());
        map.put("terminal", Tools.MyID());
        map.put("guid", strings[0]);
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost(Const.URL + "tencent/vuid/vuidAcctController/doLogin.do", map);

        LogUtil.e(TAG, res);

        Vuid_Model model;
        if (TextUtils.isEmpty(res)) {
            model = new Vuid_Model();
            model.setCode(-1);
        } else {
            try {
                model = new Gson().fromJson(res, Vuid_Model.class);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
                model = new Vuid_Model();
                model.setCode(-2);
            }
        }

        return model;

    }

    @Override
    protected void onPostExecute(Vuid_Model model) {
        super.onPostExecute(model);

        //如果正确返回则执行腾讯大票换小票接口
        if (model.getCode() == 1000 && MyAPP.getContext() != null) {

            //解析vuid数据
            HashMap<String, Object> loginData = new HashMap<>();
            loginData.put("loginType", "vu");//登录类型 vu ,qq,wx,ph
            loginData.put("vuid", model.getData().getVuid());
            loginData.put("vtoken", model.getData().getVtoken());
            loginData.put("accessToken", model.getData().getAccessToken());

            //大票换小票接口
            TvTicketTool.getVirtualTVSKey(MyAPP.getContext(), false, model.getData().getVuid(), model.getData().getVtoken(), model.getData().getAccessToken(), new TvTencentSdk.OnTVSKeyListener() {
                @Override
                public void OnTVSKeySuccess(String vusession, int expiredTime) {
                    LogUtil.e(TAG, "vusession=" + vusession + ",expiredTime=" + expiredTime);
                    loginData.put("vusession", vusession);
                    //通过onLoginResponse 将数据回传给腾讯
                    KtcpPaySdkProxy.getInstance().onLoginResponse(0, "login success", JsonUtils.addJsonValue(loginData));
                }

                @Override
                public void OnTVSKeyFaile(int failedCode, String failedMsg) {
                    LogUtil.e(TAG, "failedCode=" + failedCode + ",msg=" + failedMsg);
                    int status = failedCode;
                    String msg = failedMsg;
                    KtcpPaySdkProxy.getInstance().onLoginResponse(status, msg, JsonUtils.addJsonValue(loginData));
                }
            });
        }
    }
}
