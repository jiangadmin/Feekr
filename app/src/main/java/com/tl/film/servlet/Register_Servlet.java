package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.activity.Register_Activity;
import com.tl.film.dialog.Loading;
import com.tl.film.model.Base_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class Register_Servlet extends AsyncTask<String, Integer, Tlid_Model> {
    private static final String TAG = "Register_Servlet";

    Register_Activity activity;

    public Register_Servlet(Register_Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Tlid_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
        map.put("merchantCode", strings[0]);
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost("fapp/terminalController/bindMerchant.do", map);
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
        Loading.dismiss();
        activity.onMessage(model);
    }
}
