package com.tl.film.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.activity.Home_Activity;
import com.tl.film.model.DefTheme_Model;
import com.tl.film.model.EventBus_Model;
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
 * date: 2019/4/17
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 默认主题
 */
public class DefTheme_Servlet extends AsyncTask<String, Integer, DefTheme_Model> {
    private static final String TAG = "DefTheme_Servlet";

    @Override
    protected DefTheme_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost("fapp/themeController/findDefTheme.do", map);

        LogUtil.e(TAG, res);

        DefTheme_Model model;
        if (TextUtils.isEmpty(res)) {
            model = new DefTheme_Model();
            model.setCode(-1);
        } else {
            try {
                model = new Gson().fromJson(res, DefTheme_Model.class);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
                model = new DefTheme_Model();
                model.setCode(-2);
            }
        }

        return model;
    }


    @Override
    protected void onPostExecute(DefTheme_Model model) {
        super.onPostExecute(model);
        if (model != null && model.getCode() == 1000) {
            SaveUtils.setString(Save_Key.S_DefTheme_Model, new Gson().toJson(model));

            EventBus_Model eb = new EventBus_Model();
            eb.setCommand_1(EventBus_Model.CMD_FILL_DATA_THEME);
            eb.setData(model.getData());
            EventBus.getDefault().post(eb);
        }
    }
}
