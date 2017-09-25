package com.fangz.humorousjokes.db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhangtao on 2017/9/25.
 */

public class City extends DataSupport {

    private String name;

    public City() {
    }

    public City(String provinceId, int id, String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
