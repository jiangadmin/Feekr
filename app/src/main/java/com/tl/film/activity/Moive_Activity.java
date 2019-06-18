package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.dialog.Loading;
import com.tl.film.dialog.QRCode_Dialog;
import com.tl.film.model.FirstFilms_Model;
import com.tl.film.model.Perpay_Model;
import com.tl.film.model.Push_Model;
import com.tl.film.servlet.Get_PerPay_Servlet;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.Open_Ktcp_Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author jiangyao
 * date: 2019/3/28
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 电影详细
 */
public class Moive_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Moive_Activity";

    public static FirstFilms_Model.DataBean film;


    public static void start(Context context, FirstFilms_Model.DataBean film) {
        Intent intent = new Intent();
        intent.setClass(context, Moive_Activity.class);
        Moive_Activity.film = film;
        context.startActivity(intent);
    }

    ImageView img;
    TextView name, time, type, profile, director, tostar, score_text;
    Button play;
    RatingBar score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moive);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        MyAPP.activity = this;
        initview();

    }

    private void initview() {
        img = findViewById(R.id.movie_img);
        score = findViewById(R.id.movie_score);
        score_text = findViewById(R.id.movie_score_text);
        name = findViewById(R.id.movie_name);
        time = findViewById(R.id.movie_time);
        director = findViewById(R.id.movie_director);
        tostar = findViewById(R.id.movie_actors);
        play = findViewById(R.id.movie_play);
        type = findViewById(R.id.movie_type);
        profile = findViewById(R.id.movie_profile);

        if (film != null) {
            name.setText(film.getTitle());
            if (film.getScore() <= 10) {
                score.setRating((float) (film.getScore() / 2.0));
            } else {
                score.setRating(8.8F);
            }
            score_text.setText(String.valueOf(film.getScore()));
            Picasso.with(this).load(film.getBgImage()).into(img);
            time.setText(String.format("（%s 分钟）", film.getDuratior()));
            director.setText(film.getDirectors());
            type.setText(film.getGenre());
            tostar.setText(film.getActors());
            profile.setText(film.getProfile());
        }

        play.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.movie_play) {
            //免费
            if (film.getTxPayStatus() == 8) {
                Open_Ktcp_Utils.openWithHomePageUri(this, film.getTxJumpPath());
            } else {
                Loading.show(this, "请稍后");
                new Get_PerPay_Servlet().execute(film.getTxCoverId(), String.valueOf(film.getId()));
            }
        }
    }

    /**
     * 付费信息处理
     *
     * @param model 数据模型
     */
    @Subscribe
    public void onMessage(Perpay_Model model) {
        Loading.dismiss();
        switch (model.getCode()) {
            case 1000:
                try {
                   new QRCode_Dialog(this, URLDecoder.decode(model.getData(), "utf-8")).show();
                } catch (UnsupportedEncodingException ex) {
                    LogUtil.e(TAG, ex.getMessage());
                }

                break;
            case 37003:
                Open_Ktcp_Utils.openWithHomePageUri(this, film.getTxJumpPath());
                break;

            default:
                Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Subscribe
    public void onMessage(Push_Model model) {
        if (model.getEventId().equals("OPEN_TX_FILM")) {
            Open_Ktcp_Utils.openWithHomePageUri(this, film.getTxJumpPath());
        }
    }
}
