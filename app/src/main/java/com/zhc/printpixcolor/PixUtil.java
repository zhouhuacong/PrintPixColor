package com.zhc.printpixcolor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

/**
 * 获取图片像素颜色工具类
 *
 * @author zhouhuacong
 */
public class PixUtil {

    /**
     * 通过资源文件获取图片像素颜色
     *
     * @param context 上下文
     * @param res     资源文件地址
     */
    public static void getPixColorByRes(Context context, int res) {
        Bitmap src = BitmapFactory.decodeResource(context.getResources(), res);
        int A, R, G, B;
        int pixelColor;
        int height = src.getHeight();
        int width = src.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelColor = src.getPixel(x, y);
                A = Color.alpha(pixelColor);
                R = Color.red(pixelColor);
                G = Color.green(pixelColor);
                B = Color.blue(pixelColor);

                Log.i("A:", A + "");
                Log.i("R:", R + "");
                Log.i("G:", G + "");
                Log.i("B:", B + "");
            }
        }
    }

    /**
     * 通过bitmap获取像素颜色
     *
     * @param src bitmap文件
     */
    public static void getPixColorByBitmap(Bitmap src) {
        if (src != null) {
            int A, R, G, B;
            int pixelColor;
            int height = src.getHeight();
            int width = src.getWidth();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelColor = src.getPixel(x, y);
                    A = Color.alpha(pixelColor);
                    R = Color.red(pixelColor);
                    G = Color.green(pixelColor);
                    B = Color.blue(pixelColor);

                    Log.i("A:", A + "");
                    Log.i("R:", R + "");
                    Log.i("G:", G + "");
                    Log.i("B:", B + "");
                }
            }
        }
    }

}
