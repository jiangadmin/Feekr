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
    //        public static String URL = "http://tencent.qicp.vip/";
//    public static String URL = "http://tlh.free.idcfengye.com/";
    public static String URL = "http://api.tenglv.net/";
    public static String TLID = null;
    public static String FilePath = Environment.getExternalStorageDirectory().getPath() + "/Film/Download/";

    public static String ktcp_vuid = "";//腾讯视频ID
    public static String ktcp_vtoken = "";//腾讯视频ID
    public static String ktcp_accessToken = "";//腾讯视频ID
    public static int ktcp_open_type = 0;       //腾讯定制版打开方式：0=无效, 1=从栏目启动，2=直接唤起
    public static boolean IsGetVip = false;  //本次开机时候获取过Vip

}
