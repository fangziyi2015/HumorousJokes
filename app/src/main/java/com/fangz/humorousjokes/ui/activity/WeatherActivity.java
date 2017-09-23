package com.fangz.humorousjokes.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.fangz.humorousjokes.R;
import com.fangz.humorousjokes.app.JokeApplication;
import com.fangz.humorousjokes.bean.CitysResponse;
import com.fangz.humorousjokes.bean.WeatherResponse;
import com.fangz.humorousjokes.constants.Constant;
import com.fangz.humorousjokes.db.Citys;
import com.fangz.humorousjokes.utils.L;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WeatherActivity extends AppCompatActivity {

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


    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//该省的所有地区列表（第三极）
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();//该省的所有地区列表（第三极）
    private static final String FORMAT = "format";
    private static final String DEAULT_FARMAT = "2";// 返回格式：默认格式是 2
    private static final String CITY_NAME = "cityname";
    private static final String KEY = "key";

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
                    mDistrict = amapLocation.getDistrict();
                    L.d("定位地址：" + mDistrict);
                    requestWeatherInfo(mDistrict);
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
                    requestWeatherInfo(mDistrict);

                }
            }
        }
    };

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String mDistrict = "上海";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);


//        UltimateBar ultimateBar = new UltimateBar(this);
//        ultimateBar.setImmersionBar();

        // 设置城市
        setLocationListener();

        // 初始化高德地图定位
        initAMapLocation();

        initSwipeRefreshLitener();

    }

    private void initSwipeRefreshLitener() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeatherInfo(mDistrict);
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

    private void setLocationListener() {
        mTvCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.create(new ObservableOnSubscribe<List<Citys>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<Citys>> e) throws Exception {
                        List<Citys> citys = DataSupport.findAll(Citys.class);
                        e.onNext(citys);
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Citys>>() {
                            @Override
                            public void accept(List<Citys> cityses) throws Exception {
                                L.d("cityses====size====" + cityses.size());
                                if (cityses.size() > 0) {
                                    showCitys(cityses);
                                } else {
                                    requestCityList();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

//        requestWeatherInfo(mDistrict);
    }

    private void requestCityList() {
        final Observable<CitysResponse> weatherInfo = JokeApplication.getWeatherApi().getCitysListInfo(Constant.Key.weather_value);
        Logger.i("==>" + weatherInfo, "==");
        weatherInfo.flatMap(new Function<CitysResponse, ObservableSource<List<CitysResponse.ResultCity>>>() {
            @Override
            public ObservableSource<List<CitysResponse.ResultCity>> apply(@NonNull CitysResponse weatherResponse) throws Exception {
                return Observable.just(weatherResponse.getResult());
            }
        }).flatMap(new Function<List<CitysResponse.ResultCity>, ObservableSource<List<Citys>>>() {
            @Override
            public ObservableSource<List<Citys>> apply(@NonNull List<CitysResponse.ResultCity> resultCities) throws Exception {
                List<Citys> citys = new ArrayList<Citys>();
                for (CitysResponse.ResultCity city : resultCities) {
                    //Logger.i("省："+city.getProvince()+"\n市："+city.getCity()+"\n县："+city.getDistrict());
                    Citys c = new Citys(Integer.valueOf(city.getId()), city.getProvince(), city.getCity(), city.getDistrict());
                    c.save();
                    citys.add(c);
                }
                return Observable.just(citys);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<Citys>>() {
                    @Override
                    public void accept(List<Citys> cityses) throws Exception {
//                        showCityList(citys);
                        showCitys(cityses);
                    }
                });

    }
//
//    private void showCityList(List<Citys> citys) {
//        options1Items.clear();
//        options2Items.clear();
//        options3Items.clear();
//        for (Citys city : citys) {
//            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
//            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
//            if (!options1Items.contains(city.getProvinceName())) {
//                options1Items.add(city.getProvinceName());
//
//                L.d("省：" + city.getProvinceName());
//
//                List<Citys> c2 = DataSupport.where("provincename = ?", city.getProvinceName()).find(Citys.class);
//                for (Citys c : c2
//                        ) {
//                    ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
//                    if (!CityList.contains(c.getCityName())) {
//                        CityList.add(c.getCityName());
//                        L.d("市：" + c.getCityName());
//                        List<Citys> c3 = DataSupport.where("cityname = ?", c.getCityName()).find(Citys.class);
//                        for (Citys d : c3
//                                ) {
//                            if (!City_AreaList.contains(d.getDistrictName())) {
//                                City_AreaList.add(d.getDistrictName());
//                            }
//                        }
//
//                        Province_AreaList.add(City_AreaList);
//                    }
//
//                }
//                options2Items.add(CityList);
//                options3Items.add(Province_AreaList);
//
//            }
//
//        }
//
//        L.d("options1Items.Size===>" + options1Items.size());
//        L.d("options2Items.Size===>" + options2Items.size());
//        L.d("options3Items.Size===>" + options3Items.size());
//
//
//        //条件选择器
//        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                L.d("options1" + options1);
//                L.d("option2" + option2);
//                L.d("options3" + options3);
//                //返回的分别是三个级别的选中位置
//                String tx = options1Items.get(options1)
//                        + options2Items.get(options1).get(option2)
//                        + options3Items.get(options1).get(option2).get(options3);
////                //tvOptions.setText(tx);
////                L.d("===>"+tx);
//            }
//        }).build();
//        pvOptions.setPicker(options1Items, options2Items, options3Items);
//        pvOptions.show();
//    }


    private void showCitys(List<Citys> citys) {
        options1Items.clear();
        options2Items.clear();
        options3Items.clear();
        Observable.just(citys).flatMap(new Function<List<Citys>, ObservableSource<ArrayList<String>>>() {
            @Override
            public ObservableSource<ArrayList<String>> apply(@NonNull List<Citys> cityses) throws Exception {
                for (Citys c : cityses
                        ) {
                    if (!options1Items.contains(c.getProvinceName())) {
                        options1Items.add(c.getProvinceName());
                    }
                }
                return Observable.just(options1Items);
            }
        }).flatMap(new Function<ArrayList<String>, ObservableSource<ArrayList<ArrayList<String>>>>() {
            @Override
            public ObservableSource<ArrayList<ArrayList<String>>> apply(@NonNull ArrayList<String> provinces) throws Exception {
                for (String province : provinces) {
                    ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
                    List<Citys> cityses = DataSupport.where("provincename = ?", province).find(Citys.class);
                    for (Citys c : cityses) {
                        if (!cityList.contains(c.getCityName())) {
                            cityList.add(c.getCityName());
                        }
                    }

                    options2Items.add(cityList);
                }
                return Observable.just(options2Items);
            }
        }).flatMap(new Function<ArrayList<ArrayList<String>>, ObservableSource<ArrayList<ArrayList<ArrayList<String>>>>>() {
            @Override
            public ObservableSource<ArrayList<ArrayList<ArrayList<String>>>> apply(@NonNull ArrayList<ArrayList<String>> provinceCitys) throws Exception {
                for (ArrayList<String> provinceCity : provinceCitys) {
                    ArrayList<ArrayList<String>> provinceAreaList = new ArrayList<>();//该省的所有地区列表（第三极）
                    for (String city : provinceCity) {
                        ArrayList<String> areaList = new ArrayList<>();//该城市的所有地区列表
                        List<Citys> cityes = DataSupport.where("cityname = ?", city).find(Citys.class);
                        for (Citys area : cityes) {
                            if (!areaList.contains(area.getDistrictName())) {
                                areaList.add(area.getDistrictName());
                            }
                        }

                        provinceAreaList.add(areaList);
                    }

                    options3Items.add(provinceAreaList);
                }
                return Observable.just(options3Items);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<ArrayList<ArrayList<String>>>>() {
                    @Override
                    public void accept(ArrayList<ArrayList<ArrayList<String>>> arrayLists) throws Exception {
                        OptionsPickerView pvOptions = new OptionsPickerView.Builder(WeatherActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                                L.d("options1" + options1);
                                L.d("option2" + option2);
                                L.d("options3" + options3);
                                //返回的分别是三个级别的选中位置
                                String tx = options1Items.get(options1)
                                        + options2Items.get(options1).get(option2)
                                        + options3Items.get(options1).get(option2).get(options3);

                                mDistrict = options3Items.get(options1).get(option2).get(options3);
                                L.d("选择的地址：" + mDistrict);
                                requestWeatherInfo(mDistrict);

//                //tvOptions.setText(tx);
//                L.d("===>"+tx);
                            }
                        })
//                                .setSubmitText("确定")//确定按钮文字
//                                .setCancelText("取消")//取消按钮文字
//                                .setTitleText("城市选择")//标题
                                .setSubCalSize(16)//确定和取消文字大小
//                                .setTitleSize(20)//标题文字大小
//                                .setTitleColor(Color.BLACK)//标题文字颜色
//                                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
//                                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                                .setContentTextSize(16)//滚轮文字大小
//                                .setLinkage(true)//设置是否联动，默认true
                                .setLabels("省", "市", "县")//设置选择的三级单位
//                                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                                .setTextColorCenter(Color.parseColor("#000000"))
//                                .setCyclic(false, false, false)//循环与否
//                                .setSelectOptions(1, 1, 1)  //设置默认选中项
//                                .setOutSideCancelable(true)//点击外部dismiss default true
//                                .isDialog(false)//是否显示为对话框样式
                                .build();
                        pvOptions.setPicker(options1Items, options2Items, options3Items);
                        pvOptions.show();
                    }
                });
    }


    public void requestWeatherInfo(String cityName) {
        if (TextUtils.isEmpty(cityName)) {
            cityName = "上海";
        }

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
                        mTvUpdateTime.setText("更新时间: "+sk.getTime());

                    }
                });


//        Observable<WeatherIconInfoResponse> weatherIconCode = JokeApplication.getWeatherApi().getWeatherIconCode(Constant.Key.weather_value);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @OnClick(R.id.swipe_refresh_layout)
    public void onViewClicked() {
    }
}
