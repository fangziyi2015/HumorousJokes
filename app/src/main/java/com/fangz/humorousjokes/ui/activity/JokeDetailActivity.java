package com.fangz.humorousjokes.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fangz.humorousjokes.R;
import com.fangz.humorousjokes.ui.BaseActivity;
import com.fangz.humorousjokes.utils.T;

import org.zackratos.ultimatebar.UltimateBar;

public class JokeDetailActivity extends BaseActivity {

    private android.widget.ImageView detailimg;
    private android.support.v7.widget.Toolbar detailtoolbar;
    private android.support.design.widget.CollapsingToolbarLayout collapsingtoolbar;
    private android.widget.TextView detailcontent;
    private android.widget.TextView detailupdatetime;
    private android.support.design.widget.FloatingActionButton floatcommentbtn;
    private android.support.design.widget.CoordinatorLayout coordinatorlayout;

    public static final String JOKE_CONTENT = "joke_content";
    public static final String JOKE_UPDATE_TIME = "joke_update_time";
    public static final String JOKE_IMG = "joke_img";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_detail);

        initView();

        initData();

    }

    private void initView() {
        this.coordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        this.floatcommentbtn = (FloatingActionButton) findViewById(R.id.float_comment_btn);
        this.detailcontent = (TextView) findViewById(R.id.detail_content);
        this.detailupdatetime = (TextView) findViewById(R.id.detail_update_time);
        this.collapsingtoolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        this.detailtoolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        this.detailimg = (ImageView) findViewById(R.id.detail_img);

        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();

    }

    private void initData() {
        setSupportActionBar(detailtoolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null){
            Bundle extras = intent.getExtras();
            if (extras != null){
                String content = extras.getString(JOKE_CONTENT);
                String updateTime = extras.getString(JOKE_UPDATE_TIME);
                int imageId = extras.getInt(JOKE_IMG,R.drawable.landscape_a);

                detailcontent.setText(content);
                detailupdatetime.setText(updateTime);
                Glide.with(JokeDetailActivity.this).load(imageId).into(detailimg);
            }
        }

        collapsingtoolbar.setTitle("开心一刻");

        floatcommentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 评论专区
                T.showShort(JokeDetailActivity.this,"评论");
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
