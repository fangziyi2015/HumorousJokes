package com.fangz.humorousjokes.db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhangtao on 2017/9/22.
 */

public class Citys extends DataSupport {

    private int id;
    private String provinceName;
    private String cityName;
    private String districtName;

    public Citys() {

    }

    public Citys(int id, String provinceName, String cityName, String districtName) {
        this.id = id;
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.districtName = districtName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
