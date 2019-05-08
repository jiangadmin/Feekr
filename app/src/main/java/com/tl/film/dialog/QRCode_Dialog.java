package com.tl.film.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tl.film.MyAPP;
import com.tl.film.R;
import com.tl.film.model.EventBus_Model;
import com.tl.film.utils.ImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

public class QRCode_Dialog extends Dialog {

    public QRCode_Dialog(@NonNull Context context) {
        super(context);
    }


    ImageView qr;

    TextView message_0, message_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        setContentView(R.layout.dialog_qrcode);

        qr = findViewById(R.id.qr);
        message_0 = findViewById(R.id.message_0);
        message_1 = findViewById(R.id.message_1);
        qr.setImageBitmap(ImageUtils.getQRcode("二维码数据"));
    }

    @Subscribe
    public void onMessage(EventBus_Model model) {
        switch (model.getCommand_1()) {
            case "交易中":
                message_0.setText("正在交易中");
                break;
            case "交易成功":
                message_0.setText("交易已完成");
                break;
        }
    }

    @Override
    public void dismiss() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.dismiss();
    }


    private long lastClickTime = 0;

    public static final int MIN_CLICK_DELAY_TIME = 10 * 1000;

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    Toast.makeText(MyAPP.getContext(), "按键有效", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    Toast.makeText(MyAPP.getContext(), "稍安勿躁", Toast.LENGTH_SHORT).show();
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }
}