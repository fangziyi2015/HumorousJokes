package com.fangz.humorousjokes.api;

import com.fangz.humorousjokes.bean.WeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhangtao on 2017/9/22.
 */

public interface WeatherApi {

    @GET("citys")
    public Observable<WeatherResponse> getWeatherInfo(@Query("key") String key);
}
