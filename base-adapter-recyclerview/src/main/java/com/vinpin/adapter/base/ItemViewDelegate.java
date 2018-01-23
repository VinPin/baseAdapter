package com.vinpin.adapter.base;

/**
 * 条目的代表接口
 *
 * @author zwp
 *         create at 2018/01/23 13:14
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);
}
