package com.tl.film.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.BuildConfig;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Update_Model;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangadmin
 * date: 2017/6/19.
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 检查更新
 */

public class Update_Servlet extends AsyncTask<String, Integer, Update_Model> {
    private static final String TAG = "Update_Servlet";
    Activity activity;

    public Update_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Update_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
        map.put("build", String.valueOf(BuildConfig.VERSION_CODE));
        map.put("ver", BuildConfig.VERSION_NAME);
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost("fapp/upgradeController/upgrade.do", map);

        LogUtil.e(TAG, res);
        Update_Model entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Update_Model();
            entity.setCode(-1);
        } else {
            try {
                entity = new Gson().fromJson(res, Update_Model.class);
            } catch (Exception e) {
                e.printStackTrace();
                entity = new Update_Model();
                entity.setCode(-2);
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Update_Model model) {
        super.onPostExecute(model);
        EventBus.getDefault().post(model);

    }
}
