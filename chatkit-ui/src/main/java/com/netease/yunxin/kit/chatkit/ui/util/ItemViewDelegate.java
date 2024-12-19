package com.netease.yunxin.kit.chatkit.ui.util;


/**
 * Created by zhy on 16/6/22.
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T item, T lastItem, int position,int itemCounts);

}
