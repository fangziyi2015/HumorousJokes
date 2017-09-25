package com.fangz.humorousjokes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangz.humorousjokes.R;
import com.fangz.humorousjokes.db.Citys;
import com.fangz.humorousjokes.utils.L;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by zhangtao on 2017/9/25.
 */

public class SearchCityRecyclerViewAdapter extends RecyclerView.Adapter<SearchCityRecyclerViewAdapter.ViewHolder> {

    private List<Citys> mCityses;
    private Context mContext;

    public SearchCityRecyclerViewAdapter(List<Citys> cityses) {
        mCityses = cityses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_city_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Citys c = mCityses.get(position);
        holder.mTvShowCity.setText("中国-"+c.getProvinceName()+"-"+c.getCityName()+"-"+c.getDistrictName());
        L.d("城市："+"中国-"+c.getProvinceName()+"-"+c.getCityName()+"-"+c.getDistrictName());
        holder.mTvShowCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(c);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCityses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvShowCity;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvShowCity = (TextView) itemView;
        }
    }
}
