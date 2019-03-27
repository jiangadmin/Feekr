package com.tl.film.activity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }
}
