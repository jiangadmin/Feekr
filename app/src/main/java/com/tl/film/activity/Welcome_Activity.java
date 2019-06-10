package com.tl.film.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tl.film.BuildConfig;
import com.tl.film.R;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.servlet.Bind_Servlet;
import com.tl.film.utils.SaveUtils;

public class Welcome_Activity extends Base_Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ((TextView) findViewById(R.id.ver)).setText(BuildConfig.VERSION_NAME);

        //本地绑定数据
        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_Tlid_Model))) {
            //数据解析
            Tlid_Model model = new Gson().fromJson(SaveUtils.getString(Save_Key.S_Tlid_Model), Tlid_Model.class);
            //绑定状态判定
            if (!TextUtils.isEmpty(model.getData().getTlid()) && !TextUtils.isEmpty(model.getData().getMerchantCode())) {
                //判定有无存储空间
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    new Handler().postDelayed(() -> {
                        //启动主页
                        Home_Activity.main(this); }, 3000);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setTitle("当前应用不支持该设备");
                    builder.setMessage("【错误】 当前设备无外置储存，请确认外置存储的存在");
                    builder.setNegativeButton("朕知道了", (dialog, which) -> {
                        dialog.dismiss();
                        System.exit(0);
                    });
                    builder.show();
                }
            } else {
                //打开绑定视图
                Register_Activity.start(this);
            }
        } else {
            //获取网络数据
            new Bind_Servlet().execute();
        }
    }
}
