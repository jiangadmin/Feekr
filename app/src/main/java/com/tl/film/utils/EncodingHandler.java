package com.tl.film.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * @author Ryan Tang
 */
public final class EncodingHandler {
    private static final String TAG = "EncodingHandler";
    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xffffffff;
    private static final int STYLE_COLOR = 0xffe73737;

    public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        //设置编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置容错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        //设置边距
        hints.put(EncodeHintType.MARGIN, 0);

        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight,hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
//                    if (widthAndHeight == 800) {
//                        pixels[y * width + x] = WHITE;
//                    }else {
                    pixels[y * width + x] = BLACK;
//                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    public static Bitmap Create2DCode(String str) throws WriterException {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败       
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 400, 400);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了       
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api       
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
