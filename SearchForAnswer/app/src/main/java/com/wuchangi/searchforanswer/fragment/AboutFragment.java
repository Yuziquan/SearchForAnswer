package com.wuchangi.searchforanswer.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wuchangi.searchforanswer.R;

/**
 * Created by WuchangI on 2018/8/10.
 */

/**
 * “关于” Tab页
 */
public class AboutFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_about, null);

        return rootView;
    }
}
