package com.tl.film.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tl.film.MyAPP;

/**
 * @author jiangyao
 * date: 2019/3/22
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO:
 */
public class Base_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyAPP.activity = this;
    }

    @Override
    protected void onResume() {
        MyAPP.activity = this;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        MyAPP.activity = null;
        super.onDestroy();
    }
}
