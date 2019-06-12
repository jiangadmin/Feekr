package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.model.Perpay_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class Get_PerPay_Servlet extends AsyncTask<String, Integer, Perpay_Model> {
    private static final String TAG = "Get_PerPay_Servlet";

    @Override
    protected Perpay_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
        if (strings.length > 1) {
            map.put("txCoverId", strings[0]);
            map.put("videosId", strings[1]);
        }
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost("tencent/vuid/vuidOrderController/getPrePayInfo.do", map);

        LogUtil.e(TAG, res);

        Perpay_Model model;

        if (TextUtils.isEmpty(res)) {
            model = new Perpay_Model();
            model.setCode(-1);
        } else {
            try {
                model = new Gson().fromJson(res, Perpay_Model.class);
            } catch (Exception e) {
                e.printStackTrace();
                model = new Perpay_Model();
                model.setCode(-2);
            }
        }
        return model;
    }

    @Override
    protected void onPostExecute(Perpay_Model perpay_model) {
        super.onPostExecute(perpay_model);
        EventBus.getDefault().post(perpay_model);
    }
}
