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

import butterknife.BindView;
import butterknife.ButterKnife;

public class JokeDetailActivity extends BaseActivity {


    public static final String JOKE_CONTENT = "joke_content";
    public static final String JOKE_UPDATE_TIME = "joke_update_time";
    public static final String JOKE_IMG = "joke_img";

    @BindView(R.id.detail_img)
    ImageView mDetailImg;
    @BindView(R.id.detail_toolbar)
    Toolbar mDetailToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.detail_update_time)
    TextView mDetailUpdateTime;
    @BindView(R.id.detail_content)
    TextView mDetailContent;
    @BindView(R.id.float_comment_btn)
    FloatingActionButton mFloatCommentBtn;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_detail);
        ButterKnife.bind(this);

        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();

        initData();

    }

    private void initData() {
        setSupportActionBar(mDetailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String content = extras.getString(JOKE_CONTENT);
                String updateTime = extras.getString(JOKE_UPDATE_TIME);
                int imageId = extras.getInt(JOKE_IMG, R.drawable.landscape_a);

                mDetailContent.setText(content);
                mDetailUpdateTime.setText(updateTime);
                Glide.with(JokeDetailActivity.this).load(imageId).into(mDetailImg);
            }
        }

        mCollapsingToolbar.setTitle("开心一刻");

        mFloatCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 评论专区
                T.showShort(JokeDetailActivity.this, "评论");
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
