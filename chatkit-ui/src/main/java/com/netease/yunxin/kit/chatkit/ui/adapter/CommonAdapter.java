package com.netease.yunxin.kit.chatkit.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;


import com.netease.yunxin.kit.chatkit.ui.util.ItemViewDelegate;
import com.netease.yunxin.kit.chatkit.ui.util.ViewHolder;

import java.util.List;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, T lsatT, final int position, int itmeCounts) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(final ViewHolder holder, T t, final int position);


}
