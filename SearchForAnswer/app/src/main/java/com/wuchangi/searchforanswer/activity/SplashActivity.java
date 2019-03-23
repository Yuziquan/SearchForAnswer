package com.wuchangi.searchforanswer.activity;

import android.content.Intent;
import android.os.Bundle;
import com.wuchangi.searchforanswer.R;
import com.wuchangi.searchforanswer.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 启动页（欢迎界面）
 */
public class SplashActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));



                SplashActivity.this.finish();
            }
        };

        //两秒后跳转
        timer.schedule(timerTask, 2000);
    }
}
