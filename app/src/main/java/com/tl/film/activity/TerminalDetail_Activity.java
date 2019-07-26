package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitmapUtils;
import com.tl.film.BuildConfig;
import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.utils.SaveUtils;
import com.tl.film.utils.Tools;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangyao
 * date: 2019/4/21
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 终端设备详情
 */
public class TerminalDetail_Activity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TerminalDetail_Activity.class);
        context.startActivity(intent);
    }

    ImageView qr;

    TextView info_1, info_2, info_3, info_4, info_5, info_6, info_7, info_8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        MyAPP.AddActivity(this);
        qr = findViewById(R.id.qr);

        info_1 = findViewById(R.id.info_1);
        info_2 = findViewById(R.id.info_2);
        info_3 = findViewById(R.id.info_3);
        info_4 = findViewById(R.id.info_4);
        info_5 = findViewById(R.id.info_5);
        info_6 = findViewById(R.id.info_6);
        info_7 = findViewById(R.id.info_7);
        info_8 = findViewById(R.id.info_8);

        info_2.setText(String.format("版本:%s", BuildConfig.VERSION_NAME));
        info_3.setText(String.format("构建版本:%s", BuildConfig.VERSION_CODE));

        Map<String, Object> map = new HashMap<>();
        map.put("VERSION_NAME", BuildConfig.VERSION_NAME);
        map.put("VERSION_CODE", BuildConfig.VERSION_CODE);

        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_Tlid_Model))) {
            Tlid_Model model = new Gson().fromJson(SaveUtils.getString(Save_Key.S_Tlid_Model), Tlid_Model.class);

            if (!TextUtils.isEmpty(model.getData().getTlid())) {
                info_1.setText(String.format("TLID:%s", model.getData().getTlid()));
                map.put("TLID", model.getData().getTlid());
            }
            if (!TextUtils.isEmpty(model.getData().getMerchantName())) {
                info_4.setText(String.format("渠道商:%s", model.getData().getMerchantName()));
                map.put("MerchantName", model.getData().getMerchantName());
            }
            if (!TextUtils.isEmpty(model.getData().getMerchantCode())) {
                info_5.setText(String.format("渠道号:%s", model.getData().getMerchantCode()));
                map.put("MerchantCode", model.getData().getMerchantCode());
            }
        }

        try {
            qr.setImageBitmap(BitmapUtils.create2DCode(new Gson().toJson(map)));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        String macinfo = "";
        //有线网卡一
        if (Tools.getMac("eth0") != null) {
            macinfo += "eth0:" + Tools.getMac("eth0") + "\n";
        }
        //有线网卡二
        if (Tools.getMac("eth1") != null) {
            macinfo += "eth1:" + Tools.getMac("eth1") + "\n";
        }
        //无线网卡一
        if (Tools.getMac("wlan0") != null) {
            macinfo += "wlan0:" + Tools.getMac("wlan0") + "\n";
        }
        //无线网卡二
        if (Tools.getMac("wlan1") != null) {
            macinfo += "wlan1:" + Tools.getMac("wlan1") + "\n";
        }
        //虚拟网卡
        if (Tools.getMac("dummy0") != null) {
            macinfo += "dummy0:" + Tools.getMac("dummy0") + "\n";
        }
        //AndroidID
        if (Tools.AndroidID() != null) {
            macinfo += "AndroidID:" + Tools.AndroidID();
        }

        info_8.setText(macinfo);
    }

    @Override
    public void onBackPressed() {
        MyAPP.finishActivity();
    }
}
