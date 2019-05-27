package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.dialog.Loading;
import com.tl.film.model.Base_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class Register_Servlet extends AsyncTask<String, Integer, Base_Model> {
    private static final String TAG = "Register_Servlet";

    @Override
    protected Base_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
        map.put("merchantCode", strings[0]);
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost("fapp/terminalController/bindMerchant.do", map);
        LogUtil.e(TAG, res);

        Base_Model model;
        if (TextUtils.isEmpty(res)) {
            model = new Base_Model();
            model.setCode(-1);
        } else {
            try {
                model = new Gson().fromJson(res, Base_Model.class);
            } catch (Exception e) {
                model = new Base_Model();
                model.setCode(-2);
            }
        }
        return model;
    }

    @Override
    protected void onPostExecute(Base_Model model) {
        super.onPostExecute(model);
        Loading.dismiss();
        EventBus.getDefault().post(model);
    }
}
