package com.wuchangi.searchforanswer.utils;

/**
 * Created by WuchangI on 2018/8/8.
 */

import android.graphics.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 对 Bitmap 进行处理的工具类
 */
public class BitmapUtils
{
    /**
     * bitmap旋转
     * @param bitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees)
    {
        if (degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try
            {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != b2)
                {
                    bitmap.recycle();  //Android开发网再次提示Bitmap操作完应该显示的释放
                    bitmap = b2;
                }
            }
            catch (OutOfMemoryError ex)
            {
                // Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
            }
        }
        return bitmap;
    }

    /**
     * 对Bitmap灰度化处理
     * @param bitmap
     * @return
     */
    public static Bitmap bitmapConvertGray(Bitmap bitmap)
    {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

        Paint paint = new Paint();
        paint.setColorFilter(filter);
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        return result;
    }


    /**
     * 对Bitmap二值化
     * @param bm
     * @return
     */
    public static Bitmap bitmapBinarization(Bitmap bm)
    {
        Bitmap bitmap = null;
        // 获取图片的宽和高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 创建二值化图像
        bitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        // 遍历原始图像像素,并进行二值化处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // 得到当前的像素值
                int pixel = bitmap.getPixel(i, j);
                // 得到Alpha通道的值
                int alpha = pixel & 0xFF000000;
                // 得到Red的值
                int red = (pixel & 0x00FF0000) >> 16;
                // 得到Green的值
                int green = (pixel & 0x0000FF00) >> 8;
                // 得到Blue的值
                int blue = pixel & 0x000000FF;
                // 通过加权平均算法,计算出最佳像素值
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                // 对图像设置黑白图
                if (gray <= 95) {
                    gray = 0;
                } else {
                    gray = 255;
                }
                // 得到新的像素值
                int newPiexl = alpha | (gray << 16) | (gray << 8) | gray;
                // 赋予新图像的像素
                bitmap.setPixel(i, j, newPiexl);
            }
        }
        return bitmap;

    }


    /**
     * 压缩bitmap
     * @param image
     * @return
     */
    public static Bitmap compressBitmap(Bitmap image)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100)
        { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 将bitmap转换成字节数组
     * @param bitmap
     * @return
     */
    public static byte[] convertToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        return bitmapByte;
    }

    /**
     * 将bitmap从字节数组中读出
     * @param byteArray
     * @return
     */
    public static Bitmap getBitmapFromByteArray(byte[] byteArray)
    {
        Bitmap bitmap=BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

}
