package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.model.Base_Model;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.model.Const;
import com.tl.film.utils.LogUtil;

import java.util.Map;

/**
 * @author jiangyao
 * date: 2018/5/18
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 会员登录返回
 */
public class VIPCallBack_Servlet extends AsyncTask<Map<String, String>, Integer, Base_Model> {
    private static final String TAG = "VIPCallBack_Servlet";

    @Override
    protected Base_Model doInBackground(Map<String,String>... params) {

        Map<String, String> param = HttpParamUtils.getRequestParams(params[0]);
        String res = HttpUtil.doPost(Const.URL + "tencent/vuid/vuidAcctController/onEvent.do", param);

        Base_Model entity;
        if (TextUtils.isEmpty(res)) {
            entity = new Base_Model();
            entity.setCode(-1);
        } else {
            try {
                entity = new Gson().fromJson(res, Base_Model.class);
            } catch (Exception e) {
                entity = new Base_Model();
                entity.setCode(-2);
                LogUtil.e(TAG, e.getMessage());
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Base_Model entity) {
        super.onPostExecute(entity);
        if (entity.getCode() != 1000) {
            LogUtil.e(TAG,"腾讯回调事件失败!");
       }
    }
}
