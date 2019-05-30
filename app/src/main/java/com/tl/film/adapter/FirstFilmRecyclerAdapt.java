package com.tl.film.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tl.film.R;
import com.tl.film.model.FirstFilms_Model;
import com.tl.film.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiaoping on 2017/3/28.
 */

public class FirstFilmRecyclerAdapt extends RecyclerView.Adapter<FirstFilmRecyclerAdapt.ViewHolder> {
    private static final String TAG = "FirstFilmRecyclerAdapt";

    private Context mContext;
    private List<FirstFilms_Model.DataBean> dataBeans = new ArrayList<>();

    private int currentPosition;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public interface OnItemSelectListener {
        void onItemSelect(View view, int position);
    }

    private OnItemClickListener mListener;
    private OnItemSelectListener mSelectListener;

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        mSelectListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public void setDataBeans(List<FirstFilms_Model.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }

    private ItemClick clickCb;

    public FirstFilmRecyclerAdapt(Context c, ItemClick cb) {
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

        FirstFilms_Model.DataBean bean = dataBeans.get(position);
        holder.img.setTag(position);

        Picasso.with(mContext).load(bean.getBgImage()).error(R.mipmap.item_bg).placeholder(R.mipmap.item_bg).into(holder.img);
        holder.img.setOnClickListener(v -> {
            if (clickCb != null) {
                clickCb.clickItem(bean);
            }
        });


        holder.itemView.setFocusable(true);
        holder.itemView.setTag(position);
        holder.itemView.setOnFocusChangeListener((v, hasFocus) -> {
            LogUtil.e(TAG, "hasfocus:" + position + "--" + hasFocus);
            if (hasFocus) {
                currentPosition = (int) holder.itemView.getTag();
                mSelectListener.onItemSelect(holder.itemView, currentPosition);
            }
        });
        if (mListener != null) {
            holder.itemView.setOnClickListener(v -> mListener.onItemClick(v, holder.getLayoutPosition()));
            holder.itemView.setOnLongClickListener(v -> {
                mListener.onItemLongClick(v, holder.getLayoutPosition());
                return true;
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            //item可以获得焦点，需要设置这个属性。
            img.setFocusable(true);
            img.setFocusableInTouchMode(true);
        }

    }

    public interface ItemClick {
        void clickItem(FirstFilms_Model.DataBean bean);
    }
}