package com.tl.film.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class File_Utils {
    private static final String TAG = "File_Utils";

    /**
     * 复制文件到SD卡
     *
     * @param context
     * @param fileName 复制的文件名
     * @param path     保存的目录路径
     * @return
     */
    public static Uri copyAssetsFile(Context context, String fileName, String path) {
        try {
            InputStream mInputStream = context.getAssets().open(fileName);
            File file = new File(path);

            //判断文件夹是否被创建
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.mkdir();
            }
            File mFile = new File(path + File.separator + "tv_video_3.9.0.2054_android_16188.apk");
            if (!mFile.exists()) mFile.createNewFile();
            LogUtil.e(TAG, "开始拷贝");
            FileOutputStream mFileOutputStream = new FileOutputStream(mFile);
            byte[] mbyte = new byte[1024];
            int i = 0;
            while ((i = mInputStream.read(mbyte)) > 0) {
                mFileOutputStream.write(mbyte, 0, i);
            }
            mInputStream.close();
            mFileOutputStream.close();
            Uri uri = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                //包名.fileprovider
                    uri = FileProvider.getUriForFile(context, "com.tl.film.fileprovider", mFile);
                } else {
                    uri = Uri.fromFile(mFile);
                }
            } catch (ActivityNotFoundException anfe) {
                LogUtil.e(TAG, anfe.getMessage());
            }
            MediaScannerConnection.scanFile(context, new String[]{mFile.getAbsolutePath()}, null, null);
            LogUtil.e(TAG, "拷贝完毕：" + uri);
            return uri;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.getMessage());
            return null;
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage() );
            return null;
        }
    }


    /**
     * 安装apk
     * @param uri apk存放的路径
     * @param context
     */
    public static void openApk(Uri uri, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
