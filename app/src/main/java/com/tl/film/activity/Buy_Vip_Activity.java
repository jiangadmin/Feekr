package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.common.BitmapUtils;
import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.dialog.NetDialog;
import com.tl.film.model.DefTheme_Model;
import com.tl.film.model.Perpay_Model;
import com.tl.film.model.Push_Model;
import com.tl.film.model.Save_Key;
import com.tl.film.servlet.DefTheme_Servlet;
import com.tl.film.servlet.Get_PerPay_Servlet;
import com.tl.film.utils.LogUtil;
import com.tl.film.utils.SaveUtils;
import com.tl.film.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLDecoder;
import java.util.Calendar;

public class Buy_Vip_Activity extends AppCompatActivity {
    private static final String TAG = "Buy_Vip_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Buy_Vip_Activity.class);
        context.startActivity(intent);
    }

    ImageView bg, qrcode;

    TextView view1, view2;
    private long[] mHits = new long[7]; //用于监听连续菜单按键

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyAPP.AddActivity(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_buy_vip);

        bg = findViewById(R.id.bg);
        qrcode = findViewById(R.id.qrcode);
        view1 = findViewById(R.id.text1);
        view2 = findViewById(R.id.text2);

        new Get_PerPay_Servlet().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_DefTheme_Model))) {
            try {

                DefTheme_Model model = new Gson().fromJson(SaveUtils.getString(Save_Key.S_DefTheme_Model), DefTheme_Model.class);
                if (model.getData().getChargeBg() != null) {
                    CallBack_Theme(model.getData());
                }
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }
        //请求主题接口
        new DefTheme_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        view1.setText(Html.fromHtml("扫码解锁<font color='#FF9800'>会员版极光TV</font>"));
        view2.setText(Html.fromHtml("按方向键<font color='#FF9800'>【下键】</font>刷新二维码"));

    }

    /**
     * 主题返回
     *
     * @param dataBean 数据
     */
    public void CallBack_Theme(DefTheme_Model.DataBean dataBean) {
        if (dataBean != null && dataBean.getChargeBg() != null) {
            Glide.with(this).load(dataBean.getChargeBg()).into(bg);
        } else {
            Glide.with(this).load(R.mipmap.bg).into(bg);
        }
    }

    @Override
    protected void onDestroy() {
        MyAPP.finishActivity();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(Push_Model model) {
        if (model.getEventId().equals("OPEN_TX_APP")) {
            if (Tools.install(this)) {
                startActivity(getPackageManager().getLaunchIntentForPackage("com.ktcp.tvvideo"));
                finish();
            }
        }
    }

    /**
     * 付费信息处理
     *
     * @param model 数据模型
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(Perpay_Model model) {
        switch (model.getCode()) {
            case 1000:
                try {
                    qrcode.setImageBitmap(BitmapUtils.create2DCode(URLDecoder.decode(model.getData(), "UTF-8")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 13703:
                if (Tools.install(this)) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.ktcp.tvvideo");
                    startActivity(intent);
                    finish();
                }
                break;

            default:
                Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private long lastClickTime = 0;

    public static final int MIN_CLICK_DELAY_TIME = 10 * 1000;

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

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_MENU:
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);// 数组向左移位操作
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 5000)) {
                    mHits = new long[7];        //重置数组
                    TerminalDetail_Activity.start(this);
                }
                return true;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (Tools.install(this)) {
                    startActivity(getPackageManager().getLaunchIntentForPackage("com.ktcp.tvvideo"));
                }
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    new Get_PerPay_Servlet().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    return false;
                } else {
                    Toast.makeText(MyAPP.getContext(), "稍安勿躁", Toast.LENGTH_SHORT).show();
                    return true;
                }
        }

        return super.dispatchKeyEvent(event);
    }

}
