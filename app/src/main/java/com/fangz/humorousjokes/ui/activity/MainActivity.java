package com.fangz.humorousjokes.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import org.zackratos.ultimatebar.UltimateBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private int page = 1;

    private static final String TAG = "MainActivity";
    private static final String KEY = "key";
    private static final String PAGE = "page";
    private static final String PAGESIZE = "pagesize";
    private static final String SORT = "sort";
    private static final String TIME = "time";
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private android.support.v7.widget.Toolbar maintoolbar;
    private android.support.design.widget.NavigationView navigationview;
    private android.support.v4.widget.DrawerLayout drawerlayout;
    private android.support.v7.widget.RecyclerView recycleview;

    private FloatingActionButton mainfloatbtn;

    private List<Jokes> mJokes;
    private android.support.design.widget.AppBarLayout appbarlayout;
    private android.support.v4.widget.SwipeRefreshLayout swiperefreshlayout;
    private JokeRecyclerViewAdapter mJokeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void initView() {
        this.swiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.recycleview = (RecyclerView) findViewById(R.id.recycle_view);
        this.drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navigationview = (NavigationView) findViewById(R.id.navigation_view);
        this.maintoolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        this.mainfloatbtn = (FloatingActionButton) findViewById(R.id.main_float_btn);

        View header_layout = LayoutInflater.from(MainActivity.this).inflate(R.layout.header_layout, null);
        CircleImageView headerView = (CircleImageView) header_layout.findViewById(R.id.header_view);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看个人信息
            }
        });

        navigationview.addHeaderView(header_layout);
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerlayout.closeDrawers();
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
                        Intent intent = new Intent(MainActivity.this,WeatherActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        maintoolbar.setNavigationIcon(R.drawable.home);
        maintoolbar.setTitle(getResources().getString(R.string.main_title));
        setSupportActionBar(maintoolbar);

        maintoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlayout.openDrawer(GravityCompat.START);
            }
        });

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新
                requestJokes(String.valueOf(++page));
                L.d("page==>"+page);
                swiperefreshlayout.setRefreshing(false);
            }
        });

    }

    private void initData() {

//        mJokes = DataSupport.limit(10).find(Jokes.class);
//        if (mJokes.size() > 0) {
//            for (Jokes j : mJokes) {
//                Logger.i("笑话==>" + j.getJokeContent(), "");
//            }
//        }

        mJokes = new ArrayList<>();

        mJokeRecyclerViewAdapter = new JokeRecyclerViewAdapter(mJokes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(layoutManager);

        recycleview.setAdapter(mJokeRecyclerViewAdapter);

        mainfloatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleview.smoothScrollToPosition(0);
            }
        });

//        Retrofit retrofit = new Retrofit.Builder()
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .jokeBaseUrl(Url.jokeBaseUrl)
//                .build();
//
//        mApi = retrofit.create(JokeApi.class);


        requestJokes(String.valueOf(page));

    }

    private void requestJokes(String page) {
        HashMap<String, String> map = new HashMap();
        map.put(KEY, Constant.Key.joke_value);
        map.put(PAGE, page);
        map.put(PAGESIZE, "20");
        map.put(SORT, ASC);
        map.put(TIME, "1499999999");
        Observable<JokeResponse> jokeResponseObservable = JokeApplication.getJokeApi().getJoke(map);
        jokeResponseObservable.flatMap(new Function<JokeResponse, ObservableSource<JokeResponse.ResultData>>() {
            @Override
            public ObservableSource<JokeResponse.ResultData> apply(@io.reactivex.annotations.NonNull JokeResponse jokeResponse) throws Exception {
                return Observable.just(jokeResponse.getResult());
            }
        }).flatMap(new Function<JokeResponse.ResultData, ObservableSource<List<JokeResponse.ResultData.JokeContent>>>() {
            @Override
            public ObservableSource<List<JokeResponse.ResultData.JokeContent>> apply(@io.reactivex.annotations.NonNull JokeResponse.ResultData resultData) throws Exception {
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
//                            joke.save();
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
