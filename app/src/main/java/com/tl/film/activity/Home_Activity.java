package com.tl.film.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tl.film.R;
import com.tl.film.adapter.RecyclerCoverFlow_Adapter;
import com.tl.film.dialog.QRCode_Dialog;
import com.tl.film.model.DefTheme_Model;
import com.tl.film.model.FirstFilms_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.servlet.DefTheme_Servlet;
import com.tl.film.servlet.FirstFilms_Servlet;
import com.tl.film.utils.ExampleUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.Open_Ktcp_Utils;
import com.tl.film.utils.SaveUtils;
import com.tl.film.view.CarouselLayoutManager;
import com.tl.film.view.CarouselZoomPostLayoutListener;
import com.tl.film.view.CenterScrollListener;
import com.tl.film.view.TvRecyclerView;

/**
 * @author jiangyao
 * date: 2019/3/22
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 主页面
 */
public class Home_Activity extends Base_Activity implements RecyclerCoverFlow_Adapter.ItemClick, View.OnClickListener {
    private static final String TAG = "Home_Activity";

    TvRecyclerView recyclerView;
    RecyclerCoverFlow_Adapter adapter;

    ImageView bg;

    View shofa, quanwang, lunbo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initview();

        registerMessageReceiver();  // used for receive msg

        new FirstFilms_Servlet(this).execute();
        new DefTheme_Servlet(this).execute();
        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_TLID))) {
            LogUtil.e(TAG, "首发影院");

        }
    }


    private void initview() {

        shofa = findViewById(R.id.home_shoufa);
        quanwang = findViewById(R.id.home_quanwan);
        lunbo = findViewById(R.id.home_lunbo);

        shofa.setOnClickListener(this);
        quanwang.setOnClickListener(this);
        lunbo.setOnClickListener(this);

        bg = findViewById(R.id.bg);
        recyclerView = findViewById(R.id.recycler_view);

    }

    /**
     * 首发影院返回
     *
     * @param firstFilms_model
     */
    public void CallBack_FirstFilms(FirstFilms_Model firstFilms_model) {
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        adapter = new RecyclerCoverFlow_Adapter(this, this);
        adapter.setDataBeans(firstFilms_model.getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());

        layoutManager.setItemPrefetchEnabled(true);

        //触摸切换
        layoutManager.addOnItemSelectionListener(adapterPosition -> {

        });

    }

    /**
     * 主题返回
     *
     * @param model
     */
    public void CallBack_Theme(DefTheme_Model model) {
        Glide.with(this).load(model.getData().getBgUrl()).into(bg);
    }

    @Override
    public void clickItem(FirstFilms_Model.DataBean bean) {
        Moive_Activity.start(this, bean);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_shoufa:
                break;
            case R.id.home_quanwan:

                new QRCode_Dialog(this).show();
//                QRCode_Activity.start(this);
                break;
            case R.id.home_lunbo:
                Open_Ktcp_Utils.openWithHomePageUri(this, "tenvideo2://?action=29&round_play_id=0");
                break;
        }
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    LogUtil.e(TAG,showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }
}
