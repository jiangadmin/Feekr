package com.tl.film.servlet;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.tl.film.model.Const;
import com.tl.film.model.DefTheme_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.utils.HttpParamUtils;
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
 * TODO: 默认主题
 */
public class DefTheme_Servlet extends AsyncTask<String,Integer,String> implements Callback {
    private static final String TAG = "DefTheme_Servlet";

    @Override
    protected String doInBackground(String... strings) {
        Map<String,String> map = new HashMap<>();
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
        map = HttpParamUtils.getRequestParams(map);

        FormBody.Builder builder = new FormBody.Builder();

        //使用迭代器，获取key;
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = map.get(key);
            builder.add(key, value);
        }

        String url = Const.URL + "fapp/themeController/findDefTheme.do";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(this);
        return null;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        DefTheme_Model model = new DefTheme_Model();
        model.setCode(-1);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        DefTheme_Model model;
        try {
            model = new Gson().fromJson(response.body().toString(), DefTheme_Model.class);

        } catch (Exception e) {
            model = new DefTheme_Model();
            model.setCode(-2);
        }

    }
}
