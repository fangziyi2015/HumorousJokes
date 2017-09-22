package com.fangz.humorousjokes.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fangz.humorousjokes.R;
import com.fangz.humorousjokes.app.JokeApplication;
import com.fangz.humorousjokes.bean.WeatherResponse;
import com.fangz.humorousjokes.constants.Constant;
import com.fangz.humorousjokes.db.Citys;
import com.fangz.humorousjokes.utils.L;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WeatherActivity extends AppCompatActivity {

    String provinceName = "";
    String cityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        requestCityList();

        Cursor cursor = DataSupport.findBySQL("select * from Citys where provinceName = '北京'");
        cursor.moveToFirst();
        while (cursor.moveToNext()){
            String city = cursor.getString(cursor.getColumnIndex("cityName"));
            L.d("city====>"+city);

        }

    }

    private void requestCityList() {
        final Observable<WeatherResponse> weatherInfo = JokeApplication.getWeatherApi().getWeatherInfo(Constant.Key.weather_value);
        Logger.i("==>" + weatherInfo, "==");
        weatherInfo.flatMap(new Function<WeatherResponse, ObservableSource<List<WeatherResponse.ResultCity>>>() {
            @Override
            public ObservableSource<List<WeatherResponse.ResultCity>> apply(@NonNull WeatherResponse weatherResponse) throws Exception {
                return Observable.just(weatherResponse.getResult());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<WeatherResponse.ResultCity>>() {
                    @Override
                    public void accept(List<WeatherResponse.ResultCity> resultCities) throws Exception {
                        for (WeatherResponse.ResultCity city : resultCities) {
                            //Logger.i("省："+city.getProvince()+"\n市："+city.getCity()+"\n县："+city.getDistrict());
                            Citys c = new Citys(Integer.valueOf(city.getId()),city.getProvince(),city.getCity(),city.getDistrict());
                            c.save();
                        }

                    }
                });

    }
}
