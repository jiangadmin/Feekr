package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tl.film.R;
import com.tl.film.model.FirstFilms_Model;
import com.tl.film.utils.Open_Ktcp_Utils;

/**
 * @author jiangyao
 * date: 2019/3/28
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 电影详细
 */
public class Moive_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Moive_Activity";

    private static FirstFilms_Model.DataBean bean;

    public static void start(Context context, FirstFilms_Model.DataBean bean) {
        Intent intent = new Intent();
        intent.setClass(context, Moive_Activity.class);
        Moive_Activity.bean = bean;
        context.startActivity(intent);
    }

    ImageView img;
    TextView name, time, type, spot, director, tostar;
    Button play;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moive);

        initview();

    }

    private void initview() {
        img = findViewById(R.id.moive_img);
        name = findViewById(R.id.moive_name);
        time = findViewById(R.id.moive_time);
        director = findViewById(R.id.moive_director);
        tostar = findViewById(R.id.moive_tostar);
        play = findViewById(R.id.moive_play);
        type = findViewById(R.id.moive_type);
        spot = findViewById(R.id.moive_spot);

        name.setText(bean.getTitle());
        Picasso.with(this).load(bean.getBgImage()).into(img);
        time.setText(String.format("%s（%s 分钟）", bean.getBrief(), bean.getDuratior()));
        director.setText(bean.getDirectors());
        type.setText(bean.getBrief());
        tostar.setText(bean.getActors());
        spot.setText(bean.getProfile());

        play.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moive_play:
                if (bean.getTxPayStatus() == 8) {
                    Open_Ktcp_Utils.openWithHomePageUri(this, bean.getTxJumpPath());
                } else {
                    QRCode_Activity.start(this);
                }

                break;
        }
    }
}
