package com.wuchangi.searchforanswer.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by WuchangI on 2019/3/11.
 */


public class TypefaceUtils {

    private static TypefaceUtils sTypefaceUtils;
    private Context mContext;
    private Typeface mTypeface1;

    private TypefaceUtils(){
    }

    public static TypefaceUtils getInstance(){
        return sTypefaceUtils == null ? sTypefaceUtils = new TypefaceUtils(): sTypefaceUtils;
    }

    public void init(Context context){
        mContext = context;
    }

    public Typeface getTypeface1(){
        return mTypeface1 == null ? mTypeface1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/font1.ttf"): mTypeface1;
    }
}
