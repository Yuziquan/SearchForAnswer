package com.wuchangi.searchforanswer.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

/**
 * Created by WuchangI on 2018/8/6.
 */

/**
 * 拍照预览工具类
 */
public class CameraPreviewUtils
{

    /**
     * 检测手机是否带有摄像头
     * @param context
     * @return
     */
    public static boolean checkCameraHardware(Context context)
    {
        if(context != null &&
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * 获取相机实例
     * @return
     */
    public static Camera getCameraInstance(Context context)
    {
        Camera camera = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();

        try
        {
            for (int i = 0; i < numberOfCameras; i++)
            {
                Camera.getCameraInfo(i, cameraInfo);

                //是后置摄像头
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                {
                    try
                    {
                        //开启后置摄像头
                        camera = Camera.open(i);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(context, "后置摄像头开启失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (camera == null)
            {
                camera = Camera.open(0);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, "后置摄像头开启失败！", Toast.LENGTH_SHORT).show();
        }

        return camera;
    }

    /**
     * 找到最合适的预览显示分辨率，最好是预览的宽高比例和surfaceview的宽高比例一致，防止预览图像拉伸变形
     * @param parameters
     * @return
     */
    public static Camera.Size findBestPreviewSize(Camera.Parameters parameters, Context context, Camera camera,
                                                  double previewWidth, double previewHeight)
    {
        //预览图的尺寸比例（实际尺寸）,其实是surfaceView的尺寸比例
        double previewRatio = 0.0;

        if(previewWidth != 0 && previewHeight != 0)
        {
            previewRatio = Math.min(previewWidth, previewHeight) / Math.max(previewWidth, previewHeight);
        }

        //该手机系统支持的所有预览分辨率
        String previewSizeValueString = null;

        //针对一般手机
        previewSizeValueString = parameters.get("preview-size-values");

        //针对索尼Xperia
        if(previewSizeValueString == null)
        {
            previewSizeValueString = parameters.get("preview-size-value");
        }

        //针对魅族m9(获取不到支持的预览大小，就直接返回手机屏幕大小)
        if(previewSizeValueString == null)
        {
            return camera.new Size(getScreenWidthInPixel(context), getScreenHeightInPixel(context));
        }

        String[] commaPattern = previewSizeValueString.split(",");

        /**
         * 下面找出和previewRatio尺寸比例最接近的预览图尺寸比例
         */
        double bestX = 0, bestY = 0, tmpX = 0, tmpY = 0;
        double bestRatio = 0, tmpRatio = 0;

        for(String previewSizeString : commaPattern)
        {
            previewSizeString = previewSizeString.trim();
            int index = previewSizeString.indexOf('x');

            if(index == -1) continue;

            try
            {
                tmpX = Double.parseDouble(previewSizeString.substring(0, index));
                tmpY = Double.parseDouble(previewSizeString.substring(index + 1));
            }
            catch (NumberFormatException e)
            {
                continue;
            }

            tmpRatio = Math.min(tmpX, tmpY) / Math.max(tmpX, tmpY);

            if(bestRatio == 0)
            {
                bestRatio = tmpRatio;
                bestX = tmpX;
                bestY = tmpY;
            }
            else if(bestRatio != 0 &&
                    (Math.abs(tmpRatio - previewRatio)) < (Math.abs(bestRatio - previewRatio)))
            {
                bestRatio = tmpRatio;
                bestX = tmpX;
                bestY = tmpY;
            }
        }

        if(bestX > 0 && bestY > 0)
        {
            return camera.new Size((int)bestX, (int)bestY);
        }

        return null;
    }


    /**
     * 将预览大小设置为屏幕大小
     * @param parameters
     * @param context
     * @param camera
     * @return
     */
    public static Camera.Size findPreviewSizeByScreen(Camera.Parameters parameters, Context context, Camera camera)
    {
        return camera.new Size(getScreenWidthInPixel(context), getScreenHeightInPixel(context));
    }


    /**
     * 获取手机屏幕的高度（单位：像素）
     * @param context
     * @return
     */
    public static int getScreenHeightInPixel(Context context)
    {
        int screenHeightInPixel = context.getResources().getDisplayMetrics().heightPixels;
        return screenHeightInPixel;
    }

    /**
     * 获取手机屏幕的宽度（单位：像素）
     * @param context
     * @return
     */
    public static int getScreenWidthInPixel(Context context)
    {
        int screenWidthInPixel = context.getResources().getDisplayMetrics().widthPixels;
        return screenWidthInPixel;
    }

    /**
     * 设置自定义的相机特性参数
     */
    public static void setCustomParameters(Camera.Parameters parameters, Context context, Camera camera)
    {
        List<String> focusModes = parameters.getSupportedFocusModes();

        //设置连续对焦
        if(focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
        {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        long time = new Date().getTime();
        parameters.setGpsTimestamp(time);

        //设置相片格式
        parameters.setPictureFormat(PixelFormat.JPEG);

        Camera.Size previewSize = findPreviewSizeByScreen(parameters, context, camera);

        //预览图和实际图尺寸一致
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        parameters.setPictureSize(previewSize.width, previewSize.height);

        //设置自动对焦
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        //若当前相机的预览方向不为横屏
        if(context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
        {
            //将预览界面顺时针旋转90度，设置为横屏
            camera.setDisplayOrientation(90);
            parameters.setRotation(90);
        }
    }

    /**
     * 更新相机设置
     */
    public static void updateCameraParameters(Camera camera, Context context, double previewWidth, double previewHeight)
    {
        if(camera != null)
        {
            Camera.Parameters parameters = camera.getParameters();

            setCustomParameters(parameters, context, camera);

            try
            {
                camera.setParameters(parameters);
            }
            catch (Exception e)
            {
                Camera.Size previewSize = findBestPreviewSize(parameters, context, camera, previewWidth, previewHeight);
                parameters.setPreviewSize(previewSize.width, previewSize.height);
                parameters.setPictureSize(previewSize.width, previewSize.height);
                camera.setParameters(parameters);
            }
        }
    }



    public static int clamp(int x, int min, int max)
    {
        if (x > max)
        {
            return max;
        }
        if (x < min)
        {
            return min;
        }
        return x;
    }

    /**
     * 计算焦点及测光区域
     *
     * @param focusWidth
     * @param focusHeight
     * @param areaMultiple
     * @param x
     * @param y
     * @param previewleft
     * @param previewRight
     * @param previewTop
     * @param previewBottom
     * @return Rect(left, top, right, bottom) : left、top、right、bottom是以显示区域中心为原点的坐标
     */
    public static Rect calculateTapArea(int focusWidth, int focusHeight, float areaMultiple, float x, float y, int previewleft, int previewRight, int previewTop, int previewBottom)
    {
        int areaWidth = (int) (focusWidth * areaMultiple);
        int areaHeight = (int) (focusHeight * areaMultiple);
        int centerX = (previewleft + previewRight) / 2;
        int centerY = (previewTop + previewBottom) / 2;
        double unitx = ((double) previewRight - (double) previewleft) / 2000;
        double unity = ((double) previewBottom - (double) previewTop) / 2000;
        int left = clamp((int) (((x - areaWidth / 2) - centerX) / unitx), -1000, 1000);
        int top = clamp((int) (((y - areaHeight / 2) - centerY) / unity), -1000, 1000);
        int right = clamp((int) (left + areaWidth / unitx), -1000, 1000);
        int bottom = clamp((int) (top + areaHeight / unity), -1000, 1000);

        return new Rect(left, top, right, bottom);
    }


}
