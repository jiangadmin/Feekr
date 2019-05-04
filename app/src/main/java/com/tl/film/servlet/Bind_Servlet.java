package com.tl.film.servlet;

import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.MyAPP;
import com.tl.film.model.Const;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

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
        map.put("mac", strings[0]);
        map.put("mainBoard", Build.BOARD);
        map.put("ver", "1.0.0");
        map.put("build", "1");
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost(Const.URL + "fapp/terminalController/bind.do", map);

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
        switch (model.getCode()){
            case 1000:
                if (model.getData()!=null&&!TextUtils.isEmpty(model.getData().getTlid())) {
                    Const.TLID = model.getData().getTlid();
                    SaveUtils.setString(Save_Key.S_TLID, Const.TLID);
                }
                break;
        }

    }
}
