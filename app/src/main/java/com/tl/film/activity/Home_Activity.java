package com.tl.film.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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
import com.tl.film.model.Const;
import com.tl.film.model.DefTheme_Model;
import com.tl.film.model.EventBus_Model;
import com.tl.film.model.FirstFilms_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.model.Tlid_Model;
import com.tl.film.model.Update_Model;
import com.tl.film.servlet.Bind_Servlet;
import com.tl.film.servlet.DefTheme_Servlet;
import com.tl.film.servlet.DownUtil;
import com.tl.film.servlet.FirstFilms_Servlet;
import com.tl.film.servlet.Update_Servlet;
import com.tl.film.utils.ExampleUtil;
import com.tl.film.utils.File_Utils;
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
import java.util.List;

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

    ImageView bg, logo, qrcode;

    View  quanwang, lunbo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        setContentView(R.layout.activity_home);

        initview();



        registerMessageReceiver();  // used for receive msg

        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.FirstFilms_Model))) {
            CallBack_FirstFilms(new Gson().fromJson(SaveUtils.getString(Save_Key.FirstFilms_Model), FirstFilms_Model.class));
        }

        new Timer(3000, 3000).start();

        if (TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_Tlid_Model))) {
            LogUtil.e(TAG, "绑定设备");
            new Bind_Servlet().execute();
        } else {
            EventBus_Model model = new EventBus_Model();
            model.setCommand_1("主页初始化");
            EventBus.getDefault().post(model);
        }
        install();

    }

    @Override
    protected void onResume() {
        super.onResume();

        MyAPP.activity = this;

        //判断网络
        if (!Tools.isNetworkConnected())
            NetDialog.showW();

    }

    @Override
    protected void onStop() {
        MyAPP.activity = this;
        super.onStop();
    }

    @Subscribe
    public void initevent(EventBus_Model model) {
        switch (model.getCommand_1()) {
            case "主页初始化":
                //列表
                new FirstFilms_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //主题
                new DefTheme_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //检查更新
                new Update_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case "渠道注册":
                if (MyAPP.register_activity == null)
                    Register_Activity.start(this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        MyAPP.activity = this;
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

        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_Tlid_Model))) {
            Tlid_Model model = new Gson().fromJson(SaveUtils.getString(Save_Key.S_Tlid_Model), Tlid_Model.class);

            if (TextUtils.isEmpty(model.getData().getMerchantCode())) {
                if (MyAPP.register_activity == null)
                    Register_Activity.start(this);
            }
        }
    }

    boolean showToast = true;
    long[] mHits = new long[7];

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            LogUtil.e(TAG, "菜单键");
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);// 数组向左移位操作
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 5000)) {
                if (MyAPP.register_activity == null)
                    Register_Activity.start(this);

            } else {
                showToast = true;
            }
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    /**
     * 首发影院返回
     *
     * @param model 数据
     */
    @Subscribe
    public void CallBack_FirstFilms(FirstFilms_Model model) {

        if (model.getCode() == 1000) {
            CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
            layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
            adapter = new RecyclerCoverFlow_Adapter(this, this);
            adapter.setDataBeans(model.getData());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.addOnScrollListener(new CenterScrollListener());

            layoutManager.setItemPrefetchEnabled(true);

            //触摸切换
            layoutManager.addOnItemSelectionListener(adapterPosition -> {

            });

            SaveUtils.setString(Save_Key.FirstFilms_Model, new Gson().toJson(model));
        }

    }

    /**
     * 主题返回
     *
     * @param model 数据
     */
    @Subscribe
    public void CallBack_Theme(DefTheme_Model model) {
        if (model.getCode() == 1000) {//背景
            if (!TextUtils.isEmpty(model.getData().getBgUrl())) {
                Picasso.with(this).load(model.getData().getBgUrl()).error(R.mipmap.bg).placeholder(R.mipmap.bg).into(bg);
            }
            //logo
            if (!TextUtils.isEmpty(model.getData().getLogo())) {
                Picasso.with(this).load(model.getData().getLogo()).into(logo);
            }
            //二维码
            if (!TextUtils.isEmpty(model.getData().getCpScan())) {
                Picasso.with(this).load(model.getData().getCpScan()).into(qrcode);
            }
        }

    }

    @Override
    public void clickItem(FirstFilms_Model.DataBean bean) {
        if (install()) {
            Moive_Activity.start(this, bean);
        }
    }

    @Override
    public void focusableItem(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    /**
     * 检查更新
     *
     * @param model 数据
     */
    @Subscribe
    public void onMessage(Update_Model model) {
        //                Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
        if (model.getCode() == 1000) {
            if (model.getData().getBuild() > BuildConfig.VERSION_CODE) {
                Loading.show(this, "更新中");
                new DownUtil().downLoad(model.getData().getDownloadUrl(), "Film_" + model.getData().getBuild() + ".apk", true);
            }
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_quanwan:
                QRCode_Activity.start(this);
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
                    LogUtil.e(TAG, showMsg.toString());
                }
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }
    }


    class Timer extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {

            findViewById(R.id.welcome).setVisibility(View.GONE);
        }
    }

    private boolean install() {
        //检测有没有云视听
        if (!isAvilible("com.ktcp.tvvideo")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("未检测到云视听应用");
            builder.setMessage("为了更好的观影体验，本应用需要安装 云视听 应用");
            builder.setNegativeButton("安装", (dialog, which) -> {

                File_Utils.openApk(File_Utils.copyAssetsFile(this, "tv_video_16188.apk", Const.FilePath), this);
                dialog.dismiss();
            });

            builder.setCancelable(false);
            builder.show();
            return false;
        } else {
            return true;
        }

    }

    /**
     * 判断应用存在性
     *
     * @param packageName 包名
     * @return 是否存在
     */
    private boolean isAvilible(String packageName) {
        //获取packagemanager
        final PackageManager packageManager = getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packinfo = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> pName = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packinfo != null) {
            for (int i = 0; i < packinfo.size(); i++) {
                String pn = packinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        //判断pName中是否有目标程序的包名，有true，没有false
        return pName.contains(packageName);
    }
}
