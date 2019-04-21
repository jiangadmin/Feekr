package com.tl.film.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import java.util.List;

/**
 * @author jiangyao
 * date: 2019/4/20
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO:
 */
public class Open_Ktcp_Utils {

    private static void handleIntent(Context context, Intent intent) {
        // 隐式调用的方式startActivity
        intent.setAction("com.tencent.qqlivetv.open");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.ktcp.tvvideo");//设置视频包名，要先确认包名
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager
                .queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "未安装腾讯视频 ， 无法跳转", Toast.LENGTH_SHORT).show();
        }

    }

    public static void openWithHomePageUri(Context context, String uri) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(uri));
        handleIntent(context, intent);
    }
}
