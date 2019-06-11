package com.tl.film.servlet;

import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.BuildConfig;
import com.tl.film.MyAPP;
import com.tl.film.model.Const;
import com.tl.film.model.EventBus_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangyao
 * date: 2019/4/17
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO:  绑定
 */
public class Bind_Servlet extends AsyncTask<String, Integer, Tlid_Model> {
    private static final String TAG = "Bind_Servlet";

    @Override
    protected Tlid_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("mde", Build.MODEL);
        map.put("ip", MyAPP.getIp());
        map.put("mac", MyAPP.getMacAddress());
        map.put("mainBoard", Build.BOARD);
        map.put("ver", BuildConfig.VERSION_NAME);
        map.put("build", String.valueOf(BuildConfig.VERSION_CODE));
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost("fapp/terminalController/bind.do", map);

        LogUtil.e(TAG, res);
        Tlid_Model model;
        if (TextUtils.isEmpty(res)) {
            model = new Tlid_Model();
            model.setCode(-1);
        } else {
            try {
                model = new Gson().fromJson(res, Tlid_Model.class);
            } catch (Exception e) {
                model = new Tlid_Model();
                model.setCode(-2);
            }
        }

        return model;
    }

    @Override
    protected void onPostExecute(Tlid_Model model) {
        super.onPostExecute(model);
        switch (model.getCode()) {
            case 1000:
                if (model.getData() != null && !TextUtils.isEmpty(model.getData().getTlid())) {
                    //本地存储tlid及相关信息
                    SaveUtils.setString(Save_Key.S_Tlid_Model, new Gson().toJson(model));
                    Const.TLID = model.getData().getTlid();
                    SaveUtils.setString(Save_Key.S_TLID, Const.TLID);

                    //判断是否绑定过商户未绑定则跳转绑定页面，如果已经绑定则直接跳转到首页
                    EventBus_Model model1 = new EventBus_Model();
                    if (TextUtils.isEmpty(model.getData().getMerchantCode())){
                        model1.setCommand_1(EventBus_Model.CMD_BIND_MERT);
                    }else {
                        model1.setCommand_1(EventBus_Model.CMD_ENTRY_HOME);
                    }
                    EventBus.getDefault().post(model1);

                }
                break;
            default:
                EventBus_Model model1 = new EventBus_Model();
                model1.setCommand_1(EventBus_Model.CMD_BIND_FAIL);
                EventBus.getDefault().post(model1);
                break;
        }

    }
}
