package com.fangz.humorousjokes.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fangz.humorousjokes.R;
import com.fangz.humorousjokes.adapter.SearchCityRecyclerViewAdapter;
import com.fangz.humorousjokes.db.Citys;
import com.fangz.humorousjokes.utils.L;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class SearchCityActivity extends AppCompatActivity {

    @BindView(R.id.search_toolbar)
    Toolbar mSearchToolbar;
    @BindView(R.id.city_recycler_view)
    RecyclerView mCityRecyclerView;

    private SearchView mSearchView;
    ArrayList<Citys> cityes = new ArrayList<>();
    private SearchCityRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        ButterKnife.bind(this);

        setSupportActionBar(mSearchToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAdapter = new SearchCityRecyclerViewAdapter(cityes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchCityActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCityRecyclerView.setLayoutManager(layoutManager);
        mCityRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_toolbar, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        /*------------------ SearchView有三种默认展开搜索框的设置方式，区别如下： ------------------*/
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
//        mSearchView.setIconified(false);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
//        mSearchView.onActionViewExpanded();

        //设置最大宽度
//        mSearchView.setMaxWidth(500);
//设置是否显示搜索框展开时的提交按钮
        mSearchView.setSubmitButtonEnabled(true);
//设置输入框提示语
        mSearchView.setQueryHint("查找城市");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                L.d("onQueryTextSubmit:" + query);
                EventBus.getDefault().postSticky(query);
                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                L.d("onQueryTextChange:" + newText);
                searchCity(newText);
                return false;
            }


        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchCity(String newText) {
        cityes.clear();
        String sql = "select * from Citys where provincename like '%" + newText + "%' or cityname like '%" + newText + "%' or districtname like '%" + newText + "%';";
//        String sql = "select * from Citys where districtname like '%"+newText+"%';";
        Cursor cursor = DataSupport.findBySQL(sql);
//        if(cursor.moveToFirst()){
        while (cursor.moveToNext()) {
            String provinceName = cursor.getString(cursor.getColumnIndex("provincename"));
            String cityName = cursor.getString(cursor.getColumnIndex("cityname"));
            String districtName = cursor.getString(cursor.getColumnIndex("districtname"));

            L.d("provinceName:" + provinceName);
            L.d("cityName:" + cityName);
            L.d("districtName:" + districtName);
            L.d("城市:" + provinceName+"-"+cityName+"-"+districtName);

            Citys c = new Citys(provinceName, cityName, districtName);
            cityes.add(c);

        }

        mAdapter.notifyDataSetChanged();

//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(SearchCityActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(SearchCityActivity.this);
    }

    @Subscribe(sticky = true)
    public void receiverCitys(Citys citys){
        EventBus.getDefault().postSticky(citys.getDistrictName());
        finish();
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

    @OnClick(R.id.city_recycler_view)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_recycler_view:
                break;
        }
    }
}
