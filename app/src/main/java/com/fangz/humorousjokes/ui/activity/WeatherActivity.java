package com.fangz.humorousjokes.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.fangz.humorousjokes.R;
import com.fangz.humorousjokes.app.JokeApplication;
import com.fangz.humorousjokes.bean.CitysResponse;
import com.fangz.humorousjokes.bean.WeatherResponse;
import com.fangz.humorousjokes.constants.Constant;
import com.fangz.humorousjokes.db.Citys;
import com.fangz.humorousjokes.ui.BaseActivity;
import com.fangz.humorousjokes.utils.L;
import com.fangz.humorousjokes.utils.SPUtils;
import com.fangz.humorousjokes.utils.T;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.fangz.humorousjokes.utils.SPUtils.get;

public class WeatherActivity extends BaseActivity {
    private static final String FORMAT = "format";
    private static final String DEAULT_FARMAT = "2";// 返回格式：默认格式是 2
    private static final String CITY_NAME = "cityname";
    private static final String KEY = "key";

    @BindView(R.id.tv_current_location)
    TextView mTvCurrentLocation;
    @BindView(R.id.tv_current_date)
    TextView mTvCurrentDate;
    @BindView(R.id.tv_current_week)
    TextView mTvCurrentWeek;
    @BindView(R.id.tv_current_temp)
    TextView mTvCurrentTemp;
    @BindView(R.id.tv_current_weather)
    TextView mTvCurrentWeather;
    @BindView(R.id.tv_today_wind)
    TextView mTvTodayWind;
    @BindView(R.id.tv_low_temp)
    TextView mTvLowTemp;
    @BindView(R.id.tv_height_temp)
    TextView mTvHeightTemp;
    @BindView(R.id.tv_current_wind)
    TextView mTvCurrentWind;
    @BindView(R.id.tv_current_humidity)
    TextView mTvCurrentHumidity;
    @BindView(R.id.weather_photo)
    ImageView mWeatherPhoto;
    @BindView(R.id.weather_photo_2)
    ImageView mWeatherPhoto2;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_update_time)
    TextView mTvUpdateTime;
    @BindView(R.id.address_menu)
    ImageButton mAddressMenu;
    @BindView(R.id.add_city_btn)
    Button mAddCityBtn;
    @BindView(R.id.show_city_recycler_view)
    RecyclerView mShowCityRecyclerView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private int[] imageIds = new int[]{
            R.drawable.w_00,
            R.drawable.w_01,
            R.drawable.w_02,
            R.drawable.w_03,
            R.drawable.w_04,
            R.drawable.w_05,
            R.drawable.w_06,
            R.drawable.w_07,
            R.drawable.w_08,
            R.drawable.w_09,
            R.drawable.w_10,
            R.drawable.w_11,
            R.drawable.w_12,
            R.drawable.w_13,
            R.drawable.w_14,
            R.drawable.w_15,
            R.drawable.w_16,
            R.drawable.w_17,
            R.drawable.w_18,
            R.drawable.w_19,
            R.drawable.w_20,
            R.drawable.w_21,
            R.drawable.w_22,
            R.drawable.w_23,
            R.drawable.w_24,
            R.drawable.w_25,
            R.drawable.w_26,
            R.drawable.w_27,
            R.drawable.w_28,
            R.drawable.w_29,
            R.drawable.w_30,
            R.drawable.w_31,
            R.drawable.w_53,
    };

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            L.d("amapLocation===>" + amapLocation);
            if (amapLocation != null) {
                L.d("amapLocation.getErrorCode()===>" + amapLocation.getErrorCode());
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
//                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                    amapLocation.getLatitude();//获取纬度
//                    amapLocation.getLongitude();//获取经度
//                    amapLocation.getAccuracy();//获取精度信息
//                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                    amapLocation.getCountry();//国家信息
//                    amapLocation.getProvince();//省信息
//                    amapLocation.getCity();//城市信息
                    //城区信息
                    String district = amapLocation.getDistrict();
                    L.d("定位地址：" + district);
                    mCity = amapLocation.getCity();
                    L.d("定位城市：" + mCity);
                    requestWeatherInfo(mCity);
//                    amapLocation.getStreet();//街道信息
//                    amapLocation.getStreetNum();//街道门牌号信息
//                    amapLocation.getCityCode();//城市编码
//                    amapLocation.getAdCode();//地区编码
//                    amapLocation.getAoiName();//获取当前定位点的AOI信息
//                    amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
//                    amapLocation.getFloor();//获取当前室内定位的楼层
//                    amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                    //获取定位时间
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date date = new Date(amapLocation.getTime());
//                    df.format(date);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    requestWeatherInfo(mCity);

                }
            }
        }
    };

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String mCity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        mCity = (String) get(WeatherActivity.this, "city","");

//        UltimateBar ultimateBar = new UltimateBar(this);
//        ultimateBar.setImmersionBar();

        if (TextUtils.isEmpty(mCity)) {
            // 初始化高德地图定位
            initAMapLocation();
        }

        initSwipeRefreshLitener();

        initListener();

    }

    private void initListener() {
        mAddressMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mAddCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加城市
                mDrawerLayout.closeDrawers();
                T.showShort(WeatherActivity.this,"添加城市");
                Intent intent = new Intent(WeatherActivity.this,SearchCityActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initSwipeRefreshLitener() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeatherInfo(mCity);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initAMapLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        //mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        //mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();


    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(WeatherActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(WeatherActivity.this);
    }

    @Subscribe(sticky = true)
    public void reveierCityName(String cityName){
        L.d("选择的城市："+cityName);
        requestWeatherInfo(cityName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isRequestCity = (boolean) SPUtils.get(WeatherActivity.this,"isRequestCity",false);
        if (!isRequestCity){
            requestCityList();
        }
    }

    private void requestCityList() {
        final Observable<CitysResponse> weatherInfo = JokeApplication.getWeatherApi().getCitysListInfo(Constant.Key.weather_value);
        Logger.i("==>" + weatherInfo, "==");
        weatherInfo.flatMap(new Function<CitysResponse, ObservableSource<List<CitysResponse.ResultCity>>>() {
            @Override
            public ObservableSource<List<CitysResponse.ResultCity>> apply(@NonNull CitysResponse weatherResponse) throws Exception {
                return Observable.just(weatherResponse.getResult());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<CitysResponse.ResultCity>>() {
                    @Override
                    public void accept(List<CitysResponse.ResultCity> resultCities) throws Exception {
                        L.d("start time==>"+System.currentTimeMillis());
                        for (CitysResponse.ResultCity city:resultCities) {
                            Citys c = new Citys(Integer.valueOf(city.getId()),city.getProvince(),city.getCity(),city.getDistrict());
                            c.save();
                        }

                        SPUtils.put(WeatherActivity.this,"isRequestCity",true);
                        L.d("end time==>"+System.currentTimeMillis());
                    }
                });

    }

    public void requestWeatherInfo(String cityName) {

        SPUtils.put(WeatherActivity.this, "city", cityName);

        mTvCurrentLocation.setText(cityName);
        HashMap<String, String> map = new HashMap<>();
        map.put(FORMAT, DEAULT_FARMAT);
        map.put(CITY_NAME, cityName);
        map.put(KEY, Constant.Key.weather_value);

        final Observable<WeatherResponse> weatherInfo = JokeApplication.getWeatherApi().getWeatherInfo(map);
        L.d("weather==>" + weatherInfo);
        weatherInfo.flatMap(new Function<WeatherResponse, ObservableSource<WeatherResponse.ResultWeather>>() {
            @Override
            public ObservableSource<WeatherResponse.ResultWeather> apply(@NonNull WeatherResponse weatherResponse) throws Exception {
                return Observable.just(weatherResponse.getResult());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResponse.ResultWeather>() {
                    @Override
                    public void accept(WeatherResponse.ResultWeather weather) throws Exception {

                        WeatherResponse.ResultWeather.TodayWeather todayWeather = weather.getToday();
                        mTvCurrentDate.setText(todayWeather.getDate_y());
                        mTvCurrentWeek.setText(todayWeather.getWeek());
                        mTvCurrentWeather.setText(todayWeather.getWeather());
                        mTvTodayWind.setText(todayWeather.getWind());

                        String temp = todayWeather.getTemperature();
                        String[] temps = temp.split("~");
                        String lowTemp = temps[0];
                        String heightTemp = temps[1];
//
                        mTvLowTemp.setText(lowTemp.substring(0, 2));
                        mTvHeightTemp.setText(heightTemp.substring(0, 2));

                        String fa = todayWeather.getWeather_id().getFa();
                        String fb = todayWeather.getWeather_id().getFb();
                        if (fa.equals(fb)) {
                            mWeatherPhoto2.setVisibility(View.GONE);
                            int index = Integer.valueOf(fa);
                            Glide.with(WeatherActivity.this).load(imageIds[index]).into(mWeatherPhoto);
                        } else {
                            mWeatherPhoto2.setVisibility(View.VISIBLE);
                            int index1 = Integer.valueOf(fa);
                            int index2 = Integer.valueOf(fb);
                            Glide.with(WeatherActivity.this).load(imageIds[index1]).into(mWeatherPhoto);
                            Glide.with(WeatherActivity.this).load(imageIds[index2]).into(mWeatherPhoto2);
                        }

                        WeatherResponse.ResultWeather.SkWeather sk = weather.getSk();
                        mTvCurrentTemp.setText(sk.getTemp());
                        mTvCurrentWind.setText(sk.getWind_direction() + "\n" + sk.getWind_strength());
                        mTvCurrentHumidity.setText("湿度\n" + sk.getHumidity());
                        mTvUpdateTime.setText("更新时间: " + sk.getTime());

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @OnClick({R.id.swipe_refresh_layout, R.id.add_city_btn, R.id.show_city_recycler_view, R.id.drawer_layout})
    public void onViewClicked() {

    }
}
