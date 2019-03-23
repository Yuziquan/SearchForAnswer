package com.wuchangi.searchforanswer.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.wuchangi.searchforanswer.R;
import com.wuchangi.searchforanswer.base.BaseActivity;
import com.wuchangi.searchforanswer.fragment.PersonalHomepageFragment;
import com.wuchangi.searchforanswer.fragment.SearchFragment;
import com.wuchangi.searchforanswer.fragment.SmallGameFragment;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener
{
    private BottomNavigationBar bottomNavigationBar;
    private TextBadgeItem textBadgeItem;

    //主界面视图
    private View mainInterfaceView;

    /**
     * 三个功能页
     */
    private SearchFragment searchFragment;
    private SmallGameFragment smallGameFragment;
    private PersonalHomepageFragment personalHomepageFragment;


    //当前显示的Fragment
    private Fragment curFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //界面初始化
        initView();

    }


    /**
     * 界面初始化
     */
    public void initView()
    {
        //初始化四个功能页
        searchFragment = new SearchFragment();
        smallGameFragment = new SmallGameFragment();
        personalHomepageFragment = new PersonalHomepageFragment(immersionBar);


        //设置主界面的当前功能页
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, searchFragment)
                .commit();
        curFragment = searchFragment;


        //获取导航栏
        bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);

        //设置底部导航栏显示模式
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);

        //设置底部导航栏的背景风格样式
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        //设置选中项的颜色
        bottomNavigationBar.setActiveColor(R.color.green);


        //为导航栏添加各个选项
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.search, "拍照搜题"))
                .addItem(new BottomNavigationItem(R.drawable.small_game, "小游戏"))
                .addItem(new BottomNavigationItem(R.drawable.personal_homepage, "我"))
                .setFirstSelectedPosition(0)
                .initialise();

        //为导航栏设置监听器
        bottomNavigationBar.setTabSelectedListener(this);
    }


    /**
     * 设置导航栏的点击事件
     * @param position
     */
    @Override
    public void onTabSelected(int position)
    {
        switch(position)
        {
            case 0:
                switchToSpecifyFragment(searchFragment);
                break;

            case 1:
                switchToSpecifyFragment(smallGameFragment);
                break;

            case 2:
                switchToSpecifyFragment(personalHomepageFragment);
                break;

        }

    }


    @Override
    public void onTabUnselected(int position)
    {
    }

    @Override
    public void onTabReselected(int position)
    {

    }


    /**
     * 切换到指定功能页
     * @param fragment
     */
    public void switchToSpecifyFragment(Fragment fragment)
    {
        //若当前的Fragment不是目标Fragment
        if(fragment != curFragment)
        {
            //若目标fragment之前添加过
            if(fragment.isAdded())
            {
                //将当前的fragment隐藏，并显示目标fragment
                getSupportFragmentManager().beginTransaction()
                        .hide(curFragment)
                        .show(fragment)
                        .commit();
            }
            //若目标fragment之前没有添加过
            else
            {
                //将当前的fragment隐藏，并添加目标fragment
                getSupportFragmentManager().beginTransaction()
                        .hide(curFragment)
                        .add(R.id.frame_layout, fragment)
                        .commit();
            }

            curFragment = fragment;
        }

    }
}

