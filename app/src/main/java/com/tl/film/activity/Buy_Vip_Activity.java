package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tl.film.R;
import com.tl.film.model.DefTheme_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.utils.SaveUtils;

public class Buy_Vip_Activity extends Base_Activity {
    private static final String TAG = "Buy_Vip_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Buy_Vip_Activity.class);
        context.startActivity(intent);
    }

    ImageView bg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_vip);

        bg = findViewById(R.id.bg);

        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_DefTheme_Model))) {
            DefTheme_Model model = new Gson().fromJson(SaveUtils.getString(Save_Key.S_DefTheme_Model), DefTheme_Model.class);
            if (!TextUtils.isEmpty(model.getData().getChargeBg())) {
                Glide.with(this).load(model.getData().getChargeBg()).into(bg);
            }
        }
    }
}
