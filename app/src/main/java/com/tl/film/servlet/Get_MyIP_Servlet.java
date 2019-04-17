package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.model.Save_Key;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

/**
 * @author jiangyao
 * date: 2018/11/9
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 获取我的IP
 */
public class Get_MyIP_Servlet extends AsyncTask<String, Integer, Get_MyIP_Servlet.MyIp_Entity> {
    private static final String TAG = "Get_MyIP_Servlet";

    @Override
    protected MyIp_Entity doInBackground(String... strings) {
        String res = HttpUtil.request(HttpUtil.GET, "https://api.myip.com", null);

        MyIp_Entity entity = null;
        if (!TextUtils.isEmpty(res)) {
            try {
                entity = new Gson().fromJson(res, MyIp_Entity.class);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(MyIp_Entity entity) {
        super.onPostExecute(entity);
        if (entity != null) {
            LogUtil.e(TAG, entity.getIp());
            SaveUtils.setString(Save_Key.S_IP, entity.getIp());
        }
    }

    class MyIp_Entity {

        /**
         * ip : 49.77.233.183
         * country : China
         * cc : CN
         */

        private String ip;
        private String country;
        private String cc;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCc() {
            return cc;
        }

        public void setCc(String cc) {
            this.cc = cc;
        }
    }
}
