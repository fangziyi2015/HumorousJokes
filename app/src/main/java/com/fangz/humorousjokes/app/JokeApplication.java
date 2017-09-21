package com.fangz.humorousjokes.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by zhangtao on 2017/9/21.
 */

public class JokeApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LitePalApplication.initialize(this);
    }

    public static Context getContext() {
        if (mContext == null){
            throw new RuntimeException("Application context is null,You may not have the name attribute in the androidmanifist-xml file");
        }
        return mContext;
    }
}
