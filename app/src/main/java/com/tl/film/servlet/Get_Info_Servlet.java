package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.model.Info_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangyao
 * date: 2019/6/27
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 设备信息
 */

public class Get_Info_Servlet extends AsyncTask<String, Integer, Info_Model> {
    private static final String TAG = "Get_Info_Servlet";

    @Override
    protected Info_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("tlid", strings[0]);
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost("fapp/terminalController/findTerminal.do", map);
        LogUtil.e(TAG, res);

        Info_Model info_model;
        if (TextUtils.isEmpty(res)) {
            info_model = new Info_Model();
            info_model.setCode(-1);
        } else {
            try {
                info_model = new Gson().fromJson(res, Info_Model.class);
            } catch (Exception e) {
                info_model = new Info_Model();
                info_model.setCode(-2);
                LogUtil.e(TAG, e.getMessage());
            }
        }

        return info_model;
    }

    @Override
    protected void onPostExecute(Info_Model model) {
        super.onPostExecute(model);

        EventBus.getDefault().post(model);
    }
}
