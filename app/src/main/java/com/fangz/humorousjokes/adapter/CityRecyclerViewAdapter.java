package com.fangz.humorousjokes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangz.humorousjokes.R;

import java.util.List;

/**
 * Created by zhangtao on 2017/9/25.
 */

public class CityRecyclerViewAdapter extends RecyclerView.Adapter<CityRecyclerViewAdapter.ViewHolder> {

    private List<String> mCityNames;
    private Context mContext;

    public CityRecyclerViewAdapter(List<String> cityNames) {
        mCityNames = cityNames;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.city_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mCityNames.get(position);
        holder.mTvCityName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mCityNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvCityName;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvCityName = (TextView) itemView.findViewById(R.id.tv_city_name);
        }
    }
}
