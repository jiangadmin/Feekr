package com.tl.film.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tl.film.R;

/**
 * Created by chenxiaoping on 2017/3/28.
 */

public class RecyclerCoverFlow_Adapter extends RecyclerView.Adapter<RecyclerCoverFlow_Adapter.ViewHolder> {

    private Context mContext;
    private int[] mColors = {R.drawable.movie, R.drawable.movie, R.drawable.movie, R.drawable.movie,
            R.drawable.movie, R.drawable.movie};

    private ItemClick clickCb;

    public RecyclerCoverFlow_Adapter(Context c) {
        mContext = c;
    }

    public RecyclerCoverFlow_Adapter(Context c, ItemClick cb) {
        mContext = c;
        clickCb = cb;
    }

    public void setOnClickLstn(ItemClick cb) {
        this.clickCb = cb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        Glide.with(mContext).load(mColors[position % mColors.length]).into(holder.img);
        holder.img.setBackgroundColor(0xffffffff);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "点击了："+position, Toast.LENGTH_SHORT).show();
                if (clickCb != null) {
                    clickCb.clickItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mColors.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

    interface ItemClick {
        void clickItem(int pos);
    }
}