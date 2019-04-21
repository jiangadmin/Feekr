package com.tl.film.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.tl.film.R;
import com.tl.film.utils.ImageUtils;

/**
 * @author jiangyao
 * date: 2019/4/21
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 二维码显示页面
 */
public class QRCode_Activity extends Base_Activity {
    private static final String TAG = "QRCode_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, QRCode_Activity.class);
        context.startActivity(intent);
    }

    ImageView qr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        qr = findViewById(R.id.qr);
        qr.setImageBitmap(ImageUtils.getQRcode("二维码数据"));

    }
}
