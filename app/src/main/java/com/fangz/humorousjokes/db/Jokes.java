package com.fangz.humorousjokes.db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhangtao on 2017/9/22.
 */

public class Jokes extends DataSupport {

    private String jokeContent;
    private String updateTime;

    public Jokes() {
    }

    public Jokes(String jokeContent, String updateTime) {
        this.jokeContent = jokeContent;
        this.updateTime = updateTime;
    }

    public String getJokeContent() {
        return jokeContent;
    }

    public void setJokeContent(String jokeContent) {
        this.jokeContent = jokeContent;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
