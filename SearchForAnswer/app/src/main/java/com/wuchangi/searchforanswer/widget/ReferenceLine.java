package com.wuchangi.searchforanswer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.wuchangi.searchforanswer.utils.CameraPreviewUtils;

/**
 * Created by WuchangI on 2018/8/6.
 */

/**
 * 相机预览界面的参考线
 */
public class ReferenceLine extends View
{
    private Paint linePaint;

    public ReferenceLine(Context context)
    {
        super(context);

        //初始化画笔
        initPaint();
    }


    public ReferenceLine(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        //初始化画笔
        initPaint();
    }


    public ReferenceLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        //初始化画笔
        initPaint();
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        int screenWidth = CameraPreviewUtils.getScreenWidthInPixel(getContext());
        int screenHeight = CameraPreviewUtils.getScreenHeightInPixel(getContext());

        int width = screenWidth / 3;
        int height = screenHeight / 3;

        //绘制两条竖线
        for(int i = width, j = 0; i < screenWidth && j < 2; i += width, j++)
        {
            canvas.drawLine(i, 0, i, screenHeight, linePaint);
        }

        //绘制两条横线
        for(int i = height, j = 0; i < screenHeight && j < 2; i += height, j++)
        {
            canvas.drawLine(0, i, screenWidth, i, linePaint);
        }
    }

    /**
     * 初始化画笔
     */
    private void initPaint()
    {
        linePaint = new Paint();

        //防止边缘的锯齿
        linePaint.setAntiAlias(true);

        linePaint.setColor(Color.WHITE);

        //设置线宽
        linePaint.setStrokeWidth(1);
    }


}
