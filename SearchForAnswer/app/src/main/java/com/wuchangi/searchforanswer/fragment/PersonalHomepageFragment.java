package com.wuchangi.searchforanswer.fragment;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.wuchangi.searchforanswer.R;
import com.wuchangi.searchforanswer.adapter.MyFragmentPagerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuchangI on 2018/8/4.
 */


/**
 * “个人主页”功能页
 */
public class PersonalHomepageFragment extends Fragment
{
    private static final String TAG = "PersonalHomepageFragmen";
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentsTitles = new ArrayList<>();

    private ImmersionBar immersionBar;

    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private TabLayout tabLayout;
    private SmartRefreshLayout refreshLayout;
    private Toolbar toolbar;
    private ButtonBarLayout buttonBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView headBgImageView, bigHeadImageView, smallHeadImageView;

    //状态栏字体是否是黑色
    private boolean isBlack = false;
    //状态栏字体是否是白色
    private boolean isWhite = true;

    public PersonalHomepageFragment()
    {

    }

    @SuppressLint("ValidFragment")
    public PersonalHomepageFragment(ImmersionBar immersionBar)
    {
        this.immersionBar = immersionBar;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //数据准备
        initData();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_personal_homepage, container, false);


        //界面初始化
        initView(rootView);

        initViewPager();

        initListeners();

        return rootView;
    }


    /**
     * 界面初始化
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initView(View view)
    {
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refresh_layout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        buttonBarLayout = (ButtonBarLayout) view.findViewById(R.id.buttonbar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbarLayout);
        headBgImageView = (ImageView) view.findViewById(R.id.iv_head_bg);
        bigHeadImageView = (ImageView) view.findViewById(R.id.iv_big_head);
        smallHeadImageView = (ImageView) view.findViewById(R.id.iv_small_head);

        immersionBar.titleBar(toolbar).init();

        RequestOptions requestOptions = new RequestOptions();

        //加载圆形的大头像
        Glide.with(getActivity()).
                load(R.drawable.head).
                apply(requestOptions.circleCrop()).into(bigHeadImageView);

        //加载圆形的小头像
        Glide.with(getActivity()).
                load(R.drawable.head).
                apply(requestOptions.circleCrop()).into(smallHeadImageView);
    }


    /**
     * 数据准备
     */
    public void initData()
    {
        fragments.add(new ProfileFragment());
        fragments.add(new GameRecordFragment());
        fragments.add(new AboutFragment());

        fragmentsTitles.add("我");
        fragmentsTitles.add("游戏记录");
        fragmentsTitles.add("关于此应用");
    }


    public void initViewPager()
    {

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments, fragmentsTitles);

        viewPager.setAdapter(myFragmentPagerAdapter);


        //默认进入的第一个Tab页面
        viewPager.setCurrentItem(0);

        //设置TabLayout和ViewPager两者关联
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(Color.BLUE);

        //设置TabLayout的下划线（指示器）的宽度
        tabLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                setIndicator(tabLayout, 30, 30);
            }
        });


    }


    public void initListeners()
    {
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener()
        {
            //用户的垂直滑动距离（进行刷新时）为offset
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight)
            {
                //用户头像的背景图实际移动的距离，这样做会产生一定的错位偏移，增加了视觉效果
                int offsetTmp = offset / 2;

              //  Log.d(TAG, "onHeaderPulling: " + offset);

                headBgImageView.setTranslationY(offsetTmp);

            }

            //用户的垂直滑动距离（释放刷新时）为offset
            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int footerHeight, int extendHeight)
            {
                int offsetTmp = offset / 2;

             //   Log.d(TAG, "onHeaderReleasing: " + offset);

                headBgImageView.setTranslationY(offsetTmp);
            }
        });


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            /**
             * 1、这个方法在SmartRefreshLayout下拉刷新的时候也会被不断调用（此时AppBarLayout已经超过原本的高度），故此时
             * verticalOffset为0。当AppBarLayout向上收缩时，verticalOffset为负数。
             *
             * 2、打日志后发现，onOffsetChanged总是在SmartRefreshLayout的onHeaderPulling/onHeaderReleasing之后执行，为了
             * 防止前者的 “headBgImageView.setTranslationY(verticalOffset);” 覆盖后者的
             * “headBgImageView.setTranslationY(offsetTmp)” ，当verticalOffset为0时，不执行前者的。
             *
             * @param appBarLayout
             * @param verticalOffset
             */
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                if(verticalOffset != 0)
                {
                    //设置用户头像的背景图随着AppBarLayout的向上折叠（向下展开）而向上（下）移动（速度相同）
                    headBgImageView.setTranslationY(verticalOffset);

                    //Log.d(TAG, "onOffsetChanged: " + verticalOffset);
                }

                /**
                 * 1、其中200dp是AppBarLayout的高度，AppBarLayout向上滑动的偏移量verticalOffset为负数。
                 * 2、当AppBarLayout向上滑动的距离=AppBarLayout的底部到ToolBar底部的距离（即AppBarLayout的高度减去ToolBar的高度）。
                 * 3、也就是CollapsingToolbarLayout完全折叠时。
                 */
                if (Math.abs(verticalOffset) == DensityUtil.dp2px(200) - toolbar.getHeight())
                {
                    //若状态栏字体为白色
                    if (isWhite)
                    {
                        //将状态栏的字体改为黑色
                        if (ImmersionBar.isSupportStatusBarDarkFont())
                        {
                            immersionBar.statusBarDarkFont(true).init();
                            isBlack = true;
                            isWhite = false;
                        }
                    }

                    //设置顶部的ButtonBarLayout可见
                    buttonBarLayout.setVisibility(View.VISIBLE);

                    //设置当CollapsingToolbarLayout完全折叠时的颜色(白色)
                    collapsingToolbarLayout.setContentScrimColor(Color.WHITE);
                }
                else
                {
                    //若状态栏字体为黑色
                    if(isBlack)
                    {
                        //将状态栏的字体改为白色
                        immersionBar.statusBarDarkFont(false).init();
                        isBlack = false;
                        isWhite = true;
                    }

                    //设置顶部的ButtonBarLayout不可见
                    buttonBarLayout.setVisibility(View.INVISIBLE);

                    //设置当CollapsingToolbarLayout完全折叠时的颜色(透明)
                    collapsingToolbarLayout.setContentScrimColor(Color.TRANSPARENT);

                }
            }
        });



    }



    /**
     * 设置TabLayout的下划线（指示器）的宽度。通过反射修改TabLayout的宽，其实相当于margin。
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip)
    {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try
        {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try
        {
            llTab = (LinearLayout) tabStrip.get(tabs);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++)
        {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


}
