package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.activity.Home_Activity;
import com.tl.film.dialog.Loading;
import com.tl.film.model.EventBus_Model;
import com.tl.film.model.FirstFilms_Model;
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
 * TODO: 首发影院
 */
public class FirstFilms_Servlet extends AsyncTask<String, Integer, FirstFilms_Model> {
    private static final String TAG = "FirstFilms_Servlet";

    Home_Activity activity;

    public FirstFilms_Servlet(Home_Activity activity) {
        this.activity = activity;
    }

    @Override
    protected FirstFilms_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("channelCode", "100101");
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));

        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost("cms/channelsController/findFirstFilms.do", map);
        LogUtil.e(TAG, res);

        FirstFilms_Model model;
        if (TextUtils.isEmpty(res)) {
            model = new FirstFilms_Model();
            model.setCode(-1);
        } else {
            try {
                model = new Gson().fromJson(res, FirstFilms_Model.class);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
                model = new FirstFilms_Model();
                model.setCode(-2);
            }
        }
//
        return model;
    }

    @Override
    protected void onPostExecute(FirstFilms_Model model) {
        super.onPostExecute(model);
        Loading.dismiss();
        if(model!=null && model.getCode() == 1000){
            EventBus_Model eb = new EventBus_Model();
            eb.setCommand_1(EventBus_Model.CMD_FILL_DATA_FILM);
            eb.setData(model.getData());
            EventBus.getDefault().post(eb);
        }
        return;
    }
}
