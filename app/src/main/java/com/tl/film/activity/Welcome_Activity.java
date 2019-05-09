package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;

import com.tl.film.R;

/**
 * @author jiangyao
 * date: 2019/3/27
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 欢迎页
 */
public class Welcome_Activity extends Base_Activity {
    private static final String TAG = "Welcome_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Welcome_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Timer(3000, 3000).start();

    }

    class Timer extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {

            finish();
        }
    }
}
