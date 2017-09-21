package com.fangz.humorousjokes.ui.activity;

import android.os.Bundle;
import android.util.Log;

import com.fangz.humorousjokes.R;
import com.fangz.humorousjokes.api.Api;
import com.fangz.humorousjokes.bean.JokeResponse;
import com.fangz.humorousjokes.constants.Constant;
import com.fangz.humorousjokes.constants.Url;
import com.fangz.humorousjokes.ui.BaseActivity;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {

    private Api mApi;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.baseUrl)
                .build();

        mApi = retrofit.create(Api.class);

        HashMap<String, String> map = new HashMap();
        map.put("key", Constant.Key.value);
        map.put("page", "1");
        map.put("pagesize", "10");
        map.put("sort", "asc");
        map.put("time", "1499999999");


        Observable<JokeResponse> jokeResponseObservable = mApi.getJoke(map);
        Log.i(TAG, "onCreate: "+jokeResponseObservable);

        jokeResponseObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JokeResponse>() {
            @Override
            public void accept(JokeResponse jokeResponse) throws Exception {
                Log.i(TAG, "accept: "+jokeResponse.toString());
                List<JokeResponse.ResultData.JokeContent> datas = jokeResponse.getResult().getData();
                for (JokeResponse.ResultData.JokeContent content :datas) {
                    Log.i(TAG, "accept: "+content.getContent());
                }
            }
        });
    }
}
