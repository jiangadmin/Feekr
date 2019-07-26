package com.tl.film.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.tl.film.MyAPP;
import com.tl.film.model.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("DefaultLocale")
public final class Tools {
    private static String TAG = "Tools";

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private Tools() throws InstantiationException {
        throw new InstantiationException("This class is not created for instantiaation");
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * @param origin
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String md5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    /**
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,1,3,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "-telnum-");
        return m.matches();
    }

    /**
     * @param expression
     * @param text
     * @return
     */
    private static boolean matchingText(String expression, String text) {
        Pattern p = Pattern.compile(expression);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    /**
     * @param zipcode
     * @return
     */
    public static boolean isZipcode(String zipcode) {
        Pattern p = Pattern.compile("[0-9]\\d{5}");
        Matcher m = p.matcher(zipcode);
        System.out.println(m.matches() + "-zipcode-");
        return m.matches();
    }

    /**
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        Pattern p = Pattern
                .compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = p.matcher(email);
        System.out.println(m.matches() + "-email-");
        return m.matches();
    }

    /**
     * @param telfix
     * @return
     */
    public static boolean isTelfix(String telfix) {
        Pattern p = Pattern.compile("d{3}-d{8}|d{4}-d{7}");
        Matcher m = p.matcher(telfix);
        System.out.println(m.matches() + "-telfix-");
        return m.matches();
    }

    /**
     * @param name
     * @return
     */
    public static boolean isCorrectUserName(String name) {
        Pattern p = Pattern.compile("([A-Za-z0-9]){2,10}");
        Matcher m = p.matcher(name);
        System.out.println(m.matches() + "-name-");
        return m.matches();
    }

    /**
     * @param pwd
     * @return
     */
    public static boolean isCorrectUserPwd(String pwd) {
        Pattern p = Pattern.compile("\\w{6,18}");
        Matcher m = p.matcher(pwd);
        System.out.println(m.matches() + "-pwd-");
        return m.matches();
    }

    /**
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param endTime
     * @param countDown
     * @return 剩余时间
     */
    public static String calculationRemainTime(String endTime, long countDown) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date now = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
            Date endData = df.parse(endTime);
            long l = endData.getTime() - countDown - now.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = l / (60 * 60 * 1000) - day * 24;
            long min = (l / (60 * 1000)) - day * 24 * 60 - hour * 60;
            long s = l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
            return "ʣ��" + day + "��" + hour + "Сʱ" + min + "��" + s + "��";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void showLongToast(Context act, String pMsg) {
        Toast toast = Toast.makeText(act, pMsg, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showShortToast(Context act, String pMsg) {
        Toast toast = Toast.makeText(act, pMsg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getImeiCode(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * @param listView
     * @author sunglasses
     * @category 计算listview高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static PackageInfo getAPKVersionInfo(Context context,
                                                String packageName) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }


    /**
     * 得到sd卡剩余大小
     *
     * @return
     */
    public static long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks / 1024;
    }

    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public static List<ResolveInfo> findActivitiesForPackage(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(packageName);
        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        return apps != null ? apps : new ArrayList<ResolveInfo>();
    }

    static public boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    /**
     * @param dp 要转换的 dp
     * @return 转出 px
     */
    public static int dp2px(Context context, float dp) {
        // 拿到屏幕密度
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);// 四舍五入
        return px;
    }


    /**
     * 版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            getLocationInfo(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            getLocationInfo(null);
        }

        @Override
        public void onProviderEnabled(String provider) {
            getLocationInfo(null);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void getLocationInfo(Location location) {
        String latLongInfo;
        if (location != null) {
            double latitude = location.getLatitude();//维度
            double longitude = location.getLongitude();//经度
            //
            LogUtil.e(TAG, "getProvider:" + location.getProvider());
        } else {
            latLongInfo = "No location found";
        }
    }


    /**
     * 判断是否有网络连接
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        if (MyAPP.getContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) MyAPP.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断是否是WIFI连接
     *
     * @return
     */
    public static boolean isWifiConnected() {
        if (MyAPP.getContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) MyAPP.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.isAvailable())
                if (mWiFiNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    return mWiFiNetworkInfo.isAvailable();
            return false;
        }
        return false;
    }

    /**
     * 判断是否是有线连接
     *
     * @return
     */
    public static boolean isLineConnected() {
        if (MyAPP.getContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) MyAPP.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mLineNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mLineNetworkInfo != null && mLineNetworkInfo.isAvailable())
                if (mLineNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET)
                    return mLineNetworkInfo.isAvailable();
            return false;
        }
        return false;
    }

    /**
     * 获得休眠时间 毫秒
     */
    public static int getScreenOffTime() {
        int screenOffTime = 0;
        try {
            screenOffTime = Settings.System.getInt(MyAPP.getContext().getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception localException) {
        }
        return screenOffTime;
    }

    /**
     * 设置休眠时间 毫秒
     */
    public static void setScreenOffTime(int paramInt) {
        try {
            Settings.System.putInt(MyAPP.getContext().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
                    paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 保留文件名及后缀
     */
    public static String getFileNameWithSuffix(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }


    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String NowTime() {
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 根据包名 启动应用
     *
     * @param context
     * @param packageName
     */
    public static void StartApp(Activity context, String packageName) {
        context.startActivity(new Intent(context.getPackageManager().getLaunchIntentForPackage(packageName)));
    }

    /**
     * 根据包名关闭APP
     *
     * @param context
     * @param packageName
     */
    public static void getRunningServiceInfo(Context context, String packageName) {
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppServicees()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningServiceInfo> runServiceList = mActivityManager
                .getRunningServices(50);
        System.out.println(runServiceList.size());
        // ServiceInfo Model类 用来保存所有进程信息
        for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
            ComponentName serviceCMP = runServiceInfo.service;
            String serviceName = serviceCMP.getShortClassName(); // service 的类名
            String pkgName = serviceCMP.getPackageName(); // 包名

            if (pkgName.equals(packageName)) {
                mActivityManager.killBackgroundProcesses(packageName);

            }

        }
    }

    /**
     * 判断 Root 权限
     *
     * @return
     */
    public static synchronized boolean isRoot() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: " + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据包名查找Pid
     *
     * @param context
     * @param packageName
     * @return
     */
    public static int PID(Context context, String packageName) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> mRunningProcess = mActivityManager.getRunningAppProcesses();

        int i = 1;

        for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess) {
            LogUtil.e(TAG, (i++) + "PID: " + amProcess.pid + "(processName=" + amProcess.processName + "UID=" + amProcess.uid + ")");
        }
        return 0;
    }


    /**
     * 我的唯一身份
     * 依次获取
     *
     * @return
     */
    public static String MyID() {
        //有线网卡一
        if (getMac("eth0") != null) {
            return getMac("eth0");
        }
        //有线网卡二
        if (getMac("eth1") != null) {
            return getMac("eth1");
        }
        //无线网卡一
        if (getMac("wlan0") != null) {
            return getMac("wlan0");
        }
        //无线网卡二
        if (getMac("wlan1") != null) {
            return getMac("wlan1");
        }
        //虚拟网卡
        if (getMac("dummy0") != null) {
            return getMac("dummy0");
        }
        //AndroidID
        if (AndroidID() != null) {
            return AndroidID();
        }
        return "NULLFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

    }

    public static String AndroidID() {
        String androidid;
        androidid = Settings.System.getString(MyAPP.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        androidid = androidid + "AndroidIDFFFFFFFFFFFFFFFFFFFFFFF";
        androidid = androidid.substring(0, 32);
        return androidid;
    }


    /**
     * 根据名称获取MAC地址【补位32】
     *
     * @param name
     * @return
     */
    public static String getMac(String name) {

        String macAddress;
        StringBuffer buf = new StringBuffer();
        NetworkInterface anInterface;
        try {
            anInterface = NetworkInterface.getByName(name);

            //都获取不到
            if (anInterface == null) {
                return null;
            }

            byte[] addr = anInterface.getHardwareAddress();

            if (addr != null) {
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                macAddress = buf.toString();
            } else {
                return null;
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }

        String mac = macAddress.replaceAll(":", "");

        mac = mac + name + "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

        mac = mac.substring(0, 32);

        return mac;
    }

    private static String loadFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    static AlertDialog.Builder builder;

    public static boolean install(Context context) {
        //检测有没有云视听
        if (!isAvilible(context, "com.ktcp.tvvideo")) {
            if (builder == null) {
                builder = new AlertDialog.Builder(context);
                builder.setTitle("未检测到云视听应用");
                builder.setMessage("为了更好的观影体验，本应用需要安装 云视听 应用");
                builder.setNegativeButton("安装", (dialog, which) -> {
                    dialog.dismiss();
                    builder = null;
                    Toast.makeText(context, "正在准备资源，请稍后...", Toast.LENGTH_SHORT).show();
                    File_Utils.openApk(File_Utils.copyAssetsFile(context, "tv_video_3.9.0.2054_android_16188.apk", Const.FilePath), context);
                });
                builder.setCancelable(false);
                builder.show();
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断应用存在性
     *
     * @param packageName 包名
     * @return 是否存在
     */
    private static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packinfo = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> pName = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packinfo != null) {
            for (int i = 0; i < packinfo.size(); i++) {
                String pn = packinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        //判断pName中是否有目标程序的包名，有true，没有false
        return pName.contains(packageName);
    }

}
