package com.tl.film.activity;

import android.app.Activity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author jiangyao
 * date: 2019/3/22
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO:
 */
public class Base_Activity extends Activity {
    public static List<String> logList = new CopyOnWriteArrayList<>();
}
