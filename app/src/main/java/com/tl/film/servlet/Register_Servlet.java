package com.tl.film.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tl.film.activity.Register_Activity;
import com.tl.film.dialog.Loading;
import com.tl.film.model.Base_Model;
import com.tl.film.model.Const;
import com.tl.film.model.EventBus_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.utils.HttpParamUtils;
import com.tl.film.utils.HttpUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class Register_Servlet extends AsyncTask<String, Integer, Tlid_Model> {
    private static final String TAG = "Register_Servlet";

    @Override
    protected Tlid_Model doInBackground(String... strings) {
        Map<String, String> map = new HashMap<>();
        map.put("tlid", SaveUtils.getString(Save_Key.S_TLID));
        map.put("merchantCode", strings[0]);
        map = HttpParamUtils.getRequestParams(map);

        String res = HttpUtil.doPost("fapp/terminalController/bindMerchant.do", map);
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
        Loading.dismiss();
        EventBus_Model eb = null;
        switch (model.getCode()) {
            case 1000:
                Tlid_Model localModel = new Gson().fromJson(SaveUtils.getString(Save_Key.S_Tlid_Model), Tlid_Model.class);
                if(localModel != null && model.getData() != null){
                    localModel.getData().setMerchantCode(model.getData().getMerchantCode());
                    localModel.getData().setMerchantId(model.getData().getMerchantId());
                    localModel.getData().setMerchantName(model.getData().getMerchantName());
                    SaveUtils.setString(Save_Key.S_Tlid_Model, new Gson().toJson(localModel));
                }

                //绑定成功跳转到首页
                eb = new EventBus_Model();
                eb.setCommand_1(EventBus_Model.CMD_ENTRY_HOME);
                EventBus.getDefault().post(eb);
                break;
            default:
                eb = new EventBus_Model();
                eb.setCommand_1(EventBus_Model.CMD_BIND_MERT_FAIL);
                eb.setData(model.getMessage());
                EventBus.getDefault().post(eb);
                break;
        }
    }
}
