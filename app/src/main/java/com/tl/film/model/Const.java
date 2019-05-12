package com.tl.film.model;

import android.os.Environment;

/**
 * @author jiangyao
 * date: 2019/4/17
 * Email: www.fangmu@qq.com
 * Phone: 186 6120 1018
 * TODO: 常量
 */
public class Const {
    //    public static String URL = "http://118.31.103.224:8080/tlh_api/";
    public static String URL = "http://192.168.31.116:9092/tlh_api/";
    public static String TLID = null;
    public static String FilePath = Environment.getExternalStorageDirectory().getPath() + "/Film/Download/";


    public static String ktcp_vuid = "";//腾讯视频ID
    public static String ktcp_vtoken = "";//腾讯视频ID
    public static String ktcp_accessToken = "";//腾讯视频ID
}
