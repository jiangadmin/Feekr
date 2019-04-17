package com.tl.film.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tl.film.R;
import com.tl.film.model.FirstFilms_Model;
import com.tl.film.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiaoping on 2017/3/28.
 */

public class RecyclerCoverFlow_Adapter extends RecyclerView.Adapter<RecyclerCoverFlow_Adapter.ViewHolder> {
    private static final String TAG = "RecyclerCoverFlow_Adapt";

    private Context mContext;

    int color[] = {0xff000000, 0xff112233, 0xff223344, 0xff334455, 0xff445566, 0xff556677, 0xff667788, 0xff778899};
    List<FirstFilms_Model.DataBean> dataBeans = new ArrayList<>();

    public void setDataBeans(List<FirstFilms_Model.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }

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
    public void onBindViewHolder(ViewHolder holder, int position) {

        FirstFilms_Model.DataBean bean = dataBeans.get(position);
        holder.img.setTag(position);
        holder.img.setBackgroundColor(0xff778899);
        Picasso.with(mContext).load(bean.getBgImage()).into(holder.img);
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(mContext, "点击了：" + position, Toast.LENGTH_SHORT).show();
            if (clickCb != null) {
                clickCb.clickItem(position);
            }
        });


    }

    @Override
    public int getItemCount() {
//        LogUtil.e(TAG, "总量：" + dataBeans.size());
        return dataBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            //item可以获得焦点，需要设置这个属性。
            itemView.setFocusable(true);
            itemView.setFocusableInTouchMode(true);
        }

    }

    interface ItemClick {
        void clickItem(int pos);
    }
}