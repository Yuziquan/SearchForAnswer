package com.wuchangi.searchforanswer.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.*;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.wuchangi.searchforanswer.utils.TypefaceUtils;

/**
 * Created by WuchangI on 2018/8/10.
 */

public class BaseApplication extends Application
{
    private static BaseApplication sBaseApplication;

    /**
     * 获取全局上下文对象
     *
     * @return
     */
    public static BaseApplication getInstance() {
        return sBaseApplication == null ? sBaseApplication = new BaseApplication() : sBaseApplication;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        initTypefaceUtils();
        initSmartRefreshLayout();
    }

    /**
     * 初始化字体工具类
     */
    private void initTypefaceUtils() {
        TypefaceUtils.getInstance().init(this);
    }


    /**
     * 初始化SmartRefreshLayout的Header和Footer
     */
    public void initSmartRefreshLayout()
    {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater()
        {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout)
            {
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });


        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater()
        {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout)
            {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }
}
