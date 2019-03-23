package com.wuchangi.searchforanswer.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by WuchangI on 2018/8/6.
 */

/**
 * 相机对焦时显示的图片
 */
public class FocusView extends View
{
    private Paint linePaint;

    //边界线宽度
    private static final int borderWidth = 4;

    //是否正在触摸对焦
    private boolean isFocusing = false;

    private AnimatorSet animatorSet;
    private ObjectAnimator fadeInOut;



    public FocusView(Context context)
    {
        super(context);

        //初始化界面
        initView();
    }


    public FocusView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        //初始化界面
        initView();
    }


    public FocusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        //初始化界面
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView()
    {
        linePaint = new Paint();

        //防止边缘的锯齿
        linePaint.setAntiAlias(true);

        //设置为描边
        linePaint.setStyle(Paint.Style.STROKE);

        //设置为灰色
        linePaint.setColor(Color.parseColor("#45ffffff"));

        //设置线宽
        linePaint.setStrokeWidth(borderWidth);

        //初始时设置为全透明
        this.setAlpha(0);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2 - borderWidth, linePaint);
    }

    private void setMainColor()
    {
        //设置为绿色
        linePaint.setColor(Color.parseColor("#00FF00"));
        postInvalidate();
    }

    private void reset()
    {
        //设置为灰色
        linePaint.setColor(Color.parseColor("#45e0e0e0"));
        postInvalidate();
    }

    //触摸对焦
    public void beginTouchFocus()
    {
        isFocusing = true;
        if (animatorSet == null)
        {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.3f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.3f, 1f);
            animatorSet = new AnimatorSet();
            animatorSet.play(scaleX).with(scaleY);
            animatorSet.setInterpolator(new LinearInterpolator());
            animatorSet.setDuration(1000);
            animatorSet.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {
                    FocusView.this.setAlpha(1f);
                }

                @Override
                public void onAnimationRepeat(Animator animation)
                {
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    setMainColor();
                    if (fadeInOut == null)
                    {
                        fadeInOut = ObjectAnimator.ofFloat(FocusView.this, "alpha", 1f, 0f);
                        fadeInOut.setDuration(500);
                        fadeInOut.addListener(new Animator.AnimatorListener()
                        {
                            @Override
                            public void onAnimationStart(Animator animation)
                            {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation)
                            {
                                reset();
                                isFocusing = false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation)
                            {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation)
                            {
                            }
                        });
                    }
                    fadeInOut.start();
                }

                @Override
                public void onAnimationCancel(Animator animation)
                {
                }
            });
        }
        else
        {
            if (animatorSet.isRunning())
            {
                animatorSet.cancel();
            }
            if (fadeInOut != null && fadeInOut.isRunning())
            {
                fadeInOut.cancel();
            }
        }
        animatorSet.start();
    }

    //是否正在触摸对焦
    public boolean isFocusing()
    {
        return isFocusing;
    }

}
