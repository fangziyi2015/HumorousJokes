package com.fangz.humorousjokes.app;

import android.app.Application;
import android.content.Context;

import com.fangz.humorousjokes.api.JokeApi;
import com.fangz.humorousjokes.api.WeatherApi;
import com.fangz.humorousjokes.constants.Url;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.litepal.LitePalApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangtao on 2017/9/21.
 */

public class JokeApplication extends Application {

    private static Context mContext;
    private static JokeApi mJokeApi;
    private static WeatherApi mWeatherApi;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LitePalApplication.initialize(this);

        Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return com.fangz.humorousjokes.BuildConfig.LOG_DEBUG;
            }
        });
    }

    public static Context getContext() {
        if (mContext == null){
            throw new RuntimeException("Application context is null,You may not have the name attribute in the androidmanifist-xml file");
        }
        return mContext;
    }



    public static JokeApi getJokeApi(){
        if (mJokeApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Url.jokeBaseUrl)
                    .build();

            mJokeApi = retrofit.create(JokeApi.class);
        }

        return mJokeApi;
    }

    public static WeatherApi getWeatherApi(){
        if (mWeatherApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Url.weatherBaseUrl)
                    .build();

            mWeatherApi = retrofit.create(WeatherApi.class);
        }

        return mWeatherApi;
    }


}
