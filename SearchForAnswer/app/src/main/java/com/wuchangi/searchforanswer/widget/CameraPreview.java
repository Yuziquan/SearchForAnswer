package com.wuchangi.searchforanswer.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wuchangi.searchforanswer.utils.BitmapUtils;
import com.wuchangi.searchforanswer.utils.CameraPreviewUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by WuchangI on 2018/8/6.
 */


/**
 * 相机预览视图
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback
{
    private Bitmap bitmap;

    //处理器
    private Handler handler;

    //预览图的宽高
    private double previewWidth, previewHeight;

    //相机
    private Camera camera;

    //对焦时显示的图片
    private FocusView focusView;

    private SurfaceHolder surfaceHolder;


    //创建一个PictureCallback对象，当拍摄时调用其onPictureTaken方法
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback()
    {
        //当拍摄完成后调用
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            try
            {
                //获取拍摄得到的图片
                bitmap = BitmapUtils.getBitmapFromByteArray(data);

                //进入裁剪界面
                handler.sendEmptyMessage(0x123);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };


    public CameraPreview(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        surfaceHolder = getHolder();

        //传入一个SurfaceHolder.Callback对象使得我们可以觉察到下面的surface的创建和销毁
        surfaceHolder.addCallback(this);

        //绑定触摸监听器，监听用户的手动聚焦
        setOnTouchListener(onTouchListener);
    }


    /**
     * 当点击预览界面时显示焦点区域
     */
    OnTouchListener onTouchListener = new OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            //手指触摸屏幕时
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                focusView.setX(event.getX() - focusView.getWidth() / 2);
                focusView.setY(event.getY() - focusView.getHeight() / 2);

                focusView.beginTouchFocus();
            }
            //手指离开屏幕时
            else if(event.getAction() == MotionEvent.ACTION_UP)
            {
                //处理对焦时的事件
                handleFocus(event);
            }

            return true;
        }
    };


    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //若手机没有摄像头
        if(!CameraPreviewUtils.checkCameraHardware(getContext()))
        {
            Toast.makeText(getContext(), "该手机没有摄像头！", Toast.LENGTH_SHORT).show();
            return;
        }

        //获取Camera对象
        camera = CameraPreviewUtils.getCameraInstance(getContext());

        //没有可用的摄像头
        if(camera == null)
        {
            Toast.makeText(getContext(), "检测不到可用的摄像头！", Toast.LENGTH_SHORT).show();
            return;
        }


        //设置自定义的相机参数
        CameraPreviewUtils.updateCameraParameters(camera, getContext(), previewWidth, previewHeight);

        try
        {
            //设置相机的预览在哪显示(在surfaceView显示)
            camera.setPreviewDisplay(surfaceHolder);
        }
        catch (IOException e)
        {
            Toast.makeText(getContext(), "设置摄像头预览失败！", Toast.LENGTH_SHORT).show();
            camera.release();
            camera = null;
            return;
        }

        //开启预览界面
        camera.startPreview();

        //自动对焦
        setAutoFocus();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        if(camera != null)
        {
            //先停止预览
            camera.stopPreview();

            //设置自定义的相机参数
            CameraPreviewUtils.updateCameraParameters(camera, getContext(), previewWidth, previewHeight);

            try
            {
                //设置相机的预览在哪显示(在surfaceView显示)
                camera.setPreviewDisplay(surfaceHolder);
            }
            catch (IOException e)
            {
                Toast.makeText(getContext(), "设置摄像头预览失败！", Toast.LENGTH_SHORT).show();
                camera.release();
                camera = null;
                return;
            }

            //开启预览界面
            camera.startPreview();

            //自动对焦
            setAutoFocus();
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        camera.release();
        camera = null;
    }


    /**
     * 设置预览图的尺寸
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //获取预览图的宽高
        previewWidth = MeasureSpec.getSize(widthMeasureSpec);
        previewHeight = MeasureSpec.getSize(heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置对焦时显示的图片
     * @param focusView
     */
    public void setFocusView(FocusView focusView)
    {
        this.focusView = focusView;
    }

    /**
     * 设置自动对焦，并且对焦的圆圈显示在屏幕中间
     */
    public void setAutoFocus()
    {
        //还没触摸对焦
        if(!focusView.isFocusing())
        {
            camera.autoFocus(this);

            focusView.setX(CameraPreviewUtils.getScreenWidthInPixel(getContext()) / 2 - focusView.getWidth() / 2);
            focusView.setY(CameraPreviewUtils.getScreenHeightInPixel(getContext()) / 2 - focusView.getHeight() / 2);

            //实现触摸对焦的效果
            focusView.beginTouchFocus();
        }

    }

    /**
     * 对焦时的相关处理：设置焦点和测光区域
     */
    public void handleFocus(MotionEvent event)
    {
        int[] location = new int[2];
        RelativeLayout relativeLayout = (RelativeLayout) getParent();
        relativeLayout.getLocationOnScreen(location);

        Rect focusRect = CameraPreviewUtils.calculateTapArea(focusView.getWidth(), focusView.getHeight(), 1f, event.getRawX(), event.getRawY(), location[0], location[0] + relativeLayout.getWidth(), location[1], location[1] + relativeLayout.getHeight());
        Rect meteringRect = CameraPreviewUtils.calculateTapArea(focusView.getWidth(), focusView.getHeight(), 1.5f, event.getRawX(), event.getRawY(), location[0], location[0] + relativeLayout.getWidth(), location[1], location[1] + relativeLayout.getHeight());

        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        if (parameters.getMaxNumFocusAreas() > 0)
        {
            List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
            focusAreas.add(new Camera.Area(focusRect, 1000));

            parameters.setFocusAreas(focusAreas);
        }

        if (parameters.getMaxNumMeteringAreas() > 0)
        {
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
            meteringAreas.add(new Camera.Area(meteringRect, 1000));

            parameters.setMeteringAreas(meteringAreas);
        }

        camera.setParameters(parameters);

        camera.autoFocus(this);
    }

    /**
     * 开启预览
     */
    public void startPreview()
    {
        if(camera != null)
        {
            camera.startPreview();
        }
    }

    /**
     * 关闭预览
     */
    public void stopPreview()
    {
        if(camera != null)
        {
            camera.stopPreview();
        }
    }

    /**
     * 进行拍照，并将拍摄的照片传入Camera.PictureCallback接口的onPictureTaken方法
     */
    public void takePicture()
    {
        if(camera != null)
        {
            camera.takePicture(null, null, pictureCallback);
        }
    }


    @Override
    public void onAutoFocus(boolean success, Camera camera)
    {

    }


    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }


    public Bitmap getBitmap()
    {
        return bitmap;
    }
}
