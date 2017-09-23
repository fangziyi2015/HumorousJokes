package com.fangz.humorousjokes.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fangz.humorousjokes.R;
import com.fangz.humorousjokes.adapter.JokeRecyclerViewAdapter;
import com.fangz.humorousjokes.app.JokeApplication;
import com.fangz.humorousjokes.bean.JokeResponse;
import com.fangz.humorousjokes.constants.Constant;
import com.fangz.humorousjokes.db.Jokes;
import com.fangz.humorousjokes.ui.BaseActivity;
import com.fangz.humorousjokes.utils.L;
import com.fangz.humorousjokes.utils.T;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;
import org.zackratos.ultimatebar.UltimateBar;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_tool_bar)
    Toolbar mMainToolBar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.main_float_btn)
    FloatingActionButton mMainFloatBtn;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private int page = 1;
    private JokeRecyclerViewAdapter mJokeRecyclerViewAdapter;

    private static final String TAG = "MainActivity";
    private static final String KEY = "key";
    private static final String PAGE = "page";
    private static final String PAGESIZE = "pagesize";
    private static final String SORT = "sort";
    private static final String TIME = "time";
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private List<Jokes> mJokes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();

        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void initView() {
        View header_layout = LayoutInflater.from(MainActivity.this).inflate(R.layout.header_layout, null);
        CircleImageView headerView = (CircleImageView) header_layout.findViewById(R.id.header_view);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看个人信息
            }
        });

        mNavigationView.addHeaderView(header_layout);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.my:
                        // 我的
                        T.showShort(MainActivity.this, "我的");
                        break;
                    case R.id.love:
                        T.showShort(MainActivity.this, "喜欢");
                        // 喜欢
                        break;
                    case R.id.collect:
                        T.showShort(MainActivity.this, "收藏");
                        // 收藏
                        break;
                    case R.id.joke:
                        T.showShort(MainActivity.this, "开心一笑");
                        // 开心一笑
                        break;
                    case R.id.weather:
                        T.showShort(MainActivity.this, "今日天气");
                        // 今日天气
                        Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
//
        mMainToolBar.setNavigationIcon(R.drawable.home);
        mMainToolBar.setTitle(getResources().getString(R.string.main_title));
        setSupportActionBar(mMainToolBar);

        mMainToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新
                requestJokes(String.valueOf(++page));
                L.d("page==>" + page);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void initData() {

        mJokes = DataSupport.limit(10).find(Jokes.class);
        mJokeRecyclerViewAdapter = new JokeRecyclerViewAdapter(mJokes);

        if (mJokes.size() == 0) {
            requestJokes(String.valueOf(page));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mJokeRecyclerViewAdapter);


        mMainFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecycleView.smoothScrollToPosition(0);
            }
        });
//


    }

    private void requestJokes(String page) {
        DataSupport.deleteAll(Jokes.class);
        HashMap<String, String> map = new HashMap();
        map.put(KEY, Constant.Key.joke_value);
        map.put(PAGE, page);
        map.put(PAGESIZE, "20");
        map.put(SORT, ASC);
        map.put(TIME, "1499999999");
        Observable<JokeResponse> jokeResponseObservable = JokeApplication.getJokeApi().getJoke(map);
        L.d("jokeResponseObservable===>" + jokeResponseObservable);
        jokeResponseObservable.flatMap(new Function<JokeResponse, ObservableSource<JokeResponse.ResultData>>() {
            @Override
            public ObservableSource<JokeResponse.ResultData> apply(@NonNull JokeResponse jokeResponse) throws Exception {
                return Observable.just(jokeResponse.getResult());
            }
        }).flatMap(new Function<JokeResponse.ResultData, ObservableSource<List<JokeResponse.ResultData.JokeContent>>>() {
            @Override
            public ObservableSource<List<JokeResponse.ResultData.JokeContent>> apply(@NonNull JokeResponse.ResultData resultData) throws Exception {
                return Observable.just(resultData.getData());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<JokeResponse.ResultData.JokeContent>>() {
                    @Override
                    public void accept(List<JokeResponse.ResultData.JokeContent> jokeContents) throws Exception {
                        mJokes.clear();
                        for (JokeResponse.ResultData.JokeContent content : jokeContents) {
                            //Log.i(TAG, "accept: "+content.getContent());
                            Logger.i("笑话内容==>" + content.getContent(), "====");
                            Jokes joke = new Jokes();
                            joke.setJokeContent(content.getContent());
                            joke.setUpdateTime(content.getUpdatetime());
                            joke.save();
//
                            mJokes.add(joke);
                        }
                        mJokeRecyclerViewAdapter.notifyDataSetChanged();

                    }
                });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_collect:
                break;
            case R.id.toolbar_love:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
