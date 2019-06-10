package com.tl.film.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tl.film.R;
import com.tl.film.model.FirstFilms_Model;
import com.tl.film.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxiaoping on 2017/3/28.
 */

public class RecyclerCoverFlow_Adapter extends RecyclerView.Adapter<RecyclerCoverFlow_Adapter.ViewHolder> {
    private static final String TAG = "RecyclerCoverFlow_Adapt";

    private Context mContext;

    private List<FirstFilms_Model.DataBean> dataBeans = new ArrayList<>();
    private Map<Integer, ViewHolder> ViewHolderList = new HashMap();

    public void setDataBeans(List<FirstFilms_Model.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }

    private ItemClick clickCb;

    public RecyclerCoverFlow_Adapter(Context c, ItemClick cb) {
        mContext = c;
        clickCb = cb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolderList.put(position, holder);
        FirstFilms_Model.DataBean bean = dataBeans.get(position);

        holder.itemView.setTag(position);

        holder.shoufei.setVisibility(bean.getTxPayStatus() == 8 ? View.GONE : View.VISIBLE);

        Picasso.with(mContext).load(bean.getBgImage()).into(holder.img);

        holder.img.setTag(R.id.img, holder);

        holder.img.setOnClickListener(v -> {
            if (clickCb != null) {
                clickCb.clickItem(bean);
            }
        });

        holder.img.setOnFocusChangeListener((v, hasFocus) -> {
            LogUtil.e(TAG, position + "--" + hasFocus);
            //只要当前有焦点的view
            if (hasFocus) {
                //当前焦点位置
                clickCb.focusableItem(position);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (dataBeans != null) {
            return dataBeans.size();
        } else {
            return 0;
        }
    }

    public ViewHolder getViewHolder(int position) {
        return this.ViewHolderList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        RelativeLayout shoufei;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            shoufei = itemView.findViewById(R.id.shoufei);
            //item可以获得焦点，需要设置这个属性。
            img.setFocusable(true);
            img.setFocusableInTouchMode(true);
        }

    }

    public interface ItemClick {

        void clickItem(FirstFilms_Model.DataBean bean);

        void focusableItem(int position);
    }
}