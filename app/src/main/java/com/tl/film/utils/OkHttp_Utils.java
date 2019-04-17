package com.tl.film.utils;

import com.tl.film.model.Const;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author jiangyao
 * date: 2019/4/17
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: Okhttp 请求
 */
public class OkHttp_Utils implements Callback {
    private static final String TAG = "OkHttp_Utils";

    static String res;

    public String post(String url, Map<String, String> map) {
        res = null;


        return res;

    }


    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
}
