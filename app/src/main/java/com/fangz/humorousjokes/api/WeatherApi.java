package com.fangz.humorousjokes.api;

import com.fangz.humorousjokes.bean.CitysResponse;
import com.fangz.humorousjokes.bean.WeatherIconInfoResponse;
import com.fangz.humorousjokes.bean.WeatherResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by zhangtao on 2017/9/22.
 */

public interface WeatherApi {

    @GET("citys")
    public Observable<CitysResponse> getCitysListInfo(@Query("key") String key);

    @GET("index")
    public Observable<WeatherResponse> getWeatherInfo(@QueryMap Map<String,String> params);

    @GET("uni")
    public Observable<WeatherIconInfoResponse> getWeatherIconCode(@Query("key") String key);
}
