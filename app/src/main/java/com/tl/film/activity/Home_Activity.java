package com.tl.film.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tl.film.R;
import com.tl.film.adapter.RecyclerCoverFlow_Adapter;
import com.tl.film.model.DefTheme_Model;
import com.tl.film.model.FirstFilms_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.servlet.DefTheme_Servlet;
import com.tl.film.servlet.FirstFilms_Servlet;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;
import com.tl.film.view.CarouselLayoutManager;
import com.tl.film.view.CarouselZoomPostLayoutListener;
import com.tl.film.view.CenterScrollListener;

/**
 * @author jiangyao
 * date: 2019/3/22
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO:
 */
public class Home_Activity extends Base_Activity {
    private static final String TAG = "Home_Activity";

    RecyclerView recyclerView;
    RecyclerCoverFlow_Adapter adapter;

    ImageView bg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initview();

        new FirstFilms_Servlet(this).execute();
        new DefTheme_Servlet(this).execute();
        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_TLID))) {
            LogUtil.e(TAG, "首发影院");

        }
    }

    private void initview() {
        bg = findViewById(R.id.bg);
        recyclerView = findViewById(R.id.recycler_view);

//        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
//        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
//        adapter = new RecyclerCoverFlow_Adapter(this);
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.addOnScrollListener(new CenterScrollListener());
    }

    public void CallBack_FirstFilms(FirstFilms_Model firstFilms_model) {
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        adapter = new RecyclerCoverFlow_Adapter(this);
        adapter.setDataBeans(firstFilms_model.getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());

    }

    public void CallBack_Theme(DefTheme_Model model) {
        Glide.with(this).load(model.getData().getBgUrl()).into(bg);
    }

}
