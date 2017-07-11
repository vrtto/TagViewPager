package com.example.tagviewpager.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Description：工具类
 * Author：lxl
 * Date： 2017/7/5 16:51
 */
public class AndroidUtils {

    /**
     * 获取屏幕的宽度 <br/>
     *
     * @param [cx]-[上下文对象] <br/>
     * @return 屏幕宽度（单位px）
     */
    public static float getDeviceWidth(Context cx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度 <br/>
     *
     * @param [cx]-[上下文对象] <br/>
     * @return 屏幕高度（单位px）
     */
    public static float getDeviceHight(Context cx) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }


}
