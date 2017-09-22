package com.fangz.humorousjokes.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fangz.humorousjokes.R;
import com.fangz.humorousjokes.db.Jokes;
import com.fangz.humorousjokes.ui.activity.JokeDetailActivity;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by zhangtao on 2017/9/22.
 */

public class JokeRecyclerViewAdapter extends RecyclerView.Adapter<JokeRecyclerViewAdapter.ViewHolder> {

    private List<Jokes> mJokes;
    private Context mContext;
    private int[] imageIds = new int[]{
            R.drawable.landscape_a,
            R.drawable.landscape_b,
            R.drawable.landscape_c,
            R.drawable.landscape_d,
            R.drawable.landscape_e,
            R.drawable.landscape_f
    };

    public JokeRecyclerViewAdapter(List<Jokes> jokes/*, Context context*/) {
        mJokes = jokes;
//        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.joke_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Jokes joke = mJokes.get(position);
        final String content = joke.getJokeContent();
        final String updateTime = joke.getUpdateTime();
        final int imageId = imageIds[position%imageIds.length];

        Logger.i("content:"+content,"===");

        holder.jokeContent.setText(content);
        holder.jokeUpdateTime.setText(updateTime);
        Glide.with(mContext).load(imageId).into(holder.jokeImg);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转笑话详情界面
                Intent intent = new Intent(mContext, JokeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(JokeDetailActivity.JOKE_CONTENT,content);
                bundle.putString(JokeDetailActivity.JOKE_UPDATE_TIME,updateTime);
                bundle.putInt(JokeDetailActivity.JOKE_IMG,imageId);

                intent.putExtras(bundle);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mJokes != null){
            return mJokes.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView jokeImg;
        public TextView jokeTitle;
        public TextView jokeContent;
        public TextView jokeUpdateTime;

        public CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView;
            jokeImg = (ImageView) mCardView.findViewById(R.id.joke_img);
            jokeTitle = (TextView) mCardView.findViewById(R.id.joke_title);
            jokeContent = (TextView) mCardView.findViewById(R.id.joke_content);
            jokeUpdateTime = (TextView) mCardView.findViewById(R.id.joke_update_time);

        }
    }
}
