package com.wuchangi.searchforanswer.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wuchangi.searchforanswer.R;

/**
 * Created by WuchangI on 2018/8/10.
 */


/**
 * “个人信息” Tab页
 */
public class ProfileFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_profile, null);
        return rootView;
    }
}
