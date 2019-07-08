package com.tl.film.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tl.film.BuildConfig;
import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.adapter.RecyclerCoverFlow_Adapter;
import com.tl.film.dialog.Loading;
import com.tl.film.dialog.NetDialog;
import com.tl.film.model.DefTheme_Model;
import com.tl.film.model.EventBus_Model;
import com.tl.film.model.FirstFilms_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Update_Model;
import com.tl.film.servlet.DefTheme_Servlet;
import com.tl.film.servlet.DownUtil;
import com.tl.film.servlet.FirstFilms_Servlet;
import com.tl.film.servlet.Get_Info_Servlet;
import com.tl.film.servlet.Update_Servlet;
import com.tl.film.utils.ExampleUtil;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.Open_Ktcp_Utils;
import com.tl.film.utils.SaveUtils;
import com.tl.film.utils.Tools;
import com.tl.film.view.CarouselLayoutManager;
import com.tl.film.view.CarouselZoomPostLayoutListener;
import com.tl.film.view.CenterScrollListener;
import com.tl.film.view.TvRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * @author jiangyao
 * date: 2019/3/22
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 主页面
 */
public class Home_Activity extends AppCompatActivity implements RecyclerCoverFlow_Adapter.ItemClick, View.OnClickListener {
    private static final String TAG = "Home_Activity";

    TvRecyclerView recyclerView;
    RecyclerCoverFlow_Adapter adapter;
    ImageView bg, logo, qrcode;
    View quanwang, lunbo;

    private long[] mHits = new long[7]; //用于监听连续菜单按键

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Home_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MyAPP.AddActivity(this);

        //注册eventbus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        //初始化控件
        initview();

        //注册消息接受事件
        registerMessageReceiver();  // used for receive msg

        init();
    }

    public void init() {
        if (!Tools.isNetworkConnected()) {
            NetDialog.showW(this);
        } else {
            Loading.show(this, "努力加载中请稍后...");
        }
        new Get_Info_Servlet().execute(SaveUtils.getString(Save_Key.S_Tlid_Model));
        //列表
        new FirstFilms_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //主题
        new DefTheme_Servlet().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //检查更新
        new Update_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void initview() {

        quanwang = findViewById(R.id.home_quanwan);
        lunbo = findViewById(R.id.home_lunbo);

        quanwang.setOnClickListener(this);
        lunbo.setOnClickListener(this);

        bg = findViewById(R.id.bg);
        logo = findViewById(R.id.logo);
        qrcode = findViewById(R.id.qrcode);
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(EventBus_Model model) {
        Object data = model.getData();
        try {
            switch (model.getCommand_1()) {
                case EventBus_Model.CMD_NET_CONNECT:
                    init();
                    break;
                case EventBus_Model.CMD_FILL_DATA_THEME:
                    if (data != null) {
                        doFillTheme((DefTheme_Model.DataBean) data);
                    }
                    break;
                case EventBus_Model.CMD_FILL_DATA_FILM:
                    if (data != null) {
                        doFillFirstFilm((ArrayList<FirstFilms_Model.DataBean>) data);
                    }
                    break;
                case EventBus_Model.CMD_UPGRADE:
                    if (data != null) {
                        doUpgrade((Update_Model.DataBean) data);
                    }
                    break;
            }
        } catch (Exception ex) {
            LogUtil.e(TAG, "EventBus 报错：" + ex.getMessage());
        }
    }


    /**
     * 填充主题数据
     *
     * @param theme
     */
    private void doFillTheme(DefTheme_Model.DataBean theme) {
        if (theme != null) {
            //背景图
            if (!TextUtils.isEmpty(theme.getBgUrl())) {
                Picasso.with(this).load(theme.getBgUrl()).error(R.mipmap.bg).placeholder(R.mipmap.bg).into(bg);
            }
            //logo
            if (!TextUtils.isEmpty(theme.getLogo())) {
                Picasso.with(this).load(theme.getLogo()).into(logo);
            }
            //二维码
            if (!TextUtils.isEmpty(theme.getCpScan())) {
                Picasso.with(this).load(theme.getCpScan()).into(qrcode);
            }
        }
        return;
    }

    /**
     * 填充首发影院视频数据
     *
     * @param filmList
     */
    private void doFillFirstFilm(ArrayList<FirstFilms_Model.DataBean> filmList) {
        if (filmList != null && !filmList.isEmpty()) {
            SaveUtils.setString(Save_Key.S_FirstFilms_Model, new Gson().toJson(filmList));

            CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
            layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
            adapter = new RecyclerCoverFlow_Adapter(this, this);
            adapter.setDataBeans(filmList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.addOnScrollListener(new CenterScrollListener());
            layoutManager.setItemPrefetchEnabled(true);
        }
        return;
    }

    /**
     * 版本升级
     *
     * @param newVersioin
     */
    public void doUpgrade(Update_Model.DataBean newVersioin) {
        if (newVersioin != null) {
            if (newVersioin.getBuild() > BuildConfig.VERSION_CODE) {
                String downloadUrl = newVersioin.getDownloadUrl();
                if (downloadUrl != null && downloadUrl.toLowerCase().contains(".apk")) {
                    Loading.show(this, "更新中");
                    new DownUtil().downLoad(downloadUrl, "Film_" + newVersioin.getBuild() + ".apk", true);
                }
            }
        }
        return;
    }


    @Override
    public void clickItem(FirstFilms_Model.DataBean bean) {
        //判断网络
        if (!Tools.isNetworkConnected()) {
            NetDialog.showW(this);
            return;
        }

        if (Tools.install(this)) {
            Moive_Activity.start(this, bean);
        }
    }

    @Override
    public void focusableItem(int position) {
        recyclerView.smoothScrollToPosition(position);
    }


    /**
     * 启动安装
     *
     * @param path
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Install(String path) {
        Loading.dismiss();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");//设置intent的data和Type属性，uri是你安装文件的路径，这里时打开安装程序。
        startActivity(intent);
    }


    /**
     * 监听遥控按键
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //判断网络
        if (!Tools.isNetworkConnected()) {
            NetDialog.showW(this);
        }

        //连续按菜单键跳转到设备详情页面
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);// 数组向左移位操作
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 5000)) {
                mHits = new long[7];        //重置数组
                TerminalDetail_Activity.start(this);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_quanwan:
                if (Tools.install(this)) {
                    Buy_Vip_Activity.start(this);
                }
                break;
            case R.id.home_lunbo:
                if (Tools.install(this)) {
                    Open_Ktcp_Utils.openWithHomePageUri(this, "tenvideo2://?action=29&round_play_id=0");
                }
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
                    LogUtil.e(TAG, showMsg.toString());
                }
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }
    }


    @Override
    public void onBackPressed() {
        LogUtil.e(TAG, "返回");
        super.onBackPressed();
    }


}
