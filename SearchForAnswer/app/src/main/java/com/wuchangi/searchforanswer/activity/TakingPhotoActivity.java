package com.wuchangi.searchforanswer.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.*;

import com.edmodo.cropper.CropImageView;
import com.wuchangi.searchforanswer.R;
import com.wuchangi.searchforanswer.base.BaseActivity;
import com.wuchangi.searchforanswer.base.BitmapEvent;
import com.wuchangi.searchforanswer.widget.CameraPreview;
import com.wuchangi.searchforanswer.widget.FocusView;
import com.wuchangi.searchforanswer.utils.BitmapUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;

/**
 * 拍照界面
 */
public class TakingPhotoActivity extends BaseActivity implements View.OnClickListener, SensorEventListener {
    private static final String TAG = "TakingPhotoActivity";

    private static boolean sFlag = true; // true:裁剪题目图片， false:裁剪答案图片
    private CameraPreview cameraPreview;
    private CropImageView cropImageView;
    private RelativeLayout previewLayout;
    private LinearLayout cropLayout;
    private Button albumButton;
    private ImageView shutterImageView, closePreviewImageView;
    private ImageView closeCropImageView, startCropImageView;
    private FocusView focusView;
    private TextView previewHintTextView, cropHintTextView;

    private SensorManager sensorManager;
    private Sensor sensor;
    private double lastX = 0.0, lastY = 0.0, lastZ = 0.0;
    private boolean isInitialized = false;

    //旋转文字
    private boolean isTextRotated = false;

    //拍摄后,待裁剪的照片
    private Bitmap bitmapToBeCropped;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //准备裁剪
            if (msg.what == 0x123) {
                //获取拍摄得到的图片
                bitmapToBeCropped = cameraPreview.getBitmap();

                //设置该图片为待裁剪的图片
                cropImageView.setImageBitmap(bitmapToBeCropped);

                //展示裁剪界面
                showCroppingPhotoLayout();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taking_photo);

        //界面初始化
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //取消注册
        sensorManager.unregisterListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!isTextRotated) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(previewHintTextView, "rotation", 0f, 90f);
            animator.setStartDelay(800);
            animator.setDuration(500);
            animator.setInterpolator(new LinearInterpolator());
            animator.start();
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(shutterImageView, "rotation", 0f, 90f);
            animator1.setStartDelay(800);
            animator1.setDuration(500);
            animator1.setInterpolator(new LinearInterpolator());
            animator1.start();
            AnimatorSet animSet = new AnimatorSet();
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(cropHintTextView, "rotation", 0f, 90f);
            ObjectAnimator moveIn = ObjectAnimator.ofFloat(cropHintTextView, "translationX", 0f, -50f);
            animSet.play(animator2).before(moveIn);
            animSet.setDuration(10);
            animSet.start();
            isTextRotated = true;
        }

        //为系统的加速度传感器注册监听器
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }


    /**
     * 界面的初始化
     */
    public void initView() {
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /**
         * 获取控件实例并绑定监听器
         */
        previewLayout = (RelativeLayout) findViewById(R.id.preview_layout);
        cropLayout = (LinearLayout) findViewById(R.id.crop_layout);

        cameraPreview = (CameraPreview) findViewById(R.id.camera_preview);
        cropImageView = (CropImageView) findViewById(R.id.iv_crop);

        albumButton = (Button) findViewById(R.id.btn_album);
        albumButton.setOnClickListener(this);

        shutterImageView = (ImageView) findViewById(R.id.iv_shutter);
        shutterImageView.setOnClickListener(this);

        closePreviewImageView = (ImageView) findViewById(R.id.iv_close_preview);
        closePreviewImageView.setOnClickListener(this);

        closeCropImageView = (ImageView) findViewById(R.id.iv_close_crop);
        closeCropImageView.setOnClickListener(this);

        startCropImageView = (ImageView) findViewById(R.id.iv_start_crop);
        startCropImageView.setOnClickListener(this);

        focusView = (FocusView) findViewById(R.id.focus_view);

        previewHintTextView = (TextView) findViewById(R.id.tv_preview_hint);
        cropHintTextView = (TextView) findViewById(R.id.tv_crop_hint);

        //设置裁剪时的参考线
        cropImageView.setGuidelines(2);

        //为预览设置对焦时的图片
        cameraPreview.setFocusView(focusView);

        //设置处理器
        cameraPreview.setHandler(handler);

        //获取传感器管理者
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //获取系统的加速度传感器
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //打开手机相册
            case R.id.btn_album:

                openAlbum();
                break;

            //按下快门
            case R.id.iv_shutter:

                if (cameraPreview != null) {
                    //进行拍照
                    cameraPreview.takePicture();
                }
                break;

            //关闭相机预览
            case R.id.iv_close_preview:

                //返回主界面
                finish();
                break;

            //关闭照片裁剪界面
            case R.id.iv_close_crop:

                //展示拍照预览界面
                showTakingPhotoLayout();
                break;

            //裁剪提交
            case R.id.iv_start_crop:

                //获取裁剪后的图片
                Bitmap cropBitmap = cropImageView.getCroppedImage();

                //逆时针旋转90度
                Bitmap bitmap = BitmapUtils.rotateBitmap(cropBitmap, -90);

                //系统当前时间
                long dateTaken = System.currentTimeMillis();

                //图片名称
                String bitmapName = DateFormat.format("yyyy-MM-dd kk.mm.ss", dateTaken).toString() + ".jpg";

                submitBitmap(bitmapName, bitmap);

                break;
        }
    }


    /**
     * 位移、自动对焦
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        if (!isInitialized) {
            lastX = x;
            lastY = y;
            lastZ = z;
            isInitialized = true;
        }

        double deltaX = Math.abs(lastX - x);
        double deltaY = Math.abs(lastY - y);
        double deltaZ = Math.abs(lastZ - z);

        if (deltaX > 0.8 || deltaY > 0.8 || deltaZ > 0.8) {
            cameraPreview.setAutoFocus();
        }

        lastX = x;
        lastY = y;
        lastZ = z;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 打开手机相册
     */
    public void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }


    /**
     * 获取用户最终选择的照片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:

                try {
                    //获取用户在相册中最终选择的图片
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));

                    //与拍照保持一致，方便处理（顺时针旋转90度）
                    bitmap = BitmapUtils.rotateBitmap(bitmap, 90);

                    //设置该图片为待裁剪的图片
                    cropImageView.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }

                //展示裁剪界面
                showCroppingPhotoLayout();

                break;

            default:
                break;
        }
    }


    /**
     * 展示拍照预览界面
     */
    public void showTakingPhotoLayout() {
        previewLayout.setVisibility(View.VISIBLE);
        cropLayout.setVisibility(View.GONE);
    }

    /**
     * 展示裁剪界面
     */
    public void showCroppingPhotoLayout() {
        previewLayout.setVisibility(View.GONE);
        cropLayout.setVisibility(View.VISIBLE);
        //开启预览
        cameraPreview.startPreview();
    }


    /**
     * 提交裁剪后的图片
     */
    public void submitBitmap(String bitmapName, final Bitmap unhandledBitmap) {

        //显示等待条
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("图片正在处理中...");
        progressDialog.show();

        //处理图片
        new Thread(new Runnable() {
            @Override
            public void run() {

                //图片压缩
                Bitmap compressedBitmap = BitmapUtils.compressBitmap(unhandledBitmap);
                Log.d(TAG, "图片压缩完");

                //灰度化处理
                Bitmap grayHandledBitmap = BitmapUtils.bitmapConvertGray(compressedBitmap);
                Log.d(TAG, "图片灰度化完");


                //二值化处理
//                Bitmap binaryHandledBitmap = BitmapUtils.bitmapBinarization(grayHandledBitmap);
//                Log.d(TAG, "图片二值化完");


                //销毁等待条
                progressDialog.dismiss();

                BitmapEvent bitmapEvent = new BitmapEvent();
                bitmapEvent.setBitmap(grayHandledBitmap);

                EventBus.getDefault().postSticky(bitmapEvent);
                Intent intent = new Intent(TakingPhotoActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        }).start();

    }
}
