package com.syq.commonlibrary.utils;

import android.util.Log;


/**
 * 自定义日志类
 */
public class Logger {
    private static boolean isDebug = true;
    private static ShowLevel showLevel = ShowLevel.DEBUG;
    public static void i(String tag,String msg){
        i(tag,msg,showLevel);
    }

    public static void i(String tag,String msg,ShowLevel level){
        boolean shouldShow = false;
        if(isDebug){
            if(level != ShowLevel.NONE){
                shouldShow = true;
            }
        }else if(level == ShowLevel.PRODUCT){
            shouldShow = true;
        }

        if(shouldShow){
            Log.i(tag,msg);
        }
    }

    enum ShowLevel{
        NONE,
        DEBUG,
        PRODUCT
    }
}
