package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.model.Base_Model;
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
 * TODO: Vuid 回调
 */

public class CallBack_Vuid_Servlet extends AsyncTask<CallBack_Vuid_Servlet.TencentVip, Integer, Base_Model> {
    private static final String TAG = "CallBack_Vuid_Servlet";

    @Override
    protected Base_Model doInBackground(TencentVip... vips) {
        TencentVip vip = vips[0];
        Map<String, String> map = new HashMap<>();
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
        map.put("vuid", vip.getVuid());
        map.put("eventId", vip.getEventId());
        map.put("code", vip.getCode());
        map.put("msg", vip.getMsg());
        map.put("guid", vip.getGuid());
        map.put("vuSession", vip.getVuSession());
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost("tencent/vuid/vuidAcctController/onEvent.do", map);

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

    public static class TencentVip {
        private String vuid;
        private String eventId;
        private String code;
        private String msg;
        private String guid;
        private String vuSession;

        public String getVuid() {
            return vuid;
        }

        public void setVuid(String vuid) {
            this.vuid = vuid;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getVuSession() {
            return vuSession;
        }

        public void setVuSession(String vuSession) {
            this.vuSession = vuSession;
        }
    }
}
