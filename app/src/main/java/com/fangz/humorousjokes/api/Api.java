package com.fangz.humorousjokes.api;

import com.fangz.humorousjokes.bean.JokeResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by zhangtao on 2017/9/21.
 */

public interface Api {

    @GET("list.from")
    public Observable<JokeResponse> getJoke(@QueryMap Map<String,String> map);
}
