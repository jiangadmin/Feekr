package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tl.film.R;

public class Register_Activity extends Base_Activity {
    private static final String TAG = "Register_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,Register_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
