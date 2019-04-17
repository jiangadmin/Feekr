package com.tl.film.servlet;

import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.tl.film.MyAPP;
import com.tl.film.model.Const;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author jiangyao
 * date: 2019/4/17
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO:  绑定
 */
public class Bind_Servlet extends AsyncTask<String, Integer, String> {
    private static final String TAG = "Bind_Servlet";

    @Override
    protected String doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("mde", Build.MODEL);
        map.put("ip", MyAPP.getIp());
        map.put("mac", strings[0]);
        map.put("mainBoard", Build.BOARD);
        map.put("ver", "1.0.0");
        map.put("build", "1");
        map = HttpParamUtils.getRequestParams(map);

        FormBody.Builder builder = new FormBody.Builder();

        //使用迭代器，获取key;
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = map.get(key);
            builder.add(key, value);
        }

        String url = Const.URL + "fapp/terminalController/bind.do";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, e.getMessage());
                Tlid_Model model = new Tlid_Model();
                model.setCode(-1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.e(TAG, response.body().string());
                Tlid_Model model;
                try {
                    model = new Gson().fromJson(response.body().string(), Tlid_Model.class);
                    Const.URL = model.getData().getTlid();
                    SaveUtils.setString(Save_Key.S_TLID, Const.TLID);

                } catch (Exception e) {
                    model = new Tlid_Model();
                    model.setCode(-2);
                }
            }
        });

        return null;
    }


}
