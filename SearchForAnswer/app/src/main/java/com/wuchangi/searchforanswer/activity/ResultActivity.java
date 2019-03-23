package com.wuchangi.searchforanswer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import com.wuchangi.searchforanswer.R;
import com.wuchangi.searchforanswer.base.BaseActivity;
import com.wuchangi.searchforanswer.utils.BitmapUtils;

/**
 * 测试结果界面
 */
public class ResultActivity extends BaseActivity
{

    private ImageView grayhandledImageView;
    private ImageView binaryHandledImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //界面初始化
        initView();
    }


    /**
     * 界面初始化
     */
    public void initView()
    {
        /**
         * 获取控件实例
         */
        grayhandledImageView = (ImageView) findViewById(R.id.iv_gray_handled);
        binaryHandledImageView = (ImageView)findViewById(R.id.iv_binary_handled);


        /**
         * 获取上一个Activity的数据
         */
        Intent intent = getIntent();

        Bitmap grayHandledBitmap = BitmapUtils.getBitmapFromByteArray(intent.getByteArrayExtra("grayHandledBitmap"));
        Bitmap binaryHandledBitmap = BitmapUtils.getBitmapFromByteArray(intent.getByteArrayExtra("binaryHandledBitmap"));

        grayhandledImageView.setImageBitmap(grayHandledBitmap);
        binaryHandledImageView.setImageBitmap(binaryHandledBitmap);

    }

}
