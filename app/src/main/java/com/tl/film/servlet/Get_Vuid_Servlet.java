package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.model.Base_Model;
import com.tl.film.model.Const;
import com.tl.film.model.DefTheme_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangyao
 * date: 2019-05-17
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 获取VUID
 */
public class Get_Vuid_Servlet extends AsyncTask<String,Integer, Base_Model> {
    private static final String TAG = "Get_Vuid_Servlet";

    @Override
    protected Base_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
        map.put("mac", SaveUtils.getString(Save_Key.S_TLID));
        map.put("guid", SaveUtils.getString(Save_Key.S_TLID));
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost(Const.URL + "fapp/themeController/findDefTheme.do", map);

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
}
