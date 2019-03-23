package com.wuchangi.searchforanswer.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuchangI on 2018/8/10.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter
{
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentsTitles = new ArrayList<>();

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> fragmentsTitles)
    {
        super(fm);
        this.fragments = fragments;
        this.fragmentsTitles = fragmentsTitles;
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return fragmentsTitles.get(position);
    }
}
